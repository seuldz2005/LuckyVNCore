package com.seul.luckyvncore;

import me.clip.placeholderapi.PlaceholderAPI;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LKUtils {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    private static final Map<Type, Map<String, ItemStack>> mmoItemCache = new ConcurrentHashMap<>();

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


        Pattern pattern = Pattern.compile("\\{random=(\\d+)-(\\d+)}");
        Matcher matcher = pattern.matcher(command);

        if (matcher.find()) {
            int random = new Random().nextInt(Integer.parseInt(matcher.group(2))) + Integer.parseInt(matcher.group(1));
            command = command.replaceAll("\\{random=(\\d+)-(\\d+)}", random + "");

        }

        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                command = PlaceholderAPI.setPlaceholders(((Player) player), command);
            }
        }

        String finalCommand = command;
        (new BukkitRunnable() {
            @Override
            public void run() {
                if (finalCommand.contains("[console]")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand.replace("[console] ", ""));
                    return;
                }
                if (finalCommand.contains("[player]")) {
                    Player player = (Player)sender;
                    Bukkit.dispatchCommand(sender, finalCommand.replace("[player] ", "").replace("%player%", player.getName()));
                    return;
                }
                if (finalCommand.contains("[message]")) {
                    LKUtils.sendMessage(sender, finalCommand.replace("[message] ", ""));
                }
            }
        }).runTask(LuckyVNCore.plugin);



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

    public static ItemStack computeMMOItem(Type type, String id) {
        return mmoItemCache.computeIfAbsent(type, t -> new ConcurrentHashMap<>())
                .computeIfAbsent(id, key -> MMOItems.plugin.getItem(type, id));
    }

    public static ItemStack getItems(String[] material) {
        switch (material[0].toUpperCase()) {
            case "MMOITEMS":
                return computeMMOItem(Type.get(material[1]), material[2]);
            case "MATERIAL":
                return new ItemStack(Material.valueOf(material[1]));
            default:
                return new ItemStack(Material.STONE);
        }
    }

    public static boolean isMMOItem(ItemStack itemStack) {
        if(itemStack == null || itemStack.getType() == Material.AIR)
            return false;
        return Type.get(itemStack) != null;
    }

    public static String toSmallCaps(String text) {
        StringBuilder smallCapsText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                switch (c) {
                    case 'A': smallCapsText.append('\u1D00'); break; // ᴀ
                    case 'B': smallCapsText.append('\u0299'); break; // ʙ
                    case 'C': smallCapsText.append('\u1D04'); break; // ᴄ
                    case 'D': smallCapsText.append('\u1D05'); break; // ᴅ
                    case 'E': smallCapsText.append('\u1D07'); break; // ᴇ
                    case 'F': smallCapsText.append('\uA730'); break; // ꜰ (Latin letter small capital F)
                    case 'G': smallCapsText.append('\u0262'); break; // ɢ
                    case 'H': smallCapsText.append('\u029C'); break; // ʜ
                    case 'I': smallCapsText.append('\u1D09'); break; // ɪ
                    case 'J': smallCapsText.append('\u1D0A'); break; // ᴊ
                    case 'K': smallCapsText.append('\u1D0B'); break; // ᴋ
                    case 'L': smallCapsText.append('\u029F'); break; // ʟ
                    case 'M': smallCapsText.append('\u1D0D'); break; // ᴍ
                    case 'N': smallCapsText.append('\u0274'); break; // ɴ
                    case 'O': smallCapsText.append('\u1D0F'); break; // ᴏ
                    case 'P': smallCapsText.append('\u1D18'); break; // ᴘ
                    case 'Q': smallCapsText.append('Q'); break; // No direct small capital for Q in common blocks, can use regular Q or look for alternatives if specific need
                    case 'R': smallCapsText.append('\u0280'); break; // ʀ
                    case 'S': smallCapsText.append('s'); break; // No direct small capital for S in common blocks
                    case 'T': smallCapsText.append('\u1D1B'); break; // ᴛ
                    case 'U': smallCapsText.append('\u1D1C'); break; // ᴜ
                    case 'V': smallCapsText.append('\u1D20'); break; // ᴠ
                    case 'W': smallCapsText.append('\u1D21'); break; // ᴡ
                    case 'X': smallCapsText.append('\u0078'); break; // ˣ
                    case 'Y': smallCapsText.append('\u028F'); break; // ʏ
                    case 'Z': smallCapsText.append('\u1D22'); break; // ᴢ
                    default: smallCapsText.append(c); break; // Append character as is if no mapping
                }
            } else if (c >= 'a' && c <= 'z') {
                // Convert lowercase letters to their small capital Unicode equivalents (often same as uppercase small caps)
                switch (c) {
                    case 'a': smallCapsText.append('\u1D00'); break; // ᴀ
                    case 'b': smallCapsText.append('\u0299'); break; // ʙ
                    case 'c': smallCapsText.append('\u1D04'); break; // ᴄ
                    case 'd': smallCapsText.append('\u1D05'); break; // ᴅ
                    case 'e': smallCapsText.append('\u1D07'); break; // ᴇ
                    case 'f': smallCapsText.append('\uA730'); break; // ꜰ
                    case 'g': smallCapsText.append('\u0262'); break; // ɢ
                    case 'h': smallCapsText.append('\u029C'); break; // ʜ
                    case 'i': smallCapsText.append('\u1D09'); break; // ɪ
                    case 'j': smallCapsText.append('\u1D0A'); break; // ᴊ
                    case 'k': smallCapsText.append('\u1D0B'); break; // ᴋ
                    case 'l': smallCapsText.append('\u029F'); break; // ʟ
                    case 'm': smallCapsText.append('\u1D0D'); break; // ᴍ
                    case 'n': smallCapsText.append('\u0274'); break; // ɴ
                    case 'o': smallCapsText.append('\u1D0F'); break; // ᴏ
                    case 'p': smallCapsText.append('\u1D18'); break; // ᴘ
                    case 'q': smallCapsText.append('q'); break; // No direct small capital for Q in common blocks
                    case 'r': smallCapsText.append('\u0280'); break; // ʀ
                    case 's': smallCapsText.append('s'); break; // No direct small capital for S in common blocks
                    case 't': smallCapsText.append('\u1D1B'); break; // ᴛ
                    case 'u': smallCapsText.append('\u1D1C'); break; // ᴜ
                    case 'v': smallCapsText.append('\u1D20'); break; // ᴠ
                    case 'w': smallCapsText.append('\u1D21'); break; // ᴡ
                    case 'x': smallCapsText.append('\u0078'); break; // ˣ
                    case 'y': smallCapsText.append('\u028F'); break; // ʏ
                    case 'z': smallCapsText.append('\u1D22'); break; // ᴢ
                    default: smallCapsText.append(c); break; // Append character as is
                }
            } else {
                smallCapsText.append(c); // Append non-alphabetic characters as is (e.g., '.', '/')
            }
        }
        return smallCapsText.toString();
    }

}


