package com.dolphln.skyguardiansplayertimer.listener;

import com.dolphln.skyguardiansplayertimer.SkyGuardiansPlayerTimer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {

    private SkyGuardiansPlayerTimer plugin;

    public PlayerJoinListener(SkyGuardiansPlayerTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (plugin.getStorageManager().createPlayerFile(p.getUniqueId(), p.getDisplayName()) != null) {
            plugin.getStorageManager().addCacheConfig(p.getUniqueId());
        }
        plugin.getPlayerTimer().addPlayer(p);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        plugin.getPlayerTimer().removePlayer(p);
        //plugin.getStorageManager().removePlayer(p.getUniqueId());
    }
}
