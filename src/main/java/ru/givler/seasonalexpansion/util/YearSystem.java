package ru.givler.seasonalexpansion.util;

import net.minecraft.world.World;
import net.minecraft.util.StatCollector;
import ru.givler.seasonalexpansion.world.YearWorldData;

public class YearSystem {

    /** Возвращает индекс текущего года (0–11) */
    public static int getCurrentYearIndex(World world) {
        if (world == null || world.mapStorage == null) return 0;
        YearWorldData data = YearWorldData.get(world);
        return data != null ? data.getCurrentYear() : 0;
    }

    /** Возвращает ключ для текущего года (например "dragon", "wolf" и т.д.) */
    public static String getCurrentYearKey(World world) {
        int index = getCurrentYearIndex(world);
        switch (index) {
            case 0: return "dragon";
            case 1: return "bat";
            case 2: return "wolf";
            case 3: return "snake";
            case 4: return "owl";
            case 5: return "bear";
            case 6: return "raven";
            case 7: return "tiger";
            case 8: return "phoenix";
            case 9: return "fish";
            case 10: return "deer";
            case 11: return "minotaur";
            default: return "unknown";
        }
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
}