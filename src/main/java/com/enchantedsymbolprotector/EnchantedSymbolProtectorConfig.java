package com.enchantedsymbolprotector;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(EnchantedSymbolProtectorConfig.GROUP)
public interface EnchantedSymbolProtectorConfig extends Config
{
	String GROUP = "enchantedsymbolprotector";
	@ConfigItem(
		position = 0,
		keyName = "displayWarning",
		name = "Display Warning",
		description = "Warn in game chat when symbol cannot be used."
	)
	default boolean displayWarning()
	{
		return true;
	}

}
