package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.world.World;
import ru.givler.seasonalexpansion.network.NetworkHandler;
import ru.givler.seasonalexpansion.network.packet.S2CSeasonAnnouncementPacket;
import ru.givler.seasonalexpansion.world.SeasonAnnouncementWorldData;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

/** Detects the transition on the server and notifies only players online at that moment. */
public final class SeasonAnnouncementServerHandler {
    private SeasonAnnouncementServerHandler() {}

    public static void register() {
        FMLCommonHandler.instance().bus().register(new SeasonAnnouncementServerHandler());
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (event.phase != TickEvent.Phase.END || world.isRemote || world.provider.dimensionId != 0) return;

        ISeasonState state = SeasonHelper.getSeasonState(world);
        if (state == null || state.getSubSeason() == null) return;

        Season currentSeason = state.getSubSeason().getSeason();
        SeasonAnnouncementWorldData data = SeasonAnnouncementWorldData.get(world);
        int previousOrdinal = data.getSeasonOrdinal();

        if (previousOrdinal == currentSeason.ordinal()) return;

        data.setSeasonOrdinal(currentSeason.ordinal());
        if (previousOrdinal >= 0) {
            NetworkHandler.INSTANCE.sendToAll(new S2CSeasonAnnouncementPacket(currentSeason));
        }
    }
}
