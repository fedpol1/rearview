package com.fedpol1.rearview.config;

import com.fedpol1.rearview.RearviewClient;
import com.fedpol1.rearview.util.Pair;

import java.io.*;

import java.io.IOException;


public class ModConfig {
    public static SimpleConfig CONFIG;
    private static ModConfigBuilder configBuilder;
    public static YawHandling YAW_HANDLING;
    public static PitchHandling PITCH_HANDLING;
    public static LookHoldToggle LOOK_HOLD_TOGGLE;



    public static void registerConfig() {
        configBuilder = new ModConfigBuilder();
        createConfig();
        CONFIG = SimpleConfig.of(RearviewClient.MODID).provider(configBuilder).request();
        assignConfig();
    }

    private static void createConfig() {
        configBuilder.addKeyAndValue(new Pair<>("yaw_handling", "REFLECT"));
        configBuilder.addKeyAndValue(new Pair<>("pitch_handling", "REFLECT"));
        configBuilder.addKeyAndValue(new Pair<>("look_hold_toggle", "HOLD"));
    }

    private static void assignConfig() {
        YAW_HANDLING = YawHandling.valueOf(CONFIG.getOrDefault("yaw_handling", "REFLECT"));
        PITCH_HANDLING = PitchHandling.valueOf(CONFIG.getOrDefault("pitch_handling", "REFLECT"));
        LOOK_HOLD_TOGGLE = LookHoldToggle.valueOf(CONFIG.getOrDefault("look_hold_toggle", "HOLD"));
    }

    public static void writeConfig() {
        try {
            File output = SimpleConfig.of(RearviewClient.MODID).provider(configBuilder).getFile();
            FileWriter fw = new FileWriter(output);
            fw.write(
                    "yaw_handling=" + YAW_HANDLING.toString() + "\n" +
                    "pitch_handling=" + PITCH_HANDLING.toString() + "\n" +
                    "look_hold_toggle=" + LOOK_HOLD_TOGGLE.toString() + "\n"
            );
            fw.close();
        }
        catch (IOException e) {
            RearviewClient.LOGGER.error("Could not write configuration file.");
        }
    }
}
