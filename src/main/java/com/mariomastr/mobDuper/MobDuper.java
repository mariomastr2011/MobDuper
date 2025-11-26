package com.mariomastr.mobDuper;

import org.bukkit.plugin.java.JavaPlugin;

public final class MobDuper extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new MobSpawnListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
