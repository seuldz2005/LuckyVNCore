package com.seul.luckyvncore;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LKUtils {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    // ALLOW HEX COLOR
    public static String color(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }


    // send message color
    public static void sendMessageList(List<String> message, Player player) {
        for (String s : message) {
            player.sendMessage(color(s));
        }
    }


    // get block face in target block
    public static BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        return lastTwoTargetBlocks.get(1).getFace(lastTwoTargetBlocks.get(0));
    }

}
