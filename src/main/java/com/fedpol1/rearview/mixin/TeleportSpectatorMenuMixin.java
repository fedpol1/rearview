package com.fedpol1.rearview.mixin;

import com.fedpol1.rearview.config.ModConfig;
import net.minecraft.client.gui.hud.spectator.TeleportSpectatorMenu;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;

@Mixin(TeleportSpectatorMenu.class)
public abstract class TeleportSpectatorMenuMixin {

    @Inject(method = "method_46521(Lnet/minecraft/client/network/PlayerListEntry;)Z", at = @At(value = "RETURN"), cancellable = true)
    private static void rearviewFilterSpectators(PlayerListEntry entry, CallbackInfoReturnable<Boolean> cir) {
        if(!ModConfig.FILTER_SPECTATORS) {
            cir.setReturnValue(true);
        }
    }

    @ModifyArg(method = "<init>(Ljava/util/Collection;)V", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;sorted(Ljava/util/Comparator;)Ljava/util/stream/Stream;", ordinal = -1))
    private <T> Comparator<? super T> rearviewModifyRotation(Comparator<? super T> comparator) {
        if(ModConfig.SORT_SPECTATORS) {
            return Comparator.comparing((a) -> ((PlayerListEntry) a).getProfile().name(), String::compareToIgnoreCase);
        }
        return comparator;
    }
}
