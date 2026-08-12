package ru.givler.seasonalexpansion.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public final class TelescopeMessage {
    private TelescopeMessage() {}

    public static void sendCurrentYear(EntityPlayer player, World world) {
        ChatComponentTranslation yearName = new ChatComponentTranslation(
                "year.name." + YearSystem.getCurrentYearKey(world)
        );
        ChatComponentTranslation message = new ChatComponentTranslation(
                "seasonalexpansion.telescope.message", yearName
        );
        message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
        player.addChatMessage(message);
    }
}
