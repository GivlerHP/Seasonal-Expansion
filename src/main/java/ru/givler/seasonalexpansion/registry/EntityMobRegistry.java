package ru.givler.seasonalexpansion.registry;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import ru.givler.seasonalexpansion.SeasonalExpansion;
import ru.givler.seasonalexpansion.entity.EntityDireWolf;
import ru.givler.seasonalexpansion.entity.EntityRaven;

public class EntityMobRegistry {

    private static int entityID = 0;

    public static void registerEntities() {
        registerEntity(EntityDireWolf.class, "DireWolf", 0x222222, 0xAA0000);
        registerEntity(EntityRaven.class, "Raven", 0x000000, 0x222222);
    }

    private static void registerEntity(Class<? extends Entity> entityClass, String name, int eggPrimary, int eggSecondary) {
        int globalId = EntityRegistry.findGlobalUniqueEntityId();
        int modId = entityID++;
        EntityRegistry.registerGlobalEntityID(entityClass, name, globalId, eggPrimary, eggSecondary);
        EntityRegistry.registerModEntity(entityClass, name, modId, SeasonalExpansion.instance, 64, 3, true
        );
    }
}
