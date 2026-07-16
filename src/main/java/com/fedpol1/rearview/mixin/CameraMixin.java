package com.fedpol1.rearview.mixin;

import com.fedpol1.rearview.util.CameraAngleManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.fedpol1.rearview.event.KeyInputHandler;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Redirect(method = "alignWithEntity(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = -1))
    private void rearviewModifyRotation(Camera instance, float yaw, float pitch) {
        boolean reflected = ((Camera)(Object)this).isDetached() && Minecraft.getInstance().options.getCameraType().isMirrored();
        CameraAngleManager.setAngles(yaw, pitch);
        CameraAngleManager.transformAngles(reflected);

        float yawFinal = yaw - (reflected ? 180.0f : 0.0f); // undo front-facing third-person reflection, it will be redone in the camera angle manager
        float pitchFinal = pitch * (reflected ? -1.0f : 1.0f); // undo front-facing third-person reflection, it will be redone in the camera angle manager
        if(KeyInputHandler.isLookingBehind()) {
            yawFinal = CameraAngleManager.getYaw();
            pitchFinal = CameraAngleManager.getPitch();
        }
        ((CameraMixin) (Object) instance).setRotation(yawFinal, pitchFinal);
    }
}
