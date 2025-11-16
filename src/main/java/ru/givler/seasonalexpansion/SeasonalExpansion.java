package ru.givler.seasonalexpansion;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ru.givler.seasonalexpansion.registry.EntityMobRegistry;
import ru.givler.seasonalexpansion.proxy.CommonProxy;

@Mod(modid= SeasonalExpansion.ID, name= SeasonalExpansion.NAME, version= SeasonalExpansion.VERSION, dependencies = "required-after:sereneseasons" )
public class SeasonalExpansion {
	public static final String ID = "seasonalexpansion";
	public static final String NAME = "SeasonalExpansion";
	public static final String VERSION = "@VERSION@";
	
	@Instance(ID)
	public static SeasonalExpansion instance;
	
	@SidedProxy(clientSide="ru.givler.seasonalexpansion.proxy.ClientProxy", serverSide="ru.givler.seasonalexpansion.proxy.CommonProxy")
	public static CommonProxy proxy;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		EntityMobRegistry.registerEntities();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}
}
