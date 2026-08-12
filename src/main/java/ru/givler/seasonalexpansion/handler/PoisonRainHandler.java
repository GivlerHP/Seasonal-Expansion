package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import ru.givler.seasonalexpansion.network.NetworkHandler;
import ru.givler.seasonalexpansion.network.packet.S2CPoisonRainPacket;
import ru.givler.seasonalexpansion.util.YearSystem;
import ru.givler.seasonalexpansion.world.PoisonRainWorldData;

final class PoisonRainHandler {
    private static final int YEAR_SNAKE = 3;
    private static final int CHANCE_INTERVAL = 1200;
    private static final int DURATION_TICKS = 1200;
    private static final int EFFECT_INTERVAL = 10;

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (event.phase != TickEvent.Phase.END || world.isRemote || world.provider.dimensionId != 0) return;

        PoisonRainWorldData data = PoisonRainWorldData.get(world);
        if (!data.isActive()) {
            if (data.tickChanceTimer(CHANCE_INTERVAL)
                    && YearSystem.getCurrentYearIndex(world) == YEAR_SNAKE
                    && world.isRaining() && world.rand.nextInt(20) == 0) {
                data.start(DURATION_TICKS);
                sendToOverworld(true);
            }
            return;
        }

        data.tickRain();
        if (data.getRemainingTicks() <= 0 || YearSystem.getCurrentYearIndex(world) != YEAR_SNAKE) {
            data.stop();
            sendToOverworld(false);
        } else if (data.getEffectTimer() >= EFFECT_INTERVAL) {
            data.resetEffectTimer();
            applyEffects(world);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        syncPlayer(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncPlayer(event.player);
    }

    private static void syncPlayer(EntityPlayer playerEntity) {
        if (!(playerEntity instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) playerEntity;
        boolean active = player.dimension == 0 && PoisonRainWorldData.get(player.worldObj).isActive();
        NetworkHandler.INSTANCE.sendTo(new S2CPoisonRainPacket(active), player);
    }

    private static void applyEffects(World world) {
        for (Object object : world.loadedEntityList) {
            if (!(object instanceof EntityLivingBase)) continue;
            EntityLivingBase entity = (EntityLivingBase) object;
            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) continue;

            if (world.canBlockSeeTheSky((int) entity.posX, (int) entity.posY + 1, (int) entity.posZ)) {
                entity.addPotionEffect(new PotionEffect(Potion.hunger.id, 200, 2, true));
                entity.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0, true));
            }
        }
    }

    private static void sendToOverworld(boolean active) {
        NetworkHandler.INSTANCE.sendToDimension(new S2CPoisonRainPacket(active), 0);
    }
}
