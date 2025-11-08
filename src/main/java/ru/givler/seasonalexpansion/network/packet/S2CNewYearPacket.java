package ru.givler.seasonalexpansion.network.packet;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import ru.givler.seasonalexpansion.handler.YearCycleHandler;

public class S2CNewYearPacket implements IMessage {
    private int yearIndex;

    public S2CNewYearPacket() {}
    public S2CNewYearPacket(int yearIndex) {
        this.yearIndex = yearIndex;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(yearIndex);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        yearIndex = buf.readInt();
    }

    public static class Handler implements IMessageHandler<S2CNewYearPacket, IMessage> {
        @Override
        public IMessage onMessage(S2CNewYearPacket msg, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            YearCycleHandler.showYearOverlay(msg.yearIndex);
            return null;
        }
    }

}
