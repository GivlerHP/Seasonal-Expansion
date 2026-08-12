package ru.givler.seasonalexpansion.compat.mbo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import ru.givler.mbo.registry.ArmorRegistry;
import ru.givler.mbo.registry.ItemRegistry;
import ru.givler.mbo.registry.PotionRegistry;
import ru.givler.seasonalexpansion.util.YearSystem;
import ru.givler.seasonalexpansion.config.SeasonAnnouncementConfig;

public final class MboYearEffectHandler {
    private static final int YEAR_BAT = 1;
    private static final int YEAR_OWL = 4;

    private MboYearEffectHandler() {}

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new MboYearEffectHandler());
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.Clone event) {
        if (!event.entityPlayer.worldObj.isRemote && event.wasDeath
                && YearSystem.getCurrentYearIndex(event.entityPlayer.worldObj) == YEAR_BAT) {
            event.entityPlayer.addPotionEffect(new PotionEffect(PotionRegistry.Curse.id, 6000, 0, true));
        }
    }

    @SubscribeEvent
    public void onOwlYearZombieSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.world.isRemote
                || (!SeasonAnnouncementConfig.owlYearZombiesInAllDimensions
                    && event.world.provider.dimensionId != 0)
                || YearSystem.getCurrentYearIndex(event.world) != YEAR_OWL
                || !(event.entityLiving instanceof EntityZombie)) return;

        EntityZombie zombie = (EntityZombie) event.entityLiving;
        if (zombie.getHeldItem() != null || zombie.getEntityData().getBoolean("SeasonalExpansionOwlGear")
                || event.world.rand.nextInt(20) != 0) return;

        zombie.getEntityData().setBoolean("SeasonalExpansionOwlGear", true);
        equip(zombie, event.world.rand.nextInt(6));
    }

    private static void equip(EntityZombie zombie, int set) {
        switch (set) {
            case 0:
                setEquipment(zombie, ItemRegistry.BrokenLongsword, ArmorRegistry.KnightBoots,
                        ArmorRegistry.KnightLegs, ArmorRegistry.KnightChest, ArmorRegistry.KnightHelmet,
                        0.20D, 30.0D);
                break;
            case 1:
                setEquipment(zombie, ItemRegistry.BrokenSword, ArmorRegistry.MercenaryBoots,
                        ArmorRegistry.MercenaryLegs, ArmorRegistry.MercenaryChest, ArmorRegistry.MercenaryHelmet,
                        0.23D, 25.0D);
                break;
            case 2:
                setEquipment(zombie, ItemRegistry.BrokenDagger, ArmorRegistry.WandererBoots,
                        ArmorRegistry.WandererLegs, ArmorRegistry.WandererChest, ArmorRegistry.WandererHelmet,
                        0.35D, 20.0D);
                break;
            case 3:
                setEquipment(zombie, ItemRegistry.BrokenMace, ArmorRegistry.ClericBoots,
                        ArmorRegistry.ClericLegs, ArmorRegistry.ClericChest, ArmorRegistry.ClericHelmet,
                        0.23D, 28.0D);
                break;
            case 4:
                setEquipment(zombie, ItemRegistry.BrokenAxe, ArmorRegistry.PyromancerBoots,
                        ArmorRegistry.PyromancerLegs, ArmorRegistry.PyromancerChest, ArmorRegistry.PyromancerHelmet,
                        0.23D, 24.0D);
                break;
            case 5:
                setEquipment(zombie, ItemRegistry.BrokenRapier, ArmorRegistry.WizardBoots,
                        ArmorRegistry.WizardLegs, ArmorRegistry.WizardChest, ArmorRegistry.WizardHelmet,
                        0.26D, 22.0D);
                break;
            default:
                throw new IllegalArgumentException("Unknown owl-year equipment set: " + set);
        }
    }

    private static void setEquipment(EntityZombie zombie, Item weapon, Item boots, Item legs,
                                     Item chest, Item helmet, double speed, double health) {
        zombie.setCurrentItemOrArmor(0, new ItemStack(weapon));
        zombie.setCurrentItemOrArmor(1, new ItemStack(boots));
        zombie.setCurrentItemOrArmor(2, new ItemStack(legs));
        zombie.setCurrentItemOrArmor(3, new ItemStack(chest));
        zombie.setCurrentItemOrArmor(4, new ItemStack(helmet));
        zombie.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(speed);
        zombie.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(health);
        zombie.setHealth((float) health);

        for (int slot = 0; slot <= 4; slot++) zombie.setEquipmentDropChance(slot, 0.09F);
        if (zombie.getEntityAttribute(SharedMonsterAttributes.attackDamage) != null) {
            zombie.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
        }
    }

    @SubscribeEvent
    public void onPyromancerAttack(LivingHurtEvent event) {
        if (!(event.source.getEntity() instanceof EntityZombie)) return;
        EntityZombie zombie = (EntityZombie) event.source.getEntity();
        ItemStack helmet = zombie.getEquipmentInSlot(4);

        if (helmet != null && helmet.getItem() == ArmorRegistry.PyromancerHelmet
                && zombie.worldObj.rand.nextFloat() < 0.75F && !event.entityLiving.isImmuneToFire()) {
            event.entityLiving.setFire(5);
        }
    }
}
