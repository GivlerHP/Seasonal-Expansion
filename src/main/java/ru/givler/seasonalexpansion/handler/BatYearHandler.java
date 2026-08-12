package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.PlayerEvent;
import ru.givler.seasonalexpansion.util.YearSystem;

final class BatYearHandler {
    private static final int YEAR_BAT = 1;

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.Clone event) {
        if (event.entityPlayer.worldObj.isRemote || !event.wasDeath
                || YearSystem.getCurrentYearIndex(event.entityPlayer.worldObj) != YEAR_BAT) return;

        if (!Loader.isModLoaded("mbo")) {
            event.entityPlayer.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 6000, 1, true));
            event.entityPlayer.addPotionEffect(new PotionEffect(Potion.weakness.id, 6000, 1, true));
        }
    }
}
