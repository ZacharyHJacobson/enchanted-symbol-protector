package com.enchantedsymbolprotector;

import javax.inject.Inject;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Set;

@Slf4j
@PluginDescriptor(
	name = "Enchanted Symbol Protector"
)
public class EnchantedSymbolProtectorPlugin extends Plugin
{
	private static final Set<Integer> QUIVER_IDS = Set.of(
			ItemID.DIZANAS_QUIVER_UNCHARGED,
			ItemID.DIZANAS_QUIVER_BROKEN,
			ItemID.DIZANAS_QUIVER_UNCHARGED_TROUVER,
			ItemID.DIZANAS_QUIVER_CHARGED,
			ItemID.DIZANAS_QUIVER_CHARGED_TROUVER,
			ItemID.DIZANAS_QUIVER_INFINITE,
			ItemID.DIZANAS_QUIVER_INFINITE_BROKEN,
			ItemID.DIZANAS_QUIVER_INFINITE_TROUVER,
			ItemID.SKILLCAPE_MAX_DIZANAS,
			ItemID.SKILLCAPE_MAX_DIZANAS_BROKEN,
			ItemID.SKILLCAPE_MAX_DIZANAS_TROUVER
	);

	@Inject
	private Client client;

	@Inject
	private EnchantedSymbolProtectorConfig config;

	@Inject private ChatMessageManager chatMessageManager;

	/**
	 * Disables unsafe clicks on Enchanted Symbol
	 *
	 * @param event menu click information
	 */
	@Subscribe void onMenuOptionClicked(MenuOptionClicked event)
	{
		if(event.getItemId() != ItemID.MA2_SYMBOL) return;
		if(!event.getMenuOption().equals("Activate")) return;
		int item_limit = (client.isPrayerActive(Prayer.PROTECT_ITEM)) ? 4 : 3;
		int quiver_ammo = 0;
		if(hasQuiver(InventoryID.INV) || hasQuiver(InventoryID.WORN))
		{
			quiver_ammo = client.getVarpValue(VarPlayerID.DIZANAS_QUIVER_TEMP_AMMO_AMOUNT);
		}
		int total_items = tallyItems(InventoryID.INV) + tallyItems(InventoryID.WORN) + quiver_ammo;
		if(total_items > item_limit)
		{
			if(config.displayWarning()) displayWarning(total_items);
			event.consume();
		}
	}

	/**
	 * Checks if container includes a Dizana's Quiver
	 *
	 * @param id InventoryID of the container
	 * @return if quiver is present
	 */
	private boolean hasQuiver(int id)
	{
		ItemContainer items = client.getItemContainer(id);
		if(items == null) return false;
		for(Item item : items.getItems())
		{
			if(QUIVER_IDS.contains(item.getId())) return true;
		}
		return false;
	}

	/**
	 * Counts items including quantities in container
	 *
	 * @param id InventoryID of the container
	 * @return total quantity
	 */
	private int tallyItems(int id)
	{
		ItemContainer items = client.getItemContainer(id);
		if(items == null) return 0;
		int total = 0;
		for(Item item : items.getItems())
		{
			total += item.getQuantity();
		}
		return total;
	}

	/**
	 * Shows symbol activation was canceled
	 */
	private void displayWarning(int total_items)
	{
		chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(total_items + " items present, symbol disabled.").build());
	}

	@Provides
	EnchantedSymbolProtectorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EnchantedSymbolProtectorConfig.class);
	}
}
