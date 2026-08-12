package ru.givler.seasonalexpansion.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import ru.givler.seasonalexpansion.util.YearSystem;

public class CommandCurrentYear extends CommandBase {
    @Override
    public String getCommandName() {
        return "currentyear";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.currentyear.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        World world = sender.getEntityWorld();
        String yearKey = YearSystem.getCurrentYearKey(world);
        ChatComponentTranslation name = new ChatComponentTranslation("year.name." + yearKey);
        ChatComponentTranslation description = new ChatComponentTranslation("year.desc." + yearKey);
        ChatComponentTranslation message = new ChatComponentTranslation(
                "commands.currentyear.message", name, description
        );
        message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
        sender.addChatMessage(message);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
