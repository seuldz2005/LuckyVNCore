package com.seul.luckyvncore.Functions;

import com.cryptomorin.xseries.XMaterial;
import com.seul.luckyvncore.LKUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LKItems {


    private ItemStack item;
    private List<String> lores;
    private String name;
    public LKItems(String material, String name, List<String> lores) {

        item = material.toUpperCase().equals("headbase") ? getHeadBase(material) : XMaterial.valueOf(material).parseItem();

        this.name = name == null ? null : name;
        this.lores = (lores == null) ? new ArrayList<>() : lores;
        setLores(this.lores);
        setName(this.name);
    }

    public ItemStack getItem() {
        return item;
    }

    public List<String> getLores() {
        return lores;
    }

    public void setName(String name) {
        if (name == null) return;
        this.name = name;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(LKUtils.color(name));
        item.setItemMeta(meta);
    }

    public void setLores(List<String> lores) {
        ItemMeta meta = item.getItemMeta();
        this.lores = lores.stream().map(s -> LKUtils.color(s)).collect(Collectors.toList());
        meta.setLore(this.lores);
        item.setItemMeta(meta);
    }

    public void replaceLores(String oldchar, String newchar) {
        setLores(lores.stream().map(s -> s.replace(oldchar, newchar)).collect(Collectors.toList()));

    }

    private ItemStack getHeadBase(String material) {
        URL url;
        try {
            url = getUrlFromBase64(material.replace("headbase-", ""));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        PlayerProfile profile = getProfile(url.toString());
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    private URL getUrlFromBase64(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }

    private final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");
    private PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

}
