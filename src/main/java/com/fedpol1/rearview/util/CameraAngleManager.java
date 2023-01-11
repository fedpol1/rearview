package com.fedpol1.rearview.util;

import com.fedpol1.rearview.config.AngleHandling;
import com.fedpol1.rearview.config.AngleSource;
import com.fedpol1.rearview.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class CameraAngleManager {

    private static final float RAD_TO_DEG = 180.0f / 3.14159265358979f;
    private static final float EPSILON = 0.0001f;

    public static boolean isReflected; // true if player is in front-facing third person

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

    public static Vec2f motionToAngle(Vec3d motion) {
        if(motion.lengthSquared() < CameraAngleManager.EPSILON) { return null; }
        Vec3d norm = motion.normalize();
        float yaw = (float)Math.atan(norm.getZ() / norm.getX()) * RAD_TO_DEG + (norm.x >= 0 ? -90.0f : 90.0f);
        float pitch = (float)Math.acos(norm.getY()) * RAD_TO_DEG - 90.0f;
        return new Vec2f(yaw, pitch);
    }

    private static Vec3d getMotion(Entity e) {
        if(e == null) { return Vec3d.ZERO; }
        return new Vec3d(e.getX()-e.lastRenderX, e.getY()-e.lastRenderY, e.getZ()-e.lastRenderZ);
    }

    public static void setAngles(float yaw, float pitch) {
        float tickDelta = MinecraftClient.getInstance().getTickDelta();

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
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
            Vec2f angle = motionToAngle(getMotion(yawSource));
            if(angle == null || Float.isNaN(angle.x)) { CameraAngleManager.yaw = yaw; }
            else { CameraAngleManager.yaw = angle.x; }
        }
        else if(ModConfig.YAW_SOURCE.isRotation()) {
            if(yawSource == null) { CameraAngleManager.yaw = yaw; }
            else { CameraAngleManager.yaw = yawSource.getYaw(tickDelta); }
        }
        else if(ModConfig.YAW_SOURCE == AngleSource.CONSTANT) {
            CameraAngleManager.yaw = ModConfig.YAW_AUX;
        }

        if(ModConfig.PITCH_SOURCE.isMotion()) {
            Vec2f angle = motionToAngle(getMotion(pitchSource));
            if(angle == null || Float.isNaN(angle.y)) { CameraAngleManager.pitch = pitch; }
            else { CameraAngleManager.pitch = angle.y; }
        }
        else if(ModConfig.PITCH_SOURCE.isRotation()) {
            if(pitchSource == null) { CameraAngleManager.pitch = pitch; }
            else { CameraAngleManager.pitch = pitchSource.getPitch(tickDelta); }
        }
        else if(ModConfig.PITCH_SOURCE == AngleSource.CONSTANT) {
            CameraAngleManager.pitch = ModConfig.PITCH_AUX;
        }
    }

    public static void transformAngles() {
        if (CameraAngleManager.stale) return;
        if (ModConfig.YAW_HANDLING == AngleHandling.REFLECT ^ CameraAngleManager.isReflected) {
            CameraAngleManager.yaw = yaw + 180.0f;
        }
        if (ModConfig.PITCH_HANDLING == AngleHandling.REFLECT ^ CameraAngleManager.isReflected) {
            CameraAngleManager.pitch = -pitch;
        }
        if (ModConfig.CAMERA_LOCK) CameraAngleManager.stale = true;
    }
}
