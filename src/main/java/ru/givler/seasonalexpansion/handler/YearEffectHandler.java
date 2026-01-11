package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import ru.givler.mbo.registry.ArmorRegistry;
import ru.givler.mbo.registry.ItemRegistry;
import ru.givler.mbo.registry.PotionRegistry;
import ru.givler.seasonalexpansion.entity.EntityDireWolf;
import ru.givler.seasonalexpansion.entity.EntityRaven;
import ru.givler.seasonalexpansion.network.NetworkHandler;
import ru.givler.seasonalexpansion.network.packet.S2CPoisonRainPacket;
import ru.givler.seasonalexpansion.world.YearWorldData;

public class YearEffectHandler {

    private static final int YEAR_DRAGON = 0;
    private static final int YEAR_BAT = 1;
    private static final int YEAR_WOLF = 2;
    private static final int YEAR_SNAKE = 3;
    private static final int YEAR_OWL = 4;
    private static final int YEAR_RAVEN = 5;

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
     //   System.out.println("[SE] Registered YearEffectHandler (Forge + FML)");
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
         //   System.out.println("[SE] Год Дракона: обычный скелет превращён в иссушителя!");
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
    public void onRavenYearSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.world.isRemote) return;

        YearWorldData data = YearWorldData.get(event.world);
        if (data == null || data.getCurrentYear() != YEAR_RAVEN) return;

        long time = event.world.getWorldTime() % 24000L;
        if (time < 0L || time > 13000L) return;

        int light = event.world.getBlockLightValue((int) event.x, (int) event.y, (int) event.z);
        if (light < 8) return;

        if (event.world.rand.nextInt(300) == 0) {

            int count = 2 + event.world.rand.nextInt(2);
            for (int i = 0; i < count; i++) {
                EntityRaven raven = new EntityRaven(event.world);
                double dx = event.x + (event.world.rand.nextDouble() - 0.5D) * 8.0D;
                double dz = event.z + (event.world.rand.nextDouble() - 0.5D) * 8.0D;
                raven.setLocationAndAngles(dx, event.y, dz, event.world.rand.nextFloat() * 360F, 0.0F);
                event.world.spawnEntityInWorld(raven);
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) return;

        if (event.world.provider.dimensionId != 0)
            return;

        tickCounter++;
        if (tickCounter >= CHANCE_INTERVAL) {
            tickCounter = 0;

            YearWorldData data = YearWorldData.get(event.world);
            if (data != null && data.getCurrentYear() == YEAR_SNAKE) {

                if (!poisonRainActive && event.world.isRaining() && event.world.rand.nextInt(20) == 0) {
                    poisonRainActive = true;
                    poisonRainTicks = DURATION_TICKS;
                    NetworkHandler.INSTANCE.sendToAll(new S2CPoisonRainPacket(true));
                }
            }
        }

        if (poisonRainActive) {
            poisonRainTicks--;

            if (poisonRainTicks <= 0) {
                poisonRainActive = false;
                NetworkHandler.INSTANCE.sendToAll(new S2CPoisonRainPacket(false));
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
                            entity.addPotionEffect(new PotionEffect(Potion.hunger.id, 200, 2, true));
                            entity.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0, true));
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

    @SubscribeEvent
    public void onOwlYearZombieSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.world.isRemote) return;

        YearWorldData data = YearWorldData.get(event.world);
        if (data == null || data.getCurrentYear() != YEAR_OWL) return;

        if (!(event.entityLiving instanceof EntityZombie)) return;
        EntityZombie zombie = (EntityZombie) event.entityLiving;

        if (zombie.getEntityData().hasKey("giveMFWeapon")) return;

        if (zombie.getHeldItem() != null) return;

        if (event.world.rand.nextInt(20) == 0) {
            if (Loader.isModLoaded("mbo")) {
                zombie.getEntityData().setBoolean("giveMFWeapon", true);

                int armorSet = event.world.rand.nextInt(6);

                switch (armorSet) {
                    case 0: // Рыцарь - тяжёлая броня, медленный
                        zombie.setCurrentItemOrArmor(0, new ItemStack(ItemRegistry.BrokenLongsword));
                        zombie.setCurrentItemOrArmor(1, new ItemStack(ArmorRegistry.KnightBoots));
                        zombie.setCurrentItemOrArmor(2, new ItemStack(ArmorRegistry.KnightLegs));
                        zombie.setCurrentItemOrArmor(3, new ItemStack(ArmorRegistry.KnightChest));
                        zombie.setCurrentItemOrArmor(4, new ItemStack(ArmorRegistry.KnightHelmet));
                        zombie.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2F);
                        zombie.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
                        zombie.setHealth(30.0F);
                        break;

                    case 1: // Наёмник - средняя броня, стандартная скорость
                        zombie.setCurrentItemOrArmor(0, new ItemStack(ItemRegistry.BrokenSword));
                        zombie.setCurrentItemOrArmor(1, new ItemStack(ArmorRegistry.MercenaryBoots));
                        zombie.setCurrentItemOrArmor(2, new ItemStack(ArmorRegistry.MercenaryLegs));
                        zombie.setCurrentItemOrArmor(3, new ItemStack(ArmorRegistry.MercenaryChest));
                        zombie.setCurrentItemOrArmor(4, new ItemStack(ArmorRegistry.MercenaryHelmet));
                        zombie.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23F);
                        zombie.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0D);
                        zombie.setHealth(25.0F);
                        break;

                    case 2: // Странник - лёгкая броня, быстрый
                        zombie.setCurrentItemOrArmor(0, new ItemStack(ItemRegistry.BrokenDagger));
                        zombie.setCurrentItemOrArmor(1, new ItemStack(ArmorRegistry.WandererBoots));
                        zombie.setCurrentItemOrArmor(2, new ItemStack(ArmorRegistry.WandererLegs));
                        zombie.setCurrentItemOrArmor(3, new ItemStack(ArmorRegistry.WandererChest));
                        zombie.setCurrentItemOrArmor(4, new ItemStack(ArmorRegistry.WandererHelmet));
                        zombie.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35F);
                        zombie.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
                        zombie.setHealth(20.0F);
                        break;

                    case 3: // Клирик - средняя защита, больше здоровья
                        zombie.setCurrentItemOrArmor(0, new ItemStack(ItemRegistry.BrokenMace));
                        zombie.setCurrentItemOrArmor(1, new ItemStack(ArmorRegistry.ClericBoots));
                        zombie.setCurrentItemOrArmor(2, new ItemStack(ArmorRegistry.ClericLegs));
                        zombie.setCurrentItemOrArmor(3, new ItemStack(ArmorRegistry.ClericChest));
                        zombie.setCurrentItemOrArmor(4, new ItemStack(ArmorRegistry.ClericHelmet));
                        zombie.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23F);
                        zombie.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(28.0D);
                        zombie.setHealth(28.0F);
                        break;

                    case 4: // Пиромант - средняя броня, поджигает врагов
                        zombie.setCurrentItemOrArmor(0, new ItemStack(ItemRegistry.BrokenAxe));
                        zombie.setCurrentItemOrArmor(1, new ItemStack(ArmorRegistry.PyromancerBoots));
                        zombie.setCurrentItemOrArmor(2, new ItemStack(ArmorRegistry.PyromancerLegs));
                        zombie.setCurrentItemOrArmor(3, new ItemStack(ArmorRegistry.PyromancerChest));
                        zombie.setCurrentItemOrArmor(4, new ItemStack(ArmorRegistry.PyromancerHelmet));
                        zombie.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23F);
                        zombie.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(24.0D);
                        zombie.setHealth(24.0F);
                        break;

                    case 5: // Маг - лёгкая броня, средняя скорость
                        zombie.setCurrentItemOrArmor(0, new ItemStack(ItemRegistry.BrokenRapier));
                        zombie.setCurrentItemOrArmor(1, new ItemStack(ArmorRegistry.WizardBoots));
                        zombie.setCurrentItemOrArmor(2, new ItemStack(ArmorRegistry.WizardLegs));
                        zombie.setCurrentItemOrArmor(3, new ItemStack(ArmorRegistry.WizardChest));
                        zombie.setCurrentItemOrArmor(4, new ItemStack(ArmorRegistry.WizardHelmet));
                        zombie.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.26F);
                        zombie.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(22.0D);
                        zombie.setHealth(22.0F);
                        break;
                }

                zombie.setEquipmentDropChance(0, 0.09F);
                zombie.setEquipmentDropChance(1, 0.09F);
                zombie.setEquipmentDropChance(2, 0.09F);
                zombie.setEquipmentDropChance(3, 0.09F);
                zombie.setEquipmentDropChance(4, 0.09F);
                if (zombie.getEntityAttribute(SharedMonsterAttributes.attackDamage) != null) {
                    zombie.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPyromancerAttack(LivingHurtEvent event) {
        if (event.source.getEntity() instanceof EntityZombie) {
            EntityZombie zombie = (EntityZombie) event.source.getEntity();

            if (zombie.getEquipmentInSlot(4) != null &&
                    zombie.getEquipmentInSlot(4).getItem() == ArmorRegistry.PyromancerHelmet) {

                if (zombie.worldObj.rand.nextFloat() < 0.75F) {
                    if (event.entityLiving != null && !event.entityLiving.isImmuneToFire()) {
                        event.entityLiving.setFire(5);
                    }
                }
            }
        }
    }
}
