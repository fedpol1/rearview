package com.fedpol1.rearview;

import com.fedpol1.rearview.config.LookHoldToggle;
import com.fedpol1.rearview.config.ModConfig;
import com.fedpol1.rearview.config.PitchHandling;
import com.fedpol1.rearview.config.YawHandling;
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

            // yaw
            misc.addEntry(entryBuilder.startEnumSelector(Text.translatable("rearview.config.yaw"), YawHandling.class, ModConfig.YAW_HANDLING)
                    .setDefaultValue(YawHandling.REFLECT)
                    .setSaveConsumer(newValue -> ModConfig.YAW_HANDLING = newValue)
                    .build());

            // pitch
            misc.addEntry(entryBuilder.startEnumSelector(Text.translatable("rearview.config.pitch"), PitchHandling.class, ModConfig.PITCH_HANDLING)
                    .setDefaultValue(PitchHandling.REFLECT)
                    .setSaveConsumer(newValue -> ModConfig.PITCH_HANDLING = newValue)
                    .build());

            // camera lock
            misc.addEntry(entryBuilder.startBooleanToggle(Text.translatable("rearview.config.lock"), ModConfig.CameraLock)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> ModConfig.CameraLock = newValue)
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
