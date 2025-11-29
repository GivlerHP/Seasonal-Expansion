package ru.givler.seasonalexpansion.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.givler.mbo.block.BlockModels;
import ru.givler.seasonalexpansion.util.YearSystem;
import sereneseasons.util.inventory.CreativeTabSS;

public class ModelTelescope extends BlockModels {
    public ModelTelescope(Material material, String name, String texture, String type) {
        super(material, name, texture, type);
        this.setHarvestLevel("pick_axe", 1);
        this.setStepSound(soundTypeAnvil);
        this.setCreativeTab(CreativeTabSS.instance);
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
