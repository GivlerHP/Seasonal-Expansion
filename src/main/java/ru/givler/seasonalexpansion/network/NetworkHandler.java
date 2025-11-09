package ru.givler.seasonalexpansion.network;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.network.NetworkRegistry;
import ru.givler.seasonalexpansion.network.packet.S2CNewYearPacket;
import ru.givler.seasonalexpansion.network.packet.S2CPoisonRainPacket;

public class NetworkHandler {

    public static SimpleNetworkWrapper INSTANCE;

    public static void init() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SeasonalExpansion");
        int id = 0;

        INSTANCE.registerMessage(S2CNewYearPacket.Handler.class, S2CNewYearPacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(S2CPoisonRainPacket.Handler.class, S2CPoisonRainPacket.class, id++, Side.CLIENT);
    }

}
