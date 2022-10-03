package com.fedpol1.rearview.mixin;

import com.fedpol1.rearview.util.CameraAngleManager;
import net.minecraft.client.render.Camera;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.fedpol1.rearview.event.KeyInputHandler;
import com.fedpol1.rearview.config.ModConfig;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float yaw;

    @Shadow
    private float pitch;

    @Shadow
    private Quaternion rotation;

    @Inject(method = "update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/render/Camera;yaw:F"))
    protected void injectYaw(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if(KeyInputHandler.isLookingBehind()) {
            switch (ModConfig.YAW_HANDLING) {
                case REFLECT -> this.yaw = this.yaw - 180.0f;
            }
        }
    }

    @Inject(method = "update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/render/Camera;pitch:F"))
    protected void injectPitch(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if(KeyInputHandler.isLookingBehind()) {
            switch (ModConfig.PITCH_HANDLING) {
                case REFLECT -> this.pitch = -this.pitch;
                case ZERO -> this.pitch = 0.0f;
                case LOOK_UP -> this.pitch = -90.0f;
                case LOOK_DOWN -> this.pitch = 90.0f;
            }
        }
    }

    @Inject(method = "setRotation(FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Quaternion;hamiltonProduct(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 1, shift = At.Shift.AFTER))//, shift = At.Shift.AFTER))
    protected void injectRotation(float yaw, float pitch, CallbackInfo info) {
        if(KeyInputHandler.isLookingBehind()) {
            this.rotation.set(0.0f, 0.0f, 0.0f, 1.0f);
            CameraAngleManager.setAngles(yaw,pitch);
            this.yaw = CameraAngleManager.getYaw();
            this.pitch = CameraAngleManager.getPitch();
            this.rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-CameraAngleManager.getYaw()));
            this.rotation.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(CameraAngleManager.getPitch()));
        }
    }
}
