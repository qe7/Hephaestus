package net.minecraft.src;

import lombok.Getter;
import lombok.Setter;

public abstract class MapDataBase {
    public final String field_28168_a;
    @Getter
    @Setter
    private boolean dirty;

    public MapDataBase(String s) {
        field_28168_a = s;
    }

    public abstract void readFromNBT(NBTTagCompound nbttagcompound);

    public abstract void writeToNBT(NBTTagCompound nbttagcompound);
}
