package com.fedpol1.rearview.util;

import com.fedpol1.rearview.config.ModConfig;
import com.terraformersmc.modmenu.util.mod.Mod;
import net.minecraft.util.math.Vec3f;

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

    public static void setAngles(float yaw, float pitch){
        if(CameraAngleManager.stale) return;
        switch (ModConfig.YAW_HANDLING) {
            case KEEP -> CameraAngleManager.yaw = yaw;
            case REFLECT -> CameraAngleManager.yaw = yaw + 180.0f;
        }
        switch (ModConfig.PITCH_HANDLING) {
            case KEEP -> CameraAngleManager.pitch = pitch;
            case REFLECT -> CameraAngleManager.pitch = -pitch;
            case ZERO -> CameraAngleManager.pitch = 0.0f;
            case LOOK_UP -> CameraAngleManager.pitch = -90.0f;
            case LOOK_DOWN -> CameraAngleManager.pitch = 90.0f;
        }
        if(ModConfig.CameraLock) CameraAngleManager.stale = true;
    }
}
