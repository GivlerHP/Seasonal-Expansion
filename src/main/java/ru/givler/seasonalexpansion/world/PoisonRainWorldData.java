package ru.givler.seasonalexpansion.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public final class PoisonRainWorldData extends WorldSavedData {
    private static final String DATA_NAME = "PoisonRainData";
    private boolean active;
    private int remainingTicks;
    private int chanceTimer;
    private int effectTimer;

    public PoisonRainWorldData() { super(DATA_NAME); }
    public PoisonRainWorldData(String name) { super(name); }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        active = nbt.getBoolean("Active");
        remainingTicks = nbt.getInteger("RemainingTicks");
        chanceTimer = nbt.getInteger("ChanceTimer");
        effectTimer = nbt.getInteger("EffectTimer");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Active", active);
        nbt.setInteger("RemainingTicks", remainingTicks);
        nbt.setInteger("ChanceTimer", chanceTimer);
        nbt.setInteger("EffectTimer", effectTimer);
    }

    public boolean isActive() { return active; }
    public int getRemainingTicks() { return remainingTicks; }
    public int getChanceTimer() { return chanceTimer; }
    public int getEffectTimer() { return effectTimer; }

    public void start(int duration) {
        active = true;
        remainingTicks = duration;
        effectTimer = 0;
        markDirty();
    }

    public void stop() {
        active = false;
        remainingTicks = 0;
        effectTimer = 0;
        markDirty();
    }

    public void tickRain() {
        remainingTicks--;
        effectTimer++;
        markDirty();
    }

    public void resetEffectTimer() {
        effectTimer = 0;
        markDirty();
    }

    public boolean tickChanceTimer(int interval) {
        chanceTimer++;
        if (chanceTimer < interval) {
            markDirty();
            return false;
        }
        chanceTimer = 0;
        markDirty();
        return true;
    }

    public static PoisonRainWorldData get(World world) {
        PoisonRainWorldData data = (PoisonRainWorldData) world.mapStorage.loadData(
                PoisonRainWorldData.class, DATA_NAME
        );
        if (data == null) {
            data = new PoisonRainWorldData();
            world.mapStorage.setData(DATA_NAME, data);
        }
        return data;
    }
}
