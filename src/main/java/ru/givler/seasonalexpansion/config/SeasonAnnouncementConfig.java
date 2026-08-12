package ru.givler.seasonalexpansion.config;


import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class SeasonAnnouncementConfig {

    public static boolean enableAnnouncements = true;
    public static int displayDuration = 200;
    public static double textScale = 3.5;
    public static boolean enableYearCycle = true;
    public static boolean owlYearZombiesInAllDimensions = true;

    public static void load(File configDir) {
        try {
            File cfgFile = new File(configDir, "SeasonalExpansion/season_announcement.cfg");
            Configuration cfg = new Configuration(cfgFile);
            cfg.load();

            enableAnnouncements = cfg.getBoolean(
                    "enableSeasonAnnouncements",
                    "general",
                    true,
                    "If false — season change messages will not be shown."
            );

            displayDuration = cfg.getInt(
                    "displayDuration",
                    "general",
                    360,
                    40,
                    1000,
                    "Duration of the message display in ticks (20 ticks = 1 second)."
            );

            textScale = cfg.getFloat(
                    "textScale",
                    "general",
                    3.5f,
                    1.0f,
                    6.0f,
                    "Text scale (1.0 = normal, 3.5 = large)."
            );

            enableYearCycle = cfg.getBoolean(
                    "enableYearCycle",
                    "general",
                    true,
                    "If false — disables the year cycle system (no zodiac years or messages)."
            );

            owlYearZombiesInAllDimensions = cfg.getBoolean(
                    "owlYearZombiesInAllDimensions",
                    "yearEffects",
                    true,
                    "If true, equipped Owl-year zombies can spawn in any dimension. " +
                            "If false, they spawn only in the Overworld."
            );

            if (cfg.hasChanged()) cfg.save();
        } catch (Exception e) {
            System.err.println("[SE] Ошибка загрузки season_announcement.cfg: " + e);
        }
    }
}
