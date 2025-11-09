package ru.givler.seasonalexpansion.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import ru.givler.seasonalexpansion.client.PoisonRainRenderer;

/**
 * Пакет от сервера к клиенту, сообщающий о начале или окончании ядовитого дождя.
 */
public class S2CPoisonRainPacket implements IMessage {
    private boolean active;

    public S2CPoisonRainPacket() {}

    public S2CPoisonRainPacket(boolean active) {
        this.active = active;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(active);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        active = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<S2CPoisonRainPacket, IMessage> {
        @Override
        public IMessage onMessage(S2CPoisonRainPacket msg, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc != null && mc.theWorld != null) {
                PoisonRainRenderer.setPoisonRainActive(msg.active);
            }
            return null;
        }
    }
}
