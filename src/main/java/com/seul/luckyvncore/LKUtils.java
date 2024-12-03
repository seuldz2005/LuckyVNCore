package com.seul.luckyvncore;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
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

    public static void sendMessage(CommandSender player, String message) {


        if (player instanceof Player) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                player.sendMessage(color(PlaceholderAPI.setPlaceholders(((Player) player), message)));
            }
        } else player.sendMessage(color(message));
    }

    public static void sendMessage(CommandSender player, List<String> message) {

        Iterator var2 = message.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();

            if (player instanceof Player) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    player.sendMessage(color(PlaceholderAPI.setPlaceholders(((Player) player), s)));
                }
            } else player.sendMessage(color(s));
        }

    }


    // get block face in target block
    public static BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        return lastTwoTargetBlocks.get(1).getFace(lastTwoTargetBlocks.get(0));
    }


    // Convert String to LOCATION
    public static Location parseStringImpl(String impl) {
        String[] components = impl.split(",");
        World world = Bukkit.getServer().getWorld(components[0]);
        int x = 0;
        int y = 0;
        int z = 0;

        try {
            x = Integer.parseInt(components[1]);
            y = Integer.parseInt(components[2]);
            z = Integer.parseInt(components[3]);
        } catch (Exception var7) {
            Exception err = var7;
            err.printStackTrace();
        }

        Location loc = new Location(world, (double)x, (double)y, (double)z);
        loc.setDirection(new Vector(Double.valueOf(components[4]), Double.valueOf(components[5]), Double.valueOf(components[6])));
        return loc;
    }

    // Convert LOCATION TO STRING

    public static String converttoString(Location location) {
        String worldName = location.getWorld().getName();
        String X = String.valueOf(location.getBlockX());
        String Y = String.valueOf(location.getBlockY());
        String Z = String.valueOf(location.getBlockZ());
        return "{world},{x},{y},{z},{direction}".replace("{world}", worldName).replace("{x}", X).replace("{y}", Y).replace("{z}", Z).replace("{direction}", location.getDirection().toString());
    }


    public static void dispatchCommand(CommandSender sender, String command) {

        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                command = PlaceholderAPI.setPlaceholders(((Player) player), command);
            }
        }


        if (command.contains("[console]")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("[console] ", ""));
            return;
        }
        if (command.contains("[player]")) {
            Player player = (Player)sender;
            Bukkit.dispatchCommand(sender, command.replace("[player] ", "").replace("%player%", player.getName()));
            return;
        }
        if (command.contains("[message]")) {
            LKUtils.sendMessage(sender, command.replace("[message] ", ""));
        }
    }

    public static List<Integer> convertStringtoList(String a) {

        String stripped = a.replaceAll("\\[|\\]", "");

        String[] tokens = stripped.split(",");
        List<Integer> integerList = new ArrayList<>();

        if (a.equals("[]")) return integerList;

        for (String token : tokens) {
            integerList.add(Integer.parseInt(token.trim()));
        }

        return integerList;
    }


    public static boolean comparewithString(Long number1, Long number2, String operator) {

        switch (operator) {
            case ">=":
                return number1 >= number2;
            case ">":
                return number1 > number2;
            case "<":
                return number1 < number2;
            case "<=":
                return number1 <= number2;
            case "==":
                return number1 == number2;
            case "!=":
                return number1 != number2;
            default:
                throw new IllegalArgumentException("Operator '" + operator + "' unvaliable!");
        }

    }

    public static boolean removeItem(Player player, ItemStack item) {
        Inventory inventory = player.getInventory();
        int amountToRemove = item.getAmount();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack currentItem = inventory.getItem(i);

            if (currentItem != null && currentItem.isSimilar(item)) {
                int currentAmount = currentItem.getAmount();

                if (currentAmount >= amountToRemove) {
                    currentItem.setAmount(currentAmount - amountToRemove);
                    inventory.setItem(i, currentItem);
                    return true;
                } else {
                    amountToRemove -= currentAmount;
                    inventory.clear(i);
                }
            }
        }

        return false;
    }

}
