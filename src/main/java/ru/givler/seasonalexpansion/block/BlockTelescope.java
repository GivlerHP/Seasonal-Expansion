package ru.givler.seasonalexpansion.block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.givler.seasonalexpansion.SeasonalExpansion;
import ru.givler.seasonalexpansion.util.YearSystem;
import ru.givler.seasonalexpansion.world.YearWorldData;
import sereneseasons.util.inventory.CreativeTabSS;


public class BlockTelescope extends Block {

    public BlockTelescope(Material material, String name) {
        super(material);

        this.setBlockName(name);
        this.setLightLevel(0.0F);
        this.setLightOpacity(0);
        this.setHardness(1.0F);
        this.setCreativeTab(CreativeTabSS.instance);
        this.setResistance(10.0F);
        this.setHarvestLevel("pick_axe", 1);
        this.setStepSound(soundTypeAnvil);
        this.setBlockTextureName(SeasonalExpansion.ID + ":" + name);
        GameRegistry.registerBlock(this, name);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;

        String yearName = YearSystem.getCurrentYearName(world);

        String message = StatCollector.translateToLocalFormatted(
                "seasonalexpansion.telescope.message", yearName
        );
        player.addChatMessage(new ChatComponentText("§6" + message));
        return true;
    }
}
