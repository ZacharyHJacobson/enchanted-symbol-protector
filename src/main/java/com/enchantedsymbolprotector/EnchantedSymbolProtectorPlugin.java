package com.enchantedsymbolprotector;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
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

	/**
	 * Disables unsafe clicks on Enchanted Symbol
	 *
	 * @param event menu click information
	 */
	@Subscribe void onMenuOptionClicked(MenuOptionClicked event)
	{
		log.debug("1");
		if(event.getItemId() != ItemID.MA2_SYMBOL) return;
		log.debug("2");
		if(!event.getMenuOption().equals("Activate")) return;
		//check if too many items are present
		int item_limit = 3;
		if(client.isPrayerActive(Prayer.PROTECT_ITEM)) item_limit++;
		ItemContainer inventory = client.getItemContainer(InventoryID.INV);
		log.debug("3");
		if(inventory == null) return;
		ItemContainer worn = client.getItemContainer(InventoryID.WORN);
		log.debug("4");
		int worn_count = (worn == null) ? 0 : worn.count();
		log.debug("5");
		if(inventory.count() + worn_count <= item_limit) return;
		log.debug("6");
		event.consume();
	}
}
