package ru.givler.seasonalexpansion.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import ru.givler.mbo.block.BlockModels;
import ru.givler.seasonalexpansion.util.TelescopeMessage;
import sereneseasons.util.inventory.CreativeTabSS;

public class ModelTelescope extends BlockModels {
    public ModelTelescope(Material material, String name, String texture, String type) {
        super(material, name, texture, type);
        setHarvestLevel("pick_axe", 1);
        setStepSound(soundTypeAnvil);
        setCreativeTab(CreativeTabSS.instance);
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
