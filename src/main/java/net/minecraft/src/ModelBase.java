package net.minecraft.src;

public abstract class ModelBase {
    public float onGround;
    public boolean isRiding;

    public ModelBase() {
        isRiding = false;
    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5) {
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
    }

    public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2) {
    }
}
