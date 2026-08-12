package ru.givler.seasonalexpansion.util;

import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.util.StatCollector;
import ru.givler.seasonalexpansion.world.YearWorldData;

public class YearSystem {
    private static final String[] YEAR_KEYS = {
            "dragon", "bat", "wolf", "snake", "owl", "raven",
            "bear", "tiger", "phoenix", "fish", "deer", "minotaur"
    };

    /** Возвращает индекс текущего года (0–11) */
    public static int getCurrentYearIndex(World world) {
        if (world == null || world.mapStorage == null) return 0;
        World dataWorld = getDataWorld(world);
        YearWorldData data = YearWorldData.get(dataWorld);
        return data != null ? data.getCurrentYear() : 0;
    }

    /** Возвращает ключ года по индексу (статический метод для использования без World) */
    public static String getYearKeyByIndex(int index) {
        return index >= 0 && index < YEAR_KEYS.length ? YEAR_KEYS[index] : "unknown";
    }

    /** Возвращает ключ для текущего года (например "dragon", "wolf" и т.д.) */
    public static String getCurrentYearKey(World world) {
        return getYearKeyByIndex(getCurrentYearIndex(world));
    }

    /** Возвращает локализованное имя текущего года */
    public static String getCurrentYearName(World world) {
        String key = getCurrentYearKey(world);
        return StatCollector.translateToLocal("year.name." + key);
    }

    /** Возвращает локализованное описание текущего года */
    public static String getCurrentYearDescription(World world) {
        String key = getCurrentYearKey(world);
        return StatCollector.translateToLocal("year.desc." + key);
    }

    private static World getDataWorld(World world) {
        if (!world.isRemote && world.provider.dimensionId != 0) {
            MinecraftServer server = MinecraftServer.getServer();
            WorldServer overworld = server == null ? null : server.worldServerForDimension(0);
            if (overworld != null) return overworld;
        }
        return world;
    }
}
