package com.fedpol1.rearview.mixin;

import com.fedpol1.rearview.util.CameraAngleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.text.*;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.fedpol1.rearview.event.KeyInputHandler;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Redirect(method = "update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = -1))
    private void rearviewModifyRotation(Camera instance, float yaw, float pitch) {
        CameraAngleManager.setAngles(yaw, pitch);
        CameraAngleManager.transformAngles();

        float yawFinal = yaw - (CameraAngleManager.isReflected ? 180.0f : 0.0f); // undo front-facing third-person reflection, it will be redone in the camera angle manager
        float pitchFinal = pitch * (CameraAngleManager.isReflected ? -1.0f : 1.0f); // undo front-facing third-person reflection, it will be redone in the camera angle manager
        if(KeyInputHandler.isLookingBehind()) {
            yawFinal = CameraAngleManager.getYaw();
            pitchFinal = CameraAngleManager.getPitch();
        }
        ((CameraMixin) (Object) instance).setRotation(yawFinal, pitchFinal);
    }

    @Inject(method = "update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = -1))
    private void rearviewCaptureReflection(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        CameraAngleManager.isReflected = thirdPerson && inverseView;
    }
}
