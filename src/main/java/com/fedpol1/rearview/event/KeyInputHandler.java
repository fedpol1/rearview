package com.fedpol1.rearview.event;

import com.fedpol1.rearview.config.HoldToggle;
import com.fedpol1.rearview.util.CameraAngleManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import org.lwjgl.glfw.GLFW;
import com.fedpol1.rearview.config.ModConfig;

public class KeyInputHandler {

    public static final String KEY_NAME_LOOKBEHIND = "key.rearview.lookbehind";
    public static final String KEY_NAME_SHOW_ROTATION = "key.rearview.show_rotation";

    public static KeyMapping lookbehindKey;
    public static KeyMapping showRotationKey;

    private static boolean lookbehind;
    private static boolean showRotation;
    private static boolean showRotationInterrupt;

    public static boolean isLookingBehind() {
        return lookbehind;
    }

    public static void registerClientTickEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            lookbehind = KeyInputHandler.decideHoldToggle(ModConfig.LOOK_HOLD_TOGGLE, lookbehindKey, lookbehind);
            CameraAngleManager.refresh();
            showRotationInterrupt = showRotation;
            showRotation = KeyInputHandler.decideHoldToggle(ModConfig.SHOW_ROTATION_HOLD_TOGGLE, showRotationKey, showRotation);
            if(Minecraft.getInstance().player != null) {
                if(showRotation) {
                Camera camera = Minecraft.getInstance().gameRenderer.mainCamera();
                    Minecraft.getInstance().player.sendOverlayMessage(MutableComponent.create(new PlainTextContents.LiteralContents(camera.yaw() + " " + camera.xRot())).setStyle(Style.EMPTY.withColor(0xbfbfbf)));
                } else if(showRotationInterrupt) {
                    Minecraft.getInstance().player.sendOverlayMessage(MutableComponent.create(new PlainTextContents.LiteralContents("")));
                }
            }
        });
    }

    public static void registerKeybinds() {
        lookbehindKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                KEY_NAME_LOOKBEHIND, // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
                KeyMapping.Category.MISC // The translation key of the keybinding's category.
        ));
        showRotationKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                KEY_NAME_SHOW_ROTATION,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KeyMapping.Category.MISC
        ));
        registerClientTickEvents();
    }

    private static boolean decideHoldToggle(HoldToggle holdToggle, KeyMapping binding, boolean state) {
        switch (holdToggle) {
            case HOLD -> state = binding.isDown();
            case TOGGLE -> {
                if (binding.consumeClick()) {
                    state = !state;
                }
            }
        }
        return state;
    }
}
