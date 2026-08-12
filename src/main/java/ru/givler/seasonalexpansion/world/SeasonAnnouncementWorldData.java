package ru.givler.seasonalexpansion.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

/** Persisted server state used to distinguish a real season transition from a restart. */
public final class SeasonAnnouncementWorldData extends WorldSavedData {
    private static final String DATA_NAME = "SeasonAnnouncementData";
    private static final String SEASON_KEY = "SeasonOrdinal";

    private int seasonOrdinal = -1;

    public SeasonAnnouncementWorldData() {
        super(DATA_NAME);
    }

    public SeasonAnnouncementWorldData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        seasonOrdinal = nbt.hasKey(SEASON_KEY) ? nbt.getInteger(SEASON_KEY) : -1;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger(SEASON_KEY, seasonOrdinal);
    }

    public int getSeasonOrdinal() {
        return seasonOrdinal;
    }

    public void setSeasonOrdinal(int seasonOrdinal) {
        this.seasonOrdinal = seasonOrdinal;
        markDirty();
    }

    public static SeasonAnnouncementWorldData get(World world) {
        SeasonAnnouncementWorldData data = (SeasonAnnouncementWorldData) world.mapStorage.loadData(
                SeasonAnnouncementWorldData.class, DATA_NAME
        );
        if (data == null) {
            data = new SeasonAnnouncementWorldData();
            world.mapStorage.setData(DATA_NAME, data);
        }
        return data;
    }
}
