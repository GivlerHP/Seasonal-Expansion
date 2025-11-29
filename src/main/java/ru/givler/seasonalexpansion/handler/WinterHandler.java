package ru.givler.seasonalexpansion.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.StatCollector;
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
        ItemStack held = event.entityPlayer.getHeldItem();

        if (held == null) return;

        Season current = SeasonHelper.getSeasonState(animal.worldObj).getSeason();

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
        FMLCommonHandler.instance().bus().register(handler);
    }
}
