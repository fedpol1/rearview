package com.fedpol1.rearview;

import com.fedpol1.rearview.event.KeyInputHandler;

import net.fabricmc.api.ClientModInitializer;

public class RearviewClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyInputHandler.registerKeybind();
    }
}
