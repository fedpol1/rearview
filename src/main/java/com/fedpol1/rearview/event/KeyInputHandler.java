package com.fedpol1.rearview.event;

import com.fedpol1.rearview.config.HoldToggle;
import com.fedpol1.rearview.util.CameraAngleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import org.lwjgl.glfw.GLFW;
import com.fedpol1.rearview.config.ModConfig;

public class KeyInputHandler {

    public static final String KEY_CATEGORY_LOOKBEHIND = "key.categories.misc";
    public static final String KEY_CATEGORY_SHOW_ROTATION = "key.categories.misc";
    public static final String KEY_NAME_LOOKBEHIND = "key.rearview.lookbehind";
    public static final String KEY_NAME_SHOW_ROTATION = "key.rearview.show_rotation";

    public static KeyBinding lookbehindKey;
    public static KeyBinding showRotationKey;

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
            if(MinecraftClient.getInstance().player != null) {
                if(showRotation) {
                Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
                MinecraftClient.getInstance().player.sendMessage(MutableText.of(new PlainTextContent.Literal(camera.getYaw() + " " + camera.getPitch())).setStyle(Style.EMPTY.withColor(0xbfbfbf)), true);
                } else if(showRotationInterrupt) {
                    MinecraftClient.getInstance().player.sendMessage(MutableText.of(new PlainTextContent.Literal("")), true);
                }
            }
        });
    }

    public static void registerKeybinds() {
        lookbehindKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_NAME_LOOKBEHIND, // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
                KEY_CATEGORY_LOOKBEHIND // The translation key of the keybinding's category.
        ));
        showRotationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_NAME_SHOW_ROTATION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KEY_CATEGORY_SHOW_ROTATION
        ));
        registerClientTickEvents();
    }

    private static boolean decideHoldToggle(HoldToggle holdToggle, KeyBinding binding, boolean state) {
        switch (holdToggle) {
            case HOLD -> {
                state = false;
                if (binding.isPressed()) {
                    state = true;
                }
            }
            case TOGGLE -> {
                if (binding.wasPressed()) {
                    state = !state;
                }
            }
        }
        return state;
    }
}
