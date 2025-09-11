package com.seul.luckyvncore.Functions;

import org.bukkit.configuration.ConfigurationSection;

public class GUIItem {

    private LKItems items;
    private int slot;

    public GUIItem(ConfigurationSection section) {
        this.items = new LKItems(section.getString("material"), section.getString("name"), section.getStringList("lore"));
        this.slot = section.getInt("slot");
    }

    public LKItems getItems() {
        return items;
    }

    public int getSlot() {
        return slot;
    }
}