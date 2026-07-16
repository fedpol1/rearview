package com.fedpol1.rearview.mixin;

import com.fedpol1.rearview.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayerMenuCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(TeleportToPlayerMenuCategory.class)
public abstract class TeleportSpectatorMenuMixin {

    @WrapOperation(method = "<init>(Ljava/util/Collection;)V", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;", ordinal = -1))
    private static <T> Stream<T> rearviewFilterSpectators(Stream<T> stream, Predicate<? super T> predicate, Operation<Stream<T>> original) {
        if(!ModConfig.FILTER_SPECTATORS) {
            return stream;
        }
        return original.call(stream, predicate);
    }

    @ModifyArg(method = "<init>(Ljava/util/Collection;)V", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;sorted(Ljava/util/Comparator;)Ljava/util/stream/Stream;", ordinal = -1))
    private <T> Comparator<? super T> rearviewModifyRotation(Comparator<? super T> comparator) {
        if(ModConfig.SORT_SPECTATORS) {
            return Comparator.comparing((a) -> ((SpectatorMenuItem) a).getName().getString(), String::compareToIgnoreCase);
        }
        return comparator;
    }
}
