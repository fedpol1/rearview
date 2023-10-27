package com.fedpol1.rearview;

import com.fedpol1.rearview.config.ModConfig;
import com.fedpol1.rearview.event.KeyInputHandler;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RearviewClient implements ClientModInitializer {

    public static final String MODID = "rearview";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        ModConfig.registerConfig();
        KeyInputHandler.registerKeybinds();
    }
}
