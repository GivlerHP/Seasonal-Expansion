package ru.givler.seasonalexpansion.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ru.givler.seasonalexpansion.command.CommandCurrentYear;
import ru.givler.seasonalexpansion.config.SeasonAnnouncementConfig;
import ru.givler.seasonalexpansion.handler.YearEffectHandler;
import ru.givler.seasonalexpansion.handler.YearLogicHandler;
import ru.givler.seasonalexpansion.network.NetworkHandler;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		NetworkHandler.init();
		SeasonAnnouncementConfig.load(event.getModConfigurationDirectory());
	}

	public void init(FMLInitializationEvent event) {
		if (SeasonAnnouncementConfig.enableYearCycle) {
			YearLogicHandler.register();
			YearEffectHandler.register();
		}
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandCurrentYear());

	}
}
