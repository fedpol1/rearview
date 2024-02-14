package com.fedpol1.rearview.mixin;

import com.fedpol1.rearview.config.ModConfig;
import net.minecraft.client.gui.hud.SpectatorHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpectatorHud.class)
public abstract class SpectatorHudMixin {

    @Inject(method = "getSpectatorMenuHeight()F", at = @At(value = "RETURN"), cancellable = true)
    private void rearviewCaptureReflection(CallbackInfoReturnable<Float> cir) {
        if(ModConfig.KEEP_SPECTATOR_MENU) {
            cir.setReturnValue(1.0f);
        }
    }
}
