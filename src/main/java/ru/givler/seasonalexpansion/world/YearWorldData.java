package ru.givler.seasonalexpansion.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class YearWorldData extends WorldSavedData {

    private static final String DATA_NAME = "YearCycleData";
    private int currentYearIndex = 0;
    private boolean changedThisWinter = false;

    public YearWorldData() { super(DATA_NAME); }
    public YearWorldData(String name) { super(name); }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        currentYearIndex = nbt.getInteger("CurrentYearIndex");
        changedThisWinter = nbt.getBoolean("ChangedThisWinter");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("CurrentYearIndex", currentYearIndex);
        nbt.setBoolean("ChangedThisWinter", changedThisWinter);
    }

    public int getCurrentYear() { return currentYearIndex; }
    public void setCurrentYear(int index) { this.currentYearIndex = index; markDirty(); }

    public boolean hasChangedThisWinter() { return changedThisWinter; }
    public void setChangedThisWinter(boolean value) { this.changedThisWinter = value; markDirty(); }

    public static YearWorldData get(World world) {
        YearWorldData data = (YearWorldData) world.mapStorage.loadData(YearWorldData.class, DATA_NAME);
        if (data == null) {
            data = new YearWorldData();
            world.mapStorage.setData(DATA_NAME, data);
        }
        return data;
    }
}
