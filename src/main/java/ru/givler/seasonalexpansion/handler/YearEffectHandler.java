package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.MinecraftForge;

/** Registers the independent handlers responsible for year effects. */
public final class YearEffectHandler {
    private YearEffectHandler() {}

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new YearSpawnHandler());
        MinecraftForge.EVENT_BUS.register(new BatYearHandler());
        FMLCommonHandler.instance().bus().register(new PoisonRainHandler());

        if (Loader.isModLoaded("mbo")) {
            registerMboCompat();
        }
    }

    private static void registerMboCompat() {
        try {
            Class.forName("ru.givler.seasonalexpansion.compat.mbo.MboYearEffectHandler")
                    .getMethod("register")
                    .invoke(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to initialize MBO year effects", e);
        }
    }
}
