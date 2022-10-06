package com.fedpol1.rearview.config;

import com.fedpol1.rearview.RearviewClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(RearviewClient.MODID + ".properties").toFile();

    public static YawHandling YAW_HANDLING;
    public static PitchHandling PITCH_HANDLING;
    public static boolean CAMERA_LOCK;
    public static LookHoldToggle LOOK_HOLD_TOGGLE;
    public static final String S_YAW_HANDLING = "yaw_handling";
    public static final String S_PITCH_HANDLING = "pitch_handling";
    public static final String S_CAMERA_LOCK = "camera_lock";
    public static final String S_LOOK_HOLD_TOGGLE = "look_hold_toggle";

    public static void registerConfig() {
        ModConfig.setDefaultConfig();
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    private static void setDefaultConfig() {
        ModConfig.YAW_HANDLING = YawHandling.REFLECT;
        ModConfig.PITCH_HANDLING = PitchHandling.REFLECT;
        ModConfig.CAMERA_LOCK = false;
        ModConfig.LOOK_HOLD_TOGGLE = LookHoldToggle.HOLD;
    }

    public static void readConfig() {
        try {
            ModConfig.CONFIG_FILE.createNewFile();
            Scanner sc = new Scanner(ModConfig.CONFIG_FILE);
            String[] split;
            while(sc.hasNextLine()) {
                split = sc.nextLine().split("=");
                switch(split[0]) {
                    case S_YAW_HANDLING -> ModConfig.YAW_HANDLING = YawHandling.valueOf(split[1]);
                    case S_PITCH_HANDLING -> ModConfig.PITCH_HANDLING = PitchHandling.valueOf(split[1]);
                    case S_CAMERA_LOCK -> ModConfig.CAMERA_LOCK = Boolean.parseBoolean(split[1]);
                    case S_LOOK_HOLD_TOGGLE -> ModConfig.LOOK_HOLD_TOGGLE = LookHoldToggle.valueOf(split[1]);
                }
            }
        }
        catch (IOException e) {
            RearviewClient.LOGGER.error("Could not read configuration file.");
            ModConfig.setDefaultConfig();
        }
    }

    public static void writeConfig() {
        try {
            FileWriter fw = new FileWriter(ModConfig.CONFIG_FILE);
            String acc = S_YAW_HANDLING + "=" + ModConfig.YAW_HANDLING + "\n" +
                    S_PITCH_HANDLING + "=" + ModConfig.PITCH_HANDLING + "\n" +
                    S_CAMERA_LOCK + "=" + ModConfig.CAMERA_LOCK + "\n" +
                    S_LOOK_HOLD_TOGGLE + "=" + ModConfig.LOOK_HOLD_TOGGLE + "\n";

            fw.write(acc);
            fw.close();
        }
        catch (IOException e) {
            RearviewClient.LOGGER.error("Could not write configuration file.");
        }
    }
}
