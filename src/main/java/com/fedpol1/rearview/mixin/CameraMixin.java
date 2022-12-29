package com.fedpol1.rearview.mixin;

import com.fedpol1.rearview.util.CameraAngleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.fedpol1.rearview.event.KeyInputHandler;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float yaw;

    @Shadow
    private float pitch;

    @Final
    @Shadow
    private Quaternion rotation;

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
