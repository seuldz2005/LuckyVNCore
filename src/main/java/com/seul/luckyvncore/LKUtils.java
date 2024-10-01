package com.seul.luckyvncore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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

    public void sendMessage(Player player, String message) {
        player.sendMessage(color(message));
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


    // Convert String to LOCATION
    public static Location parseStringImpl(String impl) {
        String[] components = impl.split(";");
        World world = Bukkit.getServer().getWorld(components[0]);
        int x = 0, y = 0, z = 0;
        try {
            x = Integer.parseInt(components[1]);
            y = Integer.parseInt(components[2]);
            z = Integer.parseInt(components[3]);
        } catch (Exception err) {
            err.printStackTrace();
        }
        return new Location(world, x, y, z);
    }

    // Convert LOCATION TO STRING
    public static String converttoString(Block block) {
        assert (block != null);
        String worldName = block.getWorld().getName();
        String X = String.valueOf(block.getLocation().getBlockX());
        String Y = String.valueOf(block.getLocation().getBlockY());
        String Z = String.valueOf(block.getLocation().getBlockZ());
        return "{block};{world};{x};{y};{z}"
                .replace("{block}", block.getType().toString())
                .replace("{world}", worldName)
                .replace("{x}", X)
                .replace("{y}", Y)
                .replace("{z}", Z);
    }


}
