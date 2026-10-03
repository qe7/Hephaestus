package net.minecraft.src;

public interface IWorldAccess {
    void markBlockAndNeighborsNeedsUpdate(int i, int j, int k);

    void markBlockRangeNeedsUpdate(int i, int j, int k, int l, int i1, int j1);

    void playSound(String s, double d, double d1, double d2, float f, float f1);

    void spawnParticle(String s, double d, double d1, double d2, double d3, double d4, double d5);

    void obtainEntitySkin(Entity entity);

    void releaseEntitySkin(Entity entity);

    void updateAllRenderers();

    void playRecord(String s, int i, int j, int k);

    void doNothingWithTileEntity(int i, int j, int k, TileEntity tileentity);

    void func_28136_a(EntityPlayer entityplayer, int i, int j, int k, int l, int i1);
}
