package io.github.qe7.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.PlayerControllerMP;

public class PlayerUtils {
    public static Minecraft mc;

    public static boolean destroyBlock(int x, int y, int z, int side) {
        PlayerUtils.mc.playerController.clickBlock(x, y, z, side);
        PlayerUtils.mc.playerController.sendBlockRemoving(x, y, z, side);
        PlayerUtils.mc.playerController.field_1064_b = PlayerUtils.mc.playerController.isBeingUsed();
        return PlayerUtils.mc.playerController.isBeingUsed();
    }

    public static boolean placeBlockUnsafe(int x, int y, int z, int side) {
        PlayerUtils.mc.playerController.sendPlaceBlock(PlayerUtils.mc.thePlayer, PlayerUtils.mc.theWorld, PlayerUtils.mc.thePlayer.getCurrentEquippedItem(), x, y, z, side);
        return PlayerUtils.mc.playerController.isBeingUsed();
    }

    public static boolean placeBlock(int x, int y, int z, int side) {
        if (mc.isMultiplayerWorld()) {
            mc.getSendQueue().addToSendQueue(new Packet15Place(x, y, z, side, PlayerUtils.mc.thePlayer.getCurrentEquippedItem()));
            return true;
        }
        return PlayerUtils.placeBlockUnsafe(x, y, z, side);
    }

    public static boolean destroyBlockInstant(int x, int y, int z, int side) {
        if (mc.isMultiplayerWorld()) {
            mc.getSendQueue().addToSendQueue(new Packet14BlockDig(0, x, y, z, side));
            mc.getSendQueue().addToSendQueue(new Packet14BlockDig(2, x, y, z, side));
            return false;
        }
        PlayerUtils.mc.playerController.sendBlockRemoved(x, y, z, side);
        return false;
    }
}