package com.fedpol1.rearview;

import com.fedpol1.rearview.config.AngleSource;
import com.fedpol1.rearview.config.LookHoldToggle;
import com.fedpol1.rearview.config.ModConfig;
import com.fedpol1.rearview.config.AngleHandling;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ModMenuEntry implements ModMenuApi{

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(MinecraftClient.getInstance().currentScreen)
                    .setTitle(Text.translatable("rearview.config"));

            ConfigCategory misc = builder.getOrCreateCategory(Text.translatable("rearview.config.category.misc"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // yaw handling
            misc.addEntry(entryBuilder.startEnumSelector(Text.translatable("rearview.config.yaw_transform"), AngleHandling.class, ModConfig.YAW_HANDLING)
                    .setDefaultValue(AngleHandling.REFLECT)
                    .setSaveConsumer(newValue -> ModConfig.YAW_HANDLING = newValue)
                    .build());

            // pitch handling
            misc.addEntry(entryBuilder.startEnumSelector(Text.translatable("rearview.config.pitch_transform"), AngleHandling.class, ModConfig.PITCH_HANDLING)
                    .setDefaultValue(AngleHandling.REFLECT)
                    .setSaveConsumer(newValue -> ModConfig.PITCH_HANDLING = newValue)
                    .build());

            // yaw source
            misc.addEntry(entryBuilder.startEnumSelector(Text.translatable("rearview.config.yaw_source"), AngleSource.class, ModConfig.YAW_SOURCE)
                    .setDefaultValue(AngleSource.SELF)
                    .setSaveConsumer(newValue -> ModConfig.YAW_SOURCE = newValue)
                    .build());

            // pitch source
            misc.addEntry(entryBuilder.startEnumSelector(Text.translatable("rearview.config.pitch_source"), AngleSource.class, ModConfig.PITCH_SOURCE)
                    .setDefaultValue(AngleSource.SELF)
                    .setSaveConsumer(newValue -> ModConfig.PITCH_SOURCE = newValue)
                    .build());

            // yaw aux
            misc.addEntry(entryBuilder.startFloatField(Text.translatable("rearview.config.yaw_aux"), ModConfig.YAW_AUX)
                    .setDefaultValue(0.0f)
                    .setSaveConsumer(newValue -> ModConfig.YAW_AUX = newValue)
                    .build());

            // pitch aux
            misc.addEntry(entryBuilder.startFloatField(Text.translatable("rearview.config.pitch_aux"), ModConfig.PITCH_AUX)
                    .setDefaultValue(0.0f)
                    .setSaveConsumer(newValue -> ModConfig.PITCH_AUX = newValue)
                    .build());

            // camera lock
            misc.addEntry(entryBuilder.startBooleanToggle(Text.translatable("rearview.config.lock"), ModConfig.CAMERA_LOCK)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> ModConfig.CAMERA_LOCK = newValue)
                    .build());

            // hold/toggle
            misc.addEntry(entryBuilder.startEnumSelector(Text.translatable("rearview.config.look_hold_toggle"), LookHoldToggle.class, ModConfig.LOOK_HOLD_TOGGLE)
                    .setDefaultValue(LookHoldToggle.HOLD)
                    .setSaveConsumer(newValue -> ModConfig.LOOK_HOLD_TOGGLE = newValue)
                    .build());

            // save config
            builder.setSavingRunnable(ModConfig::writeConfig);
            return builder.build();
        };
    }

}
