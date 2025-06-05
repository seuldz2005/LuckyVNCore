package com.seul.luckyvncore;

import com.seul.luckyvncore.Hook.Placeholder;
import org.bukkit.plugin.java.JavaPlugin;

public final class LuckyVNCore extends JavaPlugin {

    @Override
    public void onEnable() {

        Placeholder.instance = new Placeholder();
        Placeholder.instance.register();

    }

    @Override
    public void onDisable() {
        Placeholder.instance.unregister();
    }
}
