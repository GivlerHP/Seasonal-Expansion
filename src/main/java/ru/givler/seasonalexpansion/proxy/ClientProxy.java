package ru.givler.seasonalexpansion.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ru.givler.seasonalexpansion.client.SereneSeasonsTooltipCompat;
import ru.givler.seasonalexpansion.config.SeasonAnnouncementConfig;
import ru.givler.seasonalexpansion.handler.YearCycleHandler;
import ru.givler.seasonalexpansion.handler.SeasonAnnouncementHandler;


public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		if (cpw.mods.fml.common.Loader.isModLoaded("sereneseasons")) {
			SereneSeasonsTooltipCompat.register();
			SeasonAnnouncementHandler.register();
			if (SeasonAnnouncementConfig.enableYearCycle) {
				YearCycleHandler.register();
			}
		}
	}
}