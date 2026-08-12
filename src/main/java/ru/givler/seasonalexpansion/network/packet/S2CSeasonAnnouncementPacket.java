package ru.givler.seasonalexpansion.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import ru.givler.seasonalexpansion.handler.SeasonAnnouncementHandler;
import sereneseasons.api.season.Season;

public class S2CSeasonAnnouncementPacket implements IMessage {
    private int seasonOrdinal;

    public S2CSeasonAnnouncementPacket() {}

    public S2CSeasonAnnouncementPacket(Season season) {
        seasonOrdinal = season.ordinal();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(seasonOrdinal);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        seasonOrdinal = buf.readUnsignedByte();
    }

    public static class Handler implements IMessageHandler<S2CSeasonAnnouncementPacket, IMessage> {
        @Override
        public IMessage onMessage(S2CSeasonAnnouncementPacket message, MessageContext context) {
            Season[] seasons = Season.values();
            if (message.seasonOrdinal < seasons.length) {
                SeasonAnnouncementHandler.showSeasonOverlay(seasons[message.seasonOrdinal]);
            }
            return null;
        }
    }
}
