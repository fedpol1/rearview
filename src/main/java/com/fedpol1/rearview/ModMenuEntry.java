package com.fedpol1.rearview;

import com.fedpol1.rearview.config.ModConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntry implements ModMenuApi{

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModConfig::createGui;
    }

}
