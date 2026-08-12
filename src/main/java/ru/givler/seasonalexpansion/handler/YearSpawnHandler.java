package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import ru.givler.seasonalexpansion.entity.EntityDireWolf;
import ru.givler.seasonalexpansion.entity.EntityRaven;
import ru.givler.seasonalexpansion.util.YearSystem;

final class YearSpawnHandler {
    private static final int YEAR_DRAGON = 0;
    private static final int YEAR_WOLF = 2;
    private static final int YEAR_RAVEN = 5;

    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.world.isRemote || event.world.provider.dimensionId != 0) return;

        switch (YearSystem.getCurrentYearIndex(event.world)) {
            case YEAR_DRAGON:
                transformSkeleton(event);
                break;
            case YEAR_WOLF:
                spawnDireWolves(event);
                break;
            case YEAR_RAVEN:
                spawnRavens(event);
                break;
            default:
                break;
        }
    }

    private static void transformSkeleton(LivingSpawnEvent.CheckSpawn event) {
        if (event.entityLiving instanceof EntitySkeleton) {
            EntitySkeleton skeleton = (EntitySkeleton) event.entityLiving;
            if (skeleton.getSkeletonType() == 0 && event.world.rand.nextInt(20) == 0) {
                skeleton.setSkeletonType(1);
            }
        }
    }

    private static void spawnDireWolves(LivingSpawnEvent.CheckSpawn event) {
        long time = event.world.getWorldTime() % 24000L;
        if (time < 13200L || time > 23000L || getLight(event) > 7 || event.world.rand.nextInt(800) != 0) return;

        int count = 2 + event.world.rand.nextInt(2);
        for (int i = 0; i < count; i++) {
            EntityDireWolf wolf = new EntityDireWolf(event.world);
            positionNearSpawn(wolf, event);
            event.world.spawnEntityInWorld(wolf);
        }
    }

    private static void spawnRavens(LivingSpawnEvent.CheckSpawn event) {
        long time = event.world.getWorldTime() % 24000L;
        if (time > 13000L || getLight(event) < 8 || event.world.rand.nextInt(300) != 0) return;

        int count = 2 + event.world.rand.nextInt(2);
        for (int i = 0; i < count; i++) {
            EntityRaven raven = new EntityRaven(event.world);
            positionNearSpawn(raven, event);
            event.world.spawnEntityInWorld(raven);
        }
    }

    private static int getLight(LivingSpawnEvent.CheckSpawn event) {
        return event.world.getBlockLightValue((int) event.x, (int) event.y, (int) event.z);
    }

    private static void positionNearSpawn(net.minecraft.entity.Entity entity, LivingSpawnEvent.CheckSpawn event) {
        double x = event.x + (event.world.rand.nextDouble() - 0.5D) * 8.0D;
        double z = event.z + (event.world.rand.nextDouble() - 0.5D) * 8.0D;
        entity.setLocationAndAngles(x, event.y, z, event.world.rand.nextFloat() * 360.0F, 0.0F);
    }
}
