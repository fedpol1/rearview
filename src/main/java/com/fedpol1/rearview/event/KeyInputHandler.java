package com.fedpol1.rearview.event;

import com.fedpol1.rearview.util.CameraAngleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.fedpol1.rearview.config.ModConfig;

public class KeyInputHandler {

    public static final String KEY_CATEGORY_LOOKBEHIND = "key.categories.misc";
    public static final String KEY_NAME_LOOKBEHIND = "key.rearview.lookbehind";

    public static KeyBinding lookbehindKey;

    private static boolean lookbehind;

    public static boolean isLookingBehind() {
        return lookbehind;
    }

    public static void registerKeypress() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            switch (ModConfig.LOOK_HOLD_TOGGLE) {
                case HOLD -> {
                    lookbehind = false;
                    if (lookbehindKey.isPressed()) {
                        lookbehind = true;
                    }
                    else CameraAngleManager.refresh();
                }
                case TOGGLE -> {
                    if (lookbehindKey.wasPressed()) {
                        lookbehind = !lookbehind;
                        CameraAngleManager.refresh();
                    }
                }
            }
        });
    }

    public static void registerKeybind() {
        lookbehindKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_NAME_LOOKBEHIND, // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_UNKNOWN, // The keycode of the key
                KEY_CATEGORY_LOOKBEHIND // The translation key of the keybinding's category.
        ));
        registerKeypress();
    }
}
