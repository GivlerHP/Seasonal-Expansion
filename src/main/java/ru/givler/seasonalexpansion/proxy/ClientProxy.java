package ru.givler.seasonalexpansion.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import ru.givler.seasonalexpansion.client.PoisonRainRenderer;
import ru.givler.seasonalexpansion.client.SereneSeasonsTooltipCompat;
import ru.givler.seasonalexpansion.client.render.RenderDireWolf;
import ru.givler.seasonalexpansion.config.SeasonAnnouncementConfig;
import ru.givler.seasonalexpansion.entity.EntityDireWolf;
import ru.givler.seasonalexpansion.handler.YearCycleHandler;
import ru.givler.seasonalexpansion.handler.SeasonAnnouncementHandler;
import ru.givler.seasonalexpansion.registry.BlockRegistry;

import static ru.givler.mbo.proxy.ClientProxy.bindDefaultRender;


public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		if(Loader.isModLoaded("mbo")) {
			bindDefaultRender(BlockRegistry.mdtelescope);
		}
		registerRenderers();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		if (cpw.mods.fml.common.Loader.isModLoaded("sereneseasons")) {
			SereneSeasonsTooltipCompat.register();
			SeasonAnnouncementHandler.register();
			if (SeasonAnnouncementConfig.enableYearCycle) {
				YearCycleHandler.register();
				MinecraftForge.EVENT_BUS.register(new PoisonRainRenderer());
			}
		}
	}

	@Override
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityDireWolf.class, new RenderDireWolf());
	}
}