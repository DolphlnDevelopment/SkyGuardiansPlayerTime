package com.dolphln.skyguardiansplayertimer.config;

import com.dolphln.skyguardiansplayertimer.SkyGuardiansPlayerTimer;
import com.dolphln.skyguardiansplayertimer.utils.DoubleUtils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ConfigFile {

    private final SkyGuardiansPlayerTimer plugin;

    private YamlConfiguration config;
    private File configFile;

    public ConfigFile(SkyGuardiansPlayerTimer plugin) {
        this.plugin = plugin;
        setup();
    }

    public void setup() {

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                plugin.saveResource("config.yml", true);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create config.yml file.");
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        plugin.getLogger().log(Level.FINE, "File config.yml loaded correctly.");

    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return configFile;
    }

    public String getMessage(String messageName) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + messageName));
    }

    public ItemStack getItem(Player p, String path, String NBT) {
        YamlConfiguration config = plugin.getConfigFile().getConfig();

        ItemStack item = new ItemStack(Material.getMaterial(config.getString(path + ".material")), 1, (short) config.getInt(path + ".data"));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(path + ".name")));
        meta.setLore(generateLore(p, config.getStringList(path + ".lore"), config.getDouble(path + ".price")));

        item.setItemMeta(meta);

        NBTItem nbti = new NBTItem(item);

        nbti.setString("PlayerTimer", NBT);
        return nbti.getItem();
    }

    private List<String> generateLore(Player p, List<String> lore, double price) {
        List<String> newLore = new ArrayList<>();

        for (String loreline : lore) {
            String line = loreline
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%price%", DoubleUtils.removeExtra(price));

            newLore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return newLore;
    }

}
