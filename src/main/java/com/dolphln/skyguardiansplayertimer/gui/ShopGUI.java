package com.dolphln.skyguardiansplayertimer.gui;

import com.dolphln.skyguardiansplayertimer.SkyGuardiansPlayerTimer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopGUI {

    private SkyGuardiansPlayerTimer plugin;
    private Player p;

    //int updatedPoints = Math.max((config.getInt("points") - points), 0);

    public ShopGUI(Player p) {
        this.plugin = SkyGuardiansPlayerTimer.getInstance();
        this.p = p;

        p.openInventory(GUIBuilder());
    }

    private Inventory GUIBuilder() {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getConfig().getString("shop.title")));

        YamlConfiguration config = plugin.getConfigFile().getConfig();
        String path = "gui.placeholder_item";

        ItemStack placeholder = new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short) 7);
        ItemMeta meta = placeholder.getItemMeta();

        meta.setDisplayName(ChatColor.BLACK.toString() + " ");

        placeholder.setItemMeta(meta);

        NBTItem nbti = new NBTItem(placeholder);

        nbti.setString("PlayerTimer", "NOTHING");

        for (int i = 0; i < 27; i++) {
            gui.setItem(i, nbti.getItem());
        }

        int startSlot = 11;

        for (String option : plugin.getConfigFile().getConfig().getConfigurationSection("shop").getKeys(false)) {
            if (!option.contains("title")) {
                ItemStack item = plugin.getConfigFile().getItem(p, "shop." + option, option);
                gui.setItem(startSlot, item);
                startSlot++;
            }
        }

        return gui;
    }

}
