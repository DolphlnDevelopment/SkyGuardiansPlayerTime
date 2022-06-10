package com.dolphln.skyguardiansplayertimer;

import com.dolphln.skyguardiansplayertimer.commands.PlayerTimeCommand;
import com.dolphln.skyguardiansplayertimer.config.ConfigFile;
import com.dolphln.skyguardiansplayertimer.config.StorageManager;
import com.dolphln.skyguardiansplayertimer.core.PlayerTimer;
import com.dolphln.skyguardiansplayertimer.listener.InventoryInteractListener;
import com.dolphln.skyguardiansplayertimer.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyGuardiansPlayerTimer extends JavaPlugin {

    private static SkyGuardiansPlayerTimer instance;

    private ConfigFile configFile;
    private StorageManager storageManager;
    private PlayerTimer playerTimer;

    @Override
    public void onEnable() {
        instance = this;

        this.configFile = new ConfigFile(this);
        this.storageManager = new StorageManager(this);
        this.playerTimer = new PlayerTimer(this);

        getCommand("playtime").setExecutor(new PlayerTimeCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryInteractListener(this), this);

        for (Player p : Bukkit.getOnlinePlayers()) {
            getStorageManager().addCacheConfig(p.getUniqueId());
            getPlayerTimer().addPlayer(p);
        }
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            getPlayerTimer().removePlayer(p);
            getStorageManager().removePlayer(p.getUniqueId());
        }
    }

    public void runCommand(String cmd, Player p) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", p.getName()));
    }

    public static SkyGuardiansPlayerTimer getInstance() {
        return instance;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public PlayerTimer getPlayerTimer() {
        return playerTimer;
    }
}
