package com.seul.luckyvncore.Functions;

import com.seul.luckyvncore.LKUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class LuckyGUI {

    private String title;
    private int size;
    private InventoryHolder holder;
    private ConfigurationSection section;
    private InventoryType type;

    public LuckyGUI(ConfigurationSection section, InventoryHolder holder, InventoryType type) {
        this.title = section.getString("title");
        this.section = section;
        this.holder = holder;
        this.type = type;
    }
    public LuckyGUI(ConfigurationSection section, InventoryHolder holder) {
        this.title = section.getString("title");
        this.size = section.getInt("size");
        this.section = section;
        this.holder = holder;
    }


    public Inventory getInventory() {
        Inventory inventory;
        if (type == null) {
            inventory = Bukkit.createInventory(holder, size, LKUtils.color(title));
        } else inventory = Bukkit.createInventory(holder, type, LKUtils.color(title));

        for (String key : Objects.requireNonNull(section.getConfigurationSection("items")).getKeys(false)) {
            LKItems lkItems = new LKItems(Objects.requireNonNull(section.getString("items." + key + ".material")), section.getString("items." + key + ".name"), section.getStringList("items." + key + ".lore"));
            ItemStack item = lkItems.getItem();
            for (Integer slot : LKUtils.convertStringtoList(Objects.requireNonNull(section.getString("items." + key + ".slots")))) {
                inventory.setItem(slot, item);
            }
        }
        return inventory;
    }
}
