package com.example;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EnchantedSymbolProtectorTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EnchantedSymbolProtectorPlugin.class);
		RuneLite.main(args);
	}
}