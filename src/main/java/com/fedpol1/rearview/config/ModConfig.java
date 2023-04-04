package com.fedpol1.rearview.config;

import com.fedpol1.rearview.RearviewClient;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.string.number.FloatFieldController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(RearviewClient.MODID + ".properties").toFile();

    public static AngleHandling YAW_HANDLING;
    public static AngleHandling PITCH_HANDLING;
    public static AngleSource YAW_SOURCE;
    public static AngleSource PITCH_SOURCE;
    public static float YAW_AUX;
    public static float PITCH_AUX;
    public static boolean CAMERA_LOCK;
    public static LookHoldToggle LOOK_HOLD_TOGGLE;
    public static final String S_YAW_HANDLING = "yaw_handling";
    public static final String S_PITCH_HANDLING = "pitch_handling";
    public static final String S_YAW_SOURCE = "yaw_source";
    public static final String S_PITCH_SOURCE = "pitch_source";
    public static final String S_YAW_AUX = "yaw_aux";
    public static final String S_PITCH_AUX = "pitch_aux";
    public static final String S_CAMERA_LOCK = "camera_lock";
    public static final String S_LOOK_HOLD_TOGGLE = "look_hold_toggle";

    public static void registerConfig() {
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    private static void setDefaultConfig() {
        ModConfig.YAW_HANDLING = AngleHandling.REFLECT;
        ModConfig.PITCH_HANDLING = AngleHandling.REFLECT;
        ModConfig.YAW_SOURCE = AngleSource.SELF;
        ModConfig.PITCH_SOURCE = AngleSource.SELF;
        ModConfig.YAW_AUX = 0.0f;
        ModConfig.PITCH_AUX = 0.0f;
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
                if(split[1].equals("null")) { throw new IllegalArgumentException(); }
                switch(split[0]) {
                    case S_YAW_HANDLING -> ModConfig.YAW_HANDLING = AngleHandling.valueOf(split[1]);
                    case S_PITCH_HANDLING -> ModConfig.PITCH_HANDLING = AngleHandling.valueOf(split[1]);
                    case S_YAW_SOURCE -> ModConfig.YAW_SOURCE = AngleSource.valueOf(split[1]);
                    case S_PITCH_SOURCE -> ModConfig.PITCH_SOURCE = AngleSource.valueOf(split[1]);
                    case S_YAW_AUX -> ModConfig.YAW_AUX = Float.parseFloat(split[1]);
                    case S_PITCH_AUX -> ModConfig.PITCH_AUX = Float.parseFloat(split[1]);
                    case S_CAMERA_LOCK -> ModConfig.CAMERA_LOCK = Boolean.parseBoolean(split[1]);
                    case S_LOOK_HOLD_TOGGLE -> ModConfig.LOOK_HOLD_TOGGLE = LookHoldToggle.valueOf(split[1]);
                }
            }
        }
        catch (IOException | IllegalArgumentException e) {
            RearviewClient.LOGGER.error("Could not read configuration file.\n" + e.getMessage());
            RearviewClient.LOGGER.info("Setting default configuration.");
            ModConfig.setDefaultConfig();
        }
    }

    public static void writeConfig() {
        try {
            FileWriter fw = new FileWriter(ModConfig.CONFIG_FILE);
            String acc = S_YAW_HANDLING + "=" + ModConfig.YAW_HANDLING + "\n" +
                    S_PITCH_HANDLING + "=" + ModConfig.PITCH_HANDLING + "\n" +
                    S_YAW_SOURCE + "=" + ModConfig.YAW_SOURCE + "\n" +
                    S_PITCH_SOURCE + "=" + ModConfig.PITCH_SOURCE + "\n" +
                    S_YAW_AUX + "=" + ModConfig.YAW_AUX + "\n" +
                    S_PITCH_AUX + "=" + ModConfig.PITCH_AUX + "\n" +
                    S_CAMERA_LOCK + "=" + ModConfig.CAMERA_LOCK + "\n" +
                    S_LOOK_HOLD_TOGGLE + "=" + ModConfig.LOOK_HOLD_TOGGLE + "\n";

            fw.write(acc);
            fw.close();
        }
        catch (IOException e) {
            RearviewClient.LOGGER.error("Could not write configuration file.\n" + e.getMessage());
        }
    }

    public static Screen createGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable(RearviewClient.MODID + ".config"))
                .save(ModConfig::writeConfig)
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable(RearviewClient.MODID + ".config.category.misc"))
                        .option(Option.createBuilder(AngleHandling.class)
                                .name(Text.translatable(RearviewClient.MODID + ".config.yaw_transform"))
                                .binding(AngleHandling.REFLECT, () -> ModConfig.YAW_HANDLING, v -> ModConfig.YAW_HANDLING = v)
                                .controller(EnumController::new)
                                .build())
                        .option(Option.createBuilder(AngleHandling.class)
                                .name(Text.translatable(RearviewClient.MODID + ".config.pitch_transform"))
                                .binding(AngleHandling.REFLECT, () -> ModConfig.PITCH_HANDLING, v -> ModConfig.PITCH_HANDLING = v)
                                .controller(EnumController::new)
                                .build())
                        .option(Option.createBuilder(AngleSource.class)
                                .name(Text.translatable(RearviewClient.MODID + ".config.yaw_source"))
                                .binding(AngleSource.SELF, () -> ModConfig.YAW_SOURCE, v -> ModConfig.YAW_SOURCE = v)
                                .controller(EnumController::new)
                                .build())
                        .option(Option.createBuilder(AngleSource.class)
                                .name(Text.translatable(RearviewClient.MODID + ".config.pitch_source"))
                                .binding(AngleSource.SELF, () -> ModConfig.PITCH_SOURCE, v -> ModConfig.PITCH_SOURCE = v)
                                .controller(EnumController::new)
                                .build())
                        .option(Option.createBuilder(Float.class)
                                .name(Text.translatable(RearviewClient.MODID + ".config.yaw_aux"))
                                .binding(0.0f, () -> ModConfig.YAW_AUX, v -> ModConfig.YAW_AUX = v)
                                .controller(FloatFieldController::new)
                                .build())
                        .option(Option.createBuilder(Float.class)
                                .name(Text.translatable(RearviewClient.MODID + ".config.pitch_aux"))
                                .binding(0.0f, () -> ModConfig.PITCH_AUX, v -> ModConfig.PITCH_AUX = v)
                                .controller(FloatFieldController::new)
                                .build())
                        .option(Option.createBuilder(Boolean.class)
                                .name(Text.translatable(RearviewClient.MODID + ".config.lock"))
                                .binding(false, () -> ModConfig.CAMERA_LOCK, v -> ModConfig.CAMERA_LOCK = v)
                                .controller(TickBoxController::new)
                                .build())
                        .option(Option.createBuilder(LookHoldToggle.class)
                                .name(Text.translatable(RearviewClient.MODID + ".config.look_hold_toggle"))
                                .binding(LookHoldToggle.HOLD, () -> ModConfig.LOOK_HOLD_TOGGLE, v -> ModConfig.LOOK_HOLD_TOGGLE = v)
                                .controller(EnumController::new)
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
    }
}
