package ru.givler.seasonalexpansion.registry;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import ru.givler.seasonalexpansion.SeasonalExpansion;
import ru.givler.seasonalexpansion.entity.EntityDireWolf;

public class EntityMobRegistry {

    private static int entityID = 0;

    public static void registerEntities() {
        // === Люто-волк ===
        registerEntity(EntityDireWolf.class, "DireWolf", 0x222222, 0xAA0000);
    }

    private static void registerEntity(Class<? extends Entity> entityClass, String name, int eggPrimary, int eggSecondary) {
        int globalId = EntityRegistry.findGlobalUniqueEntityId();
        int modId = entityID++;
        EntityRegistry.registerGlobalEntityID(entityClass, name, globalId, eggPrimary, eggSecondary);
        EntityRegistry.registerModEntity(entityClass, name, modId, SeasonalExpansion.instance, 64, 3, true
        );
    }
}
