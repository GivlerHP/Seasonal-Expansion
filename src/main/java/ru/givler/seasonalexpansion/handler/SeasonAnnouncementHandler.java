package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import ru.givler.seasonalexpansion.config.SeasonAnnouncementConfig;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import java.io.File;

@SideOnly(Side.CLIENT)
public class SeasonAnnouncementHandler {

    private static Season lastSeason = null;
    private static int tickCounter = 0;
    private static boolean justJoined = false;

    private static int fadeTicks = 0;
    private static String displayText = "";
    private static Season displaySeason = null;

    private static final boolean SHOW_OVERLAY = true;

    public static void register() {
        SeasonAnnouncementConfig.load(new File(Minecraft.getMinecraft().mcDataDir, "config"));
        if (!SeasonAnnouncementConfig.enableAnnouncements) {
            System.out.println("[SE] Season announcements disabled in config");
            return;
        }

        SeasonAnnouncementHandler inst = new SeasonAnnouncementHandler();
        FMLCommonHandler.instance().bus().register(inst);
        MinecraftForge.EVENT_BUS.register(inst);
        System.out.println("[SE] Registered SeasonAnnouncementHandler");
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote) {
            lastSeason = null;
            tickCounter = 0;
            fadeTicks = 0;
            displayText = "";
            displaySeason = null;
            justJoined = true;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) return;

        tickCounter++;
        if (tickCounter < 200) return;
        tickCounter = 0;

        ISeasonState state = SeasonHelper.getSeasonState(mc.theWorld);
        if (state == null) return;

        Season current = state.getSeason();
        if (current == null) return;

        if (justJoined) {
            lastSeason = current;
            justJoined = false;
            return;
        }

        if (lastSeason != current) {
            lastSeason = current;
            showSeasonOverlay(current);
        }
    }

    private void showSeasonOverlay(Season season) {
        Minecraft mc = Minecraft.getMinecraft();

        String key = "season.message." + season.name().toLowerCase();
        String text = StatCollector.translateToLocal(key);
        if (text == null || text.isEmpty() || text.equals(key)) {
            switch (season) {
                case SPRING: text = "Весна вступает в свои права"; break;
                case SUMMER: text = "Жаркое лето наступило"; break;
                case AUTUMN: text = "Осень укрывает землю"; break;
                case WINTER: text = "Холодные ветра принесли зиму"; break;
                default: text = "Смена сезона"; break;
            }
        }

        displayText = text;
        displaySeason = season;
        fadeTicks = SeasonAnnouncementConfig.displayDuration;
    }

    private static int getSeasonRGB(Season s) {
        switch (s) {
            case SPRING: return 0x55FF55;
            case SUMMER: return 0xFFFF55;
            case AUTUMN: return 0xFFAA00;
            case WINTER: return 0x55FFFF;
            default: return 0xFFFFFF;
        }
    }

    /** Рисуем на экране наступление нового сезона*/
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (!SHOW_OVERLAY || fadeTicks <= 0 || event.type != RenderGameOverlayEvent.ElementType.ALL)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int w = sr.getScaledWidth();
        int h = sr.getScaledHeight();

        int maxLife = SeasonAnnouncementConfig.displayDuration;
        int life = fadeTicks;
        float alphaF;

        if (life > maxLife * 0.8f) // первые 20% времени — fade-in
            alphaF = 1.0f - (life - maxLife * 0.8f) / (maxLife * 0.2f);
        else if (life < maxLife * 0.2f) // последние 20% времени — fade-out
            alphaF = life / (maxLife * 0.2f);
        else
            alphaF = 1.0f;

        int alpha = (int)(255 * Math.max(0, Math.min(alphaF, 1)));
        int rgb = getSeasonRGB(displaySeason);
        int argb = ((alpha & 0xFF) << 24) | (rgb & 0xFFFFFF);

        String text = displayText;
        if (text == null || text.isEmpty()) return;

        String[] lines = text.split("\\\\n");
        double scale = SeasonAnnouncementConfig.textScale;
        int x = w / 2;
        int y = h / 3;

        org.lwjgl.opengl.GL11.glPushMatrix();
        org.lwjgl.opengl.GL11.glScaled(scale, scale, scale);

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            float scaledX = (float)(x / scale - mc.fontRenderer.getStringWidth(line) / 2);
            float scaledY = (float)(y / scale + i * 20);

            mc.fontRenderer.drawString(line, (int)(scaledX + 1), (int)(scaledY + 1), 0xAA000000);
            mc.fontRenderer.drawString(line, (int)scaledX, (int)scaledY, argb);
        }

        org.lwjgl.opengl.GL11.glPopMatrix();
        fadeTicks--;
    }

}
