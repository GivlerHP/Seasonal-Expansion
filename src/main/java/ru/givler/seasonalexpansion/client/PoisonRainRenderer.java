package ru.givler.seasonalexpansion.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.lwjgl.opengl.GL11;

public class PoisonRainRenderer {

    private static boolean poisonRainActive = false;

    public static void setPoisonRainActive(boolean active) {
        poisonRainActive = active;
    }

    public static boolean isPoisonRainActive() {
        return poisonRainActive;
    }

    /** Мягкий зелёный оттенок — лёгкий ядовитый туман */
    @SubscribeEvent
    public void onFogColors(EntityViewRenderEvent.FogColors event) {
        if (!poisonRainActive) return;

        event.red   *= 0.85F;
        event.green *= 1.05F;
        event.blue  *= 0.85F;
    }

    /** Умеренная плотность тумана */
    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (!poisonRainActive) return;

        GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP2);
        GL11.glFogf(GL11.GL_FOG_DENSITY, 0.008F);
        event.setCanceled(true);
    }
}
