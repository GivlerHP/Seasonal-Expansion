package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class YearCycleHandler {

    private static int fadeTicks = 0;
    private static String displayTitle = "";
    private static String displaySubtitle = "";
    private static int displayColor = 0xFFFFFF;

    private static final int DISPLAY_DURATION = 200;
    private static final double SCALE_TITLE = 3.5;
    private static final double SCALE_SUBTITLE = 1.8;

    /** Цвета годов */
    private static final int[] YEAR_COLORS = {
            0xFF6600, 0xAAAAAA, 0x88AAFF, 0x66FF66,
            0x55FF55, 0xCC9966, 0x4444AA, 0xFFAA00,
            0xFF3300, 0x00CCCC, 0xBBBBBB, 0x99FFFF
    };

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new YearCycleHandler());
        System.out.println("[SE] Registered YearCycleHandler (client)");
    }

    /** Вызывается из пакета при наступлении нового года */
    public static void showYearOverlay(int yearIndex) {
        String key = getYearKey(yearIndex);
        String title = StatCollector.translateToLocal("year.name." + key);
        String desc = StatCollector.translateToLocal("year.desc." + key);

        displayTitle = StatCollector.translateToLocalFormatted("year.announcement", title);
        displaySubtitle = desc;
        displayColor = YEAR_COLORS[yearIndex];
        fadeTicks = DISPLAY_DURATION;
    }

    private static String getYearKey(int index) {
        switch (index) {
            case 0: return "dragon";
            case 1: return "wolf";
            case 2: return "owl";
            case 3: return "snake";
            case 4: return "deer";
            case 5: return "bear";
            case 6: return "raven";
            case 7: return "tiger";
            case 8: return "phoenix";
            case 9: return "fish";
            case 10: return "stone";
            case 11: return "wind";
            default: return "unknown";
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (fadeTicks <= 0 || event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int w = sr.getScaledWidth();
        int h = sr.getScaledHeight();

        int life = fadeTicks;
        int alpha = 255;
        if (life > DISPLAY_DURATION - 20)
            alpha = (int)(255 * ((DISPLAY_DURATION - life) / 20.0f));
        else if (life < 20)
            alpha = (int)(255 * (life / 20.0f));

        int argb = ((alpha & 0xFF) << 24) | (displayColor & 0xFFFFFF);

        GL11.glPushMatrix();

        // Заголовок
        GL11.glScaled(SCALE_TITLE, SCALE_TITLE, SCALE_TITLE);
        float xTitle = (float)(w / SCALE_TITLE / 2 - mc.fontRenderer.getStringWidth(displayTitle) / 2);
        float yTitle = (float)(h / SCALE_TITLE / 3);
        mc.fontRenderer.drawString(displayTitle, (int)(xTitle + 1), (int)(yTitle + 1), 0xAA000000);
        mc.fontRenderer.drawString(displayTitle, (int)xTitle, (int)yTitle, argb);

        GL11.glPopMatrix();
        GL11.glPushMatrix();

        // Подзаголовок
        GL11.glScaled(SCALE_SUBTITLE, SCALE_SUBTITLE, SCALE_SUBTITLE);
        float xSub = (float)(w / SCALE_SUBTITLE / 2 - mc.fontRenderer.getStringWidth(displaySubtitle) / 2);
        float ySub = (float)(h / SCALE_SUBTITLE / 3 + 25);
        int subAlpha = (int)(alpha * 0.9);
        int subArgb = ((subAlpha & 0xFF) << 24) | 0xFFFFFF;
        mc.fontRenderer.drawString(displaySubtitle, (int)(xSub + 1), (int)(ySub + 1), 0xAA000000);
        mc.fontRenderer.drawString(displaySubtitle, (int)xSub, (int)ySub, subArgb);

        GL11.glPopMatrix();
        fadeTicks--;
    }
}
