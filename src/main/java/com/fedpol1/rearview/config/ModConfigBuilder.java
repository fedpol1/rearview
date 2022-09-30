package com.fedpol1.rearview.config;

import com.fedpol1.rearview.util.Pair;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;


import java.util.ArrayList;
import java.util.List;

public class ModConfigBuilder implements SimpleConfig.DefaultConfig {

    private String configContents = "";
    private final ArrayList<Pair> configList = new ArrayList<>();

    public void addKeyAndValue(Pair<String, ?> keyValue) {
        configList.add(keyValue);
        configContents += keyValue.getLeft() + "=" + keyValue.getRight() + "\n";
    }

    public ArrayList getConfigList() {
        return this.configList;
    }

    @Override
    public String get(String namespace) {
        return configContents;
    }
}
