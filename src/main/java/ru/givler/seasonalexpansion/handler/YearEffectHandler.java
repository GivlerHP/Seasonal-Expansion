package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import ru.givler.mbo.registry.PotionRegistry;
import ru.givler.seasonalexpansion.entity.EntityDireWolf;
import ru.givler.seasonalexpansion.network.NetworkHandler;
import ru.givler.seasonalexpansion.network.packet.S2CPoisonRainPacket;
import ru.givler.seasonalexpansion.world.YearWorldData;

public class YearEffectHandler {

    private static final int YEAR_DRAGON = 0;
    private static final int YEAR_BAT = 1;
    private static final int YEAR_WOLF = 2;
    private static final int YEAR_SNAKE = 3;
    private static final int CHANCE_INTERVAL = 1200;
    private static final int DURATION_TICKS = 1200;


    private static final int PLAYER_CHECK_INTERVAL = 10;
    private int checkTimer = 0;
    private int tickCounter = 0;
    private int poisonRainTicks = 0;
    private boolean poisonRainActive = false;


    public static void register() {
        YearEffectHandler handler = new YearEffectHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
        System.out.println("[SE] Registered YearEffectHandler (Forge + FML)");
    }
    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.world.isRemote) return;

        YearWorldData data = YearWorldData.get(event.world);
        if (data == null || data.getCurrentYear() != YEAR_DRAGON) return;

        if (!(event.entityLiving instanceof EntitySkeleton)) return;
        EntitySkeleton skeleton = (EntitySkeleton) event.entityLiving;

        if (skeleton.getSkeletonType() != 0) return;

        if (event.world.rand.nextInt(20) == 0) { // 1 из 20 = 5%
            skeleton.setSkeletonType(1); // тип 1 = Wither Skeleton
            System.out.println("[SE] Год Дракона: обычный скелет превращён в иссушителя!");
        }
    }

    @SubscribeEvent
    public void onWolfYearSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.world.isRemote) return;

        YearWorldData data = YearWorldData.get(event.world);
        if (data == null || data.getCurrentYear() != YEAR_WOLF) return;

        long time = event.world.getWorldTime() % 24000L;
        if (time < 13200L || time > 23000L) return;

        int light = event.world.getBlockLightValue((int) event.x, (int) event.y, (int) event.z);
        if (light > 7) return;

        if (event.world.rand.nextInt(800) == 0) {

            int count = 2 + event.world.rand.nextInt(2);
            for (int i = 0; i < count; i++) {
                EntityDireWolf wolf = new EntityDireWolf(event.world);
                double dx = event.x + (event.world.rand.nextDouble() - 0.5D) * 8.0D;
                double dz = event.z + (event.world.rand.nextDouble() - 0.5D) * 8.0D;
                wolf.setLocationAndAngles(dx, event.y, dz, event.world.rand.nextFloat() * 360F, 0.0F);
                event.world.spawnEntityInWorld(wolf);
            }
        }
    }


    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) return;

        tickCounter++;
        if (tickCounter >= CHANCE_INTERVAL) {
            tickCounter = 0;

            YearWorldData data = YearWorldData.get(event.world);
            if (data != null && data.getCurrentYear() == YEAR_SNAKE) {

                if (!poisonRainActive && event.world.isRaining() && event.world.rand.nextInt(20) == 0) {
                    poisonRainActive = true;
                    poisonRainTicks = DURATION_TICKS;
                    NetworkHandler.INSTANCE.sendToAll(new S2CPoisonRainPacket(true));
                    System.out.println("[SE] Год Змеи: начался ядовитый дождь!");
                }
            }
        }

        if (poisonRainActive) {
            poisonRainTicks--;

            if (poisonRainTicks <= 0) {
                poisonRainActive = false;
                NetworkHandler.INSTANCE.sendToAll(new S2CPoisonRainPacket(false));
                System.out.println("[SE] Ядовитый дождь закончился (истёк таймер).");
            } else {
                checkTimer++;
                if (checkTimer >= PLAYER_CHECK_INTERVAL) {
                    checkTimer = 0;

                    java.util.List<?> entities = new java.util.ArrayList<>(event.world.loadedEntityList);

                    for (Object obj : entities) {
                        if (!(obj instanceof EntityLivingBase)) continue;
                        EntityLivingBase entity = (EntityLivingBase) obj;

                        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)
                            continue;

                        if (event.world.canBlockSeeTheSky(
                                (int) entity.posX,
                                (int) entity.posY + 1,
                                (int) entity.posZ)) {
                            entity.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 0, true));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.Clone event) {
        if (event.entityPlayer.worldObj.isRemote) return;

        YearWorldData data = YearWorldData.get(event.entityPlayer.worldObj);
        if (data == null || data.getCurrentYear() != YEAR_BAT) return;

        if (event.wasDeath) {
            EntityPlayer player = event.entityPlayer;
            if(Loader.isModLoaded("mbo")) {
                player.addPotionEffect(new PotionEffect(PotionRegistry.Curse.id, 6000, 0, true));
            }else {
                player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 6000, 1, true));
                player.addPotionEffect(new PotionEffect(Potion.weakness.id, 6000, 1, true));
            }
        }
    }
}
