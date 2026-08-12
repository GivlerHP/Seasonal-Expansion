package ru.givler.seasonalexpansion.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import ru.givler.seasonalexpansion.SeasonalExpansion;
import ru.givler.seasonalexpansion.util.TelescopeMessage;
import sereneseasons.util.inventory.CreativeTabSS;

public class BlockTelescope extends Block {
    public BlockTelescope(Material material, String name) {
        super(material);
        setBlockName(name);
        setLightLevel(0.0F);
        setLightOpacity(0);
        setHardness(1.0F);
        setCreativeTab(CreativeTabSS.instance);
        setResistance(10.0F);
        setHarvestLevel("pick_axe", 1);
        setStepSound(soundTypeAnvil);
        setBlockTextureName(SeasonalExpansion.ID + ":" + name);
        GameRegistry.registerBlock(this, name);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TelescopeMessage.sendCurrentYear(player, world);
        }
        return true;
    }
}
