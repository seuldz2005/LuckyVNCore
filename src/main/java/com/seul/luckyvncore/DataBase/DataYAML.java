package com.seul.luckyvncore.DataBase;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class DataYAML {

    public static DataYAML instance;
    private Plugin plugin;
    private File folderPlugin;

    public DataYAML(Plugin plugin) {
        File file = plugin.getDataFolder();
        this.plugin = plugin;
    }

    public HashMap<UUID, PlayerData> loadPlayerData() {
        File files = new File (getFolderPlugin(), "userdata");

        if (!files.exists()) return null;
        HashMap<UUID, PlayerData> datas = new HashMap<>();
        for (File file : files.listFiles()) {
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
            datas.put(uuid, new PlayerData(uuid));
        }
        return datas;
    }

    public File getFolderPlugin() {
        return folderPlugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }


    public class PlayerData {
        private UUID uuid;
        private Player player;
        private File filedata;
        private FileConfiguration yaml;

        public PlayerData(UUID uuid) {
            this.player = player;
            filedata = new File(getFolderPlugin(), "userdata" + File.separator + uuid +".yml");
            if (!isExist()) {
                createData();
            }
            this.yaml = YamlConfiguration.loadConfiguration(filedata);
        }
        public Player getPlayer() {
            if (Bukkit.getPlayer(uuid) != null)
                return player;
            else return null;
        }
        public boolean isExist() {
            if (filedata.exists()) {
                return true;
            }
            return false;
        }

        public FileConfiguration getYaml() {
            return yaml;
        }

        public void createData() {
            try {
                filedata.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void saveData(YamlConfiguration yaml) {
            try {
                yaml.save(filedata);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
