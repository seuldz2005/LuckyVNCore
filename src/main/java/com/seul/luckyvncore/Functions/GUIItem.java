package com.seul.luckyvncore.Functions;

import org.bukkit.configuration.ConfigurationSection;

public class GUIItem {

    private LKItems items;
    private int slot;

    private String name;

    public GUIItem(ConfigurationSection section) {
        this.name = section.getString("name", "");
        this.items = new LKItems(section.getString("material"), name, section.getStringList("lore"));
        this.slot = section.getInt("slot");
    }
    public String getName() {
        return name;
    }

    public LKItems getItems() {
        return items;
    }

    public int getSlot() {
        return slot;
    }
}