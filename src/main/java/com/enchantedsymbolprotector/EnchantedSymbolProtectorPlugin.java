package com.enchantedsymbolprotector;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.Notifier;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Enchanted Symbol Protector"
)
public class EnchantedSymbolProtectorPlugin extends Plugin
{
	@Inject
	private Client client;

	private final ChatMessageManager chatMessageManager = null;

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
		int quiver_ammo = client.getVarpValue(VarPlayerID.DIZANAS_QUIVER_TEMP_AMMO_AMOUNT);
		if(tallyItems(InventoryID.INV) + tallyItems(InventoryID.WORN) + quiver_ammo > item_limit)
		{
			displayWarning();
			event.consume();
		}
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
	private void displayWarning()
	{
		chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage("Too many items!").build());
	}
}
