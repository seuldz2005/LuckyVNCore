package com.seul.luckyvncore.Hook;

import com.seul.luckyvncore.LKUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {


    public static Placeholder instance;

    @Override
    @NotNull
    public String getAuthor() {
        return "Crush_Seul"; //
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "luckyvncore"; //
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0"; //
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {


        String[] args = params.split("_");


        switch (args[0].toLowerCase()) {

            case "monoupper": {
                String parse = params.replace("monoupper_", "").replaceAll("[{}]", "%");
                return LKUtils.toSmallCaps(PlaceholderAPI.setPlaceholders(((Player) player), parse));
            }

        }

        return null;

    }
}
