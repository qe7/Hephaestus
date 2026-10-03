package net.minecraft.src;

public interface ICamera {
    boolean isBoundingBoxInFrustum(AxisAlignedBB axisalignedbb);

    void setPosition(double d, double d1, double d2);
}
