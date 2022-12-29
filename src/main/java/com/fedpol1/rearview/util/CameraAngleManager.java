package com.fedpol1.rearview.util;

import com.fedpol1.rearview.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class CameraAngleManager {

    private static float yaw;
    private static float pitch;
    private static boolean stale = false;

    public static void refresh() {
        CameraAngleManager.stale = false;
    }

    public static float getYaw() {
        return CameraAngleManager.yaw;
    }

    public static float getPitch() {
        return CameraAngleManager.pitch;
    }

    public static Entity getRootVehicle() {
        PlayerEntity p = MinecraftClient.getInstance().player;
        if(p == null) { return null; }
        return p.getRootVehicle();
    }

    public static void setAngles(float yaw, float pitch) {
        if (CameraAngleManager.stale) return;
        Entity rootVehicle = CameraAngleManager.getRootVehicle();
        switch (ModConfig.YAW_HANDLING) {
            case KEEP -> CameraAngleManager.yaw = yaw;
            case REFLECT -> CameraAngleManager.yaw = yaw + 180.0f;
            case INHERIT -> CameraAngleManager.yaw = rootVehicle == null ? yaw : rootVehicle.getYaw(MinecraftClient.getInstance().getTickDelta());
        }
        switch (ModConfig.PITCH_HANDLING) {
            case KEEP -> CameraAngleManager.pitch = pitch;
            case REFLECT -> CameraAngleManager.pitch = -pitch;
            case ZERO -> CameraAngleManager.pitch = 0.0f;
            case LOOK_UP -> CameraAngleManager.pitch = -90.0f;
            case LOOK_DOWN -> CameraAngleManager.pitch = 90.0f;
            case INHERIT -> CameraAngleManager.pitch = rootVehicle == null ? pitch : rootVehicle.getPitch(MinecraftClient.getInstance().getTickDelta());
        }
        if (ModConfig.CAMERA_LOCK) CameraAngleManager.stale = true;
    }
}
