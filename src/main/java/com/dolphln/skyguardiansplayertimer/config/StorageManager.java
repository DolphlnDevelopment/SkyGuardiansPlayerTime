package com.dolphln.skyguardiansplayertimer.config;

import com.dolphln.skyguardiansplayertimer.SkyGuardiansPlayerTimer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import sun.nio.ch.FileKey;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class StorageManager {

    private SkyGuardiansPlayerTimer plugin;

    private File dataFolder;

    private HashMap<UUID, YamlConfiguration> dataCache = new HashMap<>();
    //private ArrayList<UUID> configToDelete = new ArrayList<>();

    public StorageManager(SkyGuardiansPlayerTimer plugin) {
        this.plugin = plugin;
        setup();

        new BukkitRunnable() {
            @Override
            public void run() {
                saveData();
            }
        }.runTaskTimerAsynchronously(plugin, 30*20L, 60*20L);
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.dataFolder = new File(plugin.getDataFolder().getPath() + "/players");

        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdir();
        }

        for (File file : this.dataFolder.listFiles()) {
            this.addCacheConfig(UUID.fromString(file.getName().replaceAll(".yml", "")));
        }
    }

    public void saveData() {
        for (UUID uuid : dataCache.keySet()) {
            saveData(uuid, dataCache.get(uuid));
            /*if (configToDelete.contains(uuid)) {
                dataCache.remove(uuid);
                configToDelete.remove(uuid);
            }*/
        }
    }

    public File createPlayerFile(UUID uuid, String playerName) {
        File dataFile = getFile(uuid);
        if (dataFile.exists()) {
            return null;
        }

        try {
            dataFile.createNewFile();

            YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);

            config.set("name", playerName);
            config.set("uuid", uuid.toString());
            config.set("time", (long) 0);
            config.set("cooldown", (long) 0);
            config.set("points", (int) 0);

            config.save(dataFile);
        } catch (Exception e) {e.printStackTrace();}

        return dataFile;
    }

    public File getFile(UUID uuid) {
        return new File(this.dataFolder, uuid.toString() + ".yml");
    }

    public void saveData(UUID uuid, YamlConfiguration config) {
        File dataFile = getFile(uuid);
        try {
            if (dataFile.exists()) {
                config.save(dataFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateCacheConfig(uuid, config);
    }

    public void addTime(UUID uuid, long seconds) {
        YamlConfiguration config = getCacheConfig(uuid);
        config.set("time", config.getDouble("time") + seconds);
        updateCacheConfig(uuid, config);
    }

    public long getTime(UUID uuid) {
        return getCacheConfig(uuid).getLong("time");
    }

    public void setCooldown(UUID uuid, long timestamp) {
        YamlConfiguration config = getCacheConfig(uuid);
        config.set("cooldown", timestamp);
        updateCacheConfig(uuid, config);
    }

    public long getCooldown(UUID uuid) {
        return getCacheConfig(uuid).getLong("cooldown");
    }

    public void addPoints(UUID uuid, int points) {
        YamlConfiguration config = getCacheConfig(uuid);
        config.set("points", config.getDouble("points") + points);
        updateCacheConfig(uuid, config);
    }

    public void setPoints(UUID uuid, int points) {
        YamlConfiguration config = getCacheConfig(uuid);
        config.set("points", points);
        updateCacheConfig(uuid, config);
    }

    public int getPoints(UUID uuid) {
        return getCacheConfig(uuid).getInt("points");
    }

    public void addCacheConfig(UUID uuid) {
        File dataFile = getFile(uuid);
        this.dataCache.put(uuid, YamlConfiguration.loadConfiguration(dataFile));
    }

    public void deleteCacheConfig(UUID uuid, Boolean save) {
        if (save) {
            saveData(uuid, this.dataCache.get(uuid));
        }
        this.dataCache.remove(uuid);
    }

    public void updateCacheConfig(UUID uuid, YamlConfiguration config) {
        this.dataCache.put(uuid, config);
    }

    public YamlConfiguration getCacheConfig(UUID uuid) {
        return this.dataCache.get(uuid);
    }

    public YamlConfiguration getCacheConfig(String playerName) {
        for (YamlConfiguration config : this.dataCache.values()) {
            if (config.getString("name").equalsIgnoreCase(playerName)) {
                return config;
            }
        }
        return null;
    }

    /*public void addRemoveQueue(UUID uuid) {
        configToDelete.add(uuid);
    }*/

    public void removePlayer(UUID uuid) {
        saveData(uuid, getCacheConfig(uuid));
        //dataCache.remove(uuid);
    }

}
