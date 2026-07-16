package com.fedpol1.rearview.util;

import com.fedpol1.rearview.config.AngleHandling;
import com.fedpol1.rearview.config.AngleSource;
import com.fedpol1.rearview.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3d;

public class CameraAngleManager {

    private static final float RAD_TO_DEG = 180.0f / 3.14159265358979f;
    private static final float EPSILON = 0.0001f;

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

    public static Vector2f motionToAngle(Vector3d motion) {
        if(motion.lengthSquared() < CameraAngleManager.EPSILON) { return null; }
        Vector3d norm = motion.normalize();
        float yaw = (float)Math.atan(norm.z / norm.x) * RAD_TO_DEG + (norm.x >= 0 ? -90.0f : 90.0f);
        float pitch = (float)Math.acos(norm.y) * RAD_TO_DEG - 90.0f;
        return new Vector2f(yaw, pitch);
    }

    private static Vector3d getMotion(Entity e) {
        if(e == null) { return new Vector3d(0); }
        Vec3 speed = e.getKnownSpeed();
        return new Vector3d(speed.x, speed.y, speed.z);
    }

    public static void setAngles(float yaw, float pitch) {
        float tickDelta = Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks();

        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null || CameraAngleManager.stale) {
            return;
        }

        Entity yawSource = null;
        Entity pitchSource = null;
        switch (ModConfig.YAW_SOURCE) {
            case SELF, SELF_MOTION -> yawSource = player;
            case VEHICLE, VEHICLE_MOTION -> yawSource = player.getVehicle();
            case ROOT_VEHICLE, ROOT_MOTION -> yawSource = player.getRootVehicle();
        }
        switch (ModConfig.PITCH_SOURCE) {
            case SELF, SELF_MOTION -> pitchSource = player;
            case VEHICLE, VEHICLE_MOTION -> pitchSource = player.getVehicle();
            case ROOT_VEHICLE, ROOT_MOTION -> pitchSource = player.getRootVehicle();
        }

        if(ModConfig.YAW_SOURCE.isMotion()) {
            Vector2f angle = motionToAngle(getMotion(yawSource));
            if(angle == null || Float.isNaN(angle.x)) { CameraAngleManager.yaw = yaw; }
            else { CameraAngleManager.yaw = angle.x; }
        }
        else if(ModConfig.YAW_SOURCE.isRotation()) {
            if(yawSource == null) { CameraAngleManager.yaw = yaw; }
            else { CameraAngleManager.yaw = yawSource.getViewYRot(tickDelta); }
        }
        else if(ModConfig.YAW_SOURCE == AngleSource.ZERO) {
            CameraAngleManager.yaw = 0.0f;
        }

        if(ModConfig.PITCH_SOURCE.isMotion()) {
            Vector2f angle = motionToAngle(getMotion(pitchSource));
            if(angle == null || Float.isNaN(angle.y)) { CameraAngleManager.pitch = pitch; }
            else { CameraAngleManager.pitch = angle.y; }
        }
        else if(ModConfig.PITCH_SOURCE.isRotation()) {
            if(pitchSource == null) { CameraAngleManager.pitch = pitch; }
            else { CameraAngleManager.pitch = pitchSource.getViewXRot(tickDelta); }
        }
        else if(ModConfig.PITCH_SOURCE == AngleSource.ZERO) {
            CameraAngleManager.pitch = 0.0f;
        }
    }

    public static void transformAngles(boolean reflected) {
        if (CameraAngleManager.stale) return;
        if (ModConfig.YAW_HANDLING == AngleHandling.REFLECT ^ reflected) {
            CameraAngleManager.yaw = yaw + 180.0f;
        }
        if (ModConfig.PITCH_HANDLING == AngleHandling.REFLECT ^ reflected) {
            CameraAngleManager.pitch = -pitch;
        }
        CameraAngleManager.yaw += ModConfig.YAW_AUX;
        CameraAngleManager.pitch += ModConfig.PITCH_AUX;
        if (ModConfig.CAMERA_LOCK) CameraAngleManager.stale = true;
    }
}
