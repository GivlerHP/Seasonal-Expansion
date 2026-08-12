package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import net.minecraft.util.ChatComponentTranslation;

public class WinterHandler {

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (!(event.target instanceof EntityAnimal)) return;

        EntityAnimal animal = (EntityAnimal) event.target;
        if (animal.worldObj.isRemote) return;
        ItemStack held = event.entityPlayer.getHeldItem();

        if (held == null) return;

        sereneseasons.api.season.ISeasonState state = SeasonHelper.getSeasonState(animal.worldObj);
        if (state == null) return;
        Season current = state.getSeason();

        if (current == Season.WINTER && animal.isBreedingItem(held)) {
            event.setCanceled(true);
            event.entityPlayer.addChatMessage(
                    new ChatComponentTranslation("sereneexpansion.breeding.winter")
            );
        }
    }

    static public void register() {
        WinterHandler handler = new WinterHandler();
        MinecraftForge.EVENT_BUS.register(handler);
    }
}
