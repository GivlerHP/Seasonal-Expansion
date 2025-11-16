package ru.givler.seasonalexpansion.registry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ru.givler.mbo.block.BlockModels;
import ru.givler.mbo.models.ExternalModelRegistry;
import ru.givler.seasonalexpansion.block.BlockTelescope;
import ru.givler.seasonalexpansion.block.ModelTelescope;


public class BlockRegistry {

    public static Block telescope;
    public static BlockModels mdtelescope;

    @Mod.EventHandler
    public static void preLoad(FMLPreInitializationEvent event) {
        if(Loader.isModLoaded("mbo")) {
        mdtelescope  = new ModelTelescope(Material.iron, "mdtelescope", "telescope", "telescope");
        mdtelescope.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 1.4F, 0.7F);
        }else {
            telescope = new BlockTelescope(Material.iron, "telescope");
        }
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        if(Loader.isModLoaded("mbo")) {
        mdtelescope.register();
        }
    }
}
