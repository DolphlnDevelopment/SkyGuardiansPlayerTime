package com.dolphln.skyguardiansplayertimer.core;

import com.dolphln.skyguardiansplayertimer.SkyGuardiansPlayerTimer;
import com.dolphln.skyguardiansplayertimer.config.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerTimer {

    private SkyGuardiansPlayerTimer plugin;
    private StorageManager storageManager;

    private HashMap<UUID, CustomTimestamp> playerJoinTime;

    private BukkitRunnable counter = new BukkitRunnable() {
        @Override
        public void run() {
            for (UUID player : playerJoinTime.keySet()) {
                plugin.getStorageManager().addTime(player, playerJoinTime.get(player).getDifference());
                playerJoinTime.put(player, new CustomTimestamp());
            }
        }
    };

    public PlayerTimer(SkyGuardiansPlayerTimer plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
        this.playerJoinTime = new HashMap<>();

        counter.runTaskTimerAsynchronously(plugin, 0L, 30*20L);
    }

    public void addPlayer(Player p) {
        addPlayer(p.getUniqueId());
    }

    public void addPlayer(UUID uuid) {
        playerJoinTime.put(uuid, new CustomTimestamp());
    }

    public void removePlayer(Player p) {
        plugin.getStorageManager().addTime(p.getUniqueId(), playerJoinTime.get(p.getUniqueId()).getDifference());
        playerJoinTime.remove(p.getUniqueId());
    }

    public long getSeconds(UUID id) {
        return storageManager.getTime(id) + (playerJoinTime.containsKey(id) ? playerJoinTime.get(id).getDifference() : 0);
    }

}
