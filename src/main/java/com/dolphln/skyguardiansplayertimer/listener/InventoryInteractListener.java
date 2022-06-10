package com.dolphln.skyguardiansplayertimer.listener;

import com.dolphln.skyguardiansplayertimer.SkyGuardiansPlayerTimer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryInteractListener implements Listener {

    private final SkyGuardiansPlayerTimer plugin;

    public InventoryInteractListener(SkyGuardiansPlayerTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();

        if (item == null || item.getType() == Material.AIR || p == null) { return; }

        NBTItem nbti = new NBTItem(item);

        if (nbti.hasKey("PlayerTimer")) {
            e.setCancelled(true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (nbti.getString("PlayerTimer").equalsIgnoreCase("NOTHING")) {
                        return;
                    }
                    YamlConfiguration config = plugin.getConfigFile().getConfig();

                    String path = "shop." + nbti.getString("PlayerTimer");
                    int points = plugin.getStorageManager().getPoints(p.getUniqueId());
                    if (points < config.getInt(path + ".price")) {
                        p.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getMessage("not_enough_points"))
                                        .replaceAll("%player%", p.getName()).replaceAll("%points%", String.valueOf(points))
                        );
                        return;
                    }
                    p.closeInventory();
                    points -= config.getInt(path + ".price");
                    plugin.getStorageManager().setPoints(p.getUniqueId(), points);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            plugin.runCommand(config.getString(path + ".command"), p);
                        }
                    }.runTask(plugin);
                    p.sendMessage(
                            ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getMessage("item_bought"))
                                    .replaceAll("%player%", p.getName()).replaceAll("%points%", String.valueOf(points))
                    );
                }
            }.runTaskAsynchronously(plugin);
        }
    }
}
