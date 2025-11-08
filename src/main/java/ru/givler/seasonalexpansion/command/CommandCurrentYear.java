package ru.givler.seasonalexpansion.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
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
        return "/currentyear — показывает текущий год Ариамиса";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        World world = sender.getEntityWorld();
        String yearName = YearSystem.getCurrentYearName(world);
        String yearDesc = YearSystem.getCurrentYearDescription(world);

        sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GOLD + "Сейчас " +
                        EnumChatFormatting.YELLOW + yearName + EnumChatFormatting.GRAY +
                        " — " + yearDesc
        ));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
