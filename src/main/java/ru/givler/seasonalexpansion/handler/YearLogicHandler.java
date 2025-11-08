package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import ru.givler.seasonalexpansion.network.NetworkHandler;
import ru.givler.seasonalexpansion.network.packet.S2CNewYearPacket;
import ru.givler.seasonalexpansion.world.YearWorldData;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class YearLogicHandler {

    public static void register() {
        YearLogicHandler inst = new YearLogicHandler();
        FMLCommonHandler.instance().bus().register(inst);
        MinecraftForge.EVENT_BUS.register(inst);
        System.out.println("[SE] Registered YearLogicHandler (server)");
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (world.isRemote || world.provider.dimensionId != 0) return; // только основной мир

        ISeasonState state = SeasonHelper.getSeasonState(world);
        if (state == null) return;

        Season.SubSeason sub = state.getSubSeason();
        if (sub == null) return;

        YearWorldData data = YearWorldData.get(world);

        // Смена года только один раз за середину зимы
        if (sub == Season.SubSeason.MID_WINTER) {
            if (!data.hasChangedThisWinter()) {
                int newYear = (data.getCurrentYear() + 1) % 12;
                data.setCurrentYear(newYear);
                data.setChangedThisWinter(true);

                NetworkHandler.INSTANCE.sendToAll(new S2CNewYearPacket(newYear));
                System.out.println("[SE] New year started: " + newYear);
            }
        } else {
            data.setChangedThisWinter(false);
        }
    }
}
