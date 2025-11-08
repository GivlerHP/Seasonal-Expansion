package ru.givler.seasonalexpansion.network;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.network.NetworkRegistry;
import ru.givler.seasonalexpansion.network.packet.S2CNewYearPacket;

public class NetworkHandler {

    public static final SimpleNetworkWrapper INSTANCE =
            NetworkRegistry.INSTANCE.newSimpleChannel("SeasonalExpansion");

    public static void init() {
        INSTANCE.registerMessage(S2CNewYearPacket.Handler.class, S2CNewYearPacket.class, 0, Side.CLIENT);
    }
}
