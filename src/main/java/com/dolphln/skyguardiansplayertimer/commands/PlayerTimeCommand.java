package com.dolphln.skyguardiansplayertimer.commands;

import com.dolphln.skyguardiansplayertimer.SkyGuardiansPlayerTimer;
import com.dolphln.skyguardiansplayertimer.gui.ShopGUI;
import com.dolphln.skyguardiansplayertimer.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerTimeCommand implements CommandExecutor {

    private SkyGuardiansPlayerTimer plugin;

    public PlayerTimeCommand(SkyGuardiansPlayerTimer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length > 0) {

                switch (args[0].toLowerCase()) {
                    case "claim":
                        long cooldown = (plugin.getConfigFile().getConfig().getInt("cooldown")*60L)+plugin.getStorageManager().getCooldown(p.getUniqueId());
                        if (cooldown > plugin.getPlayerTimer().getSeconds(p.getUniqueId())) {
                            long currentCooldownTime = plugin.getPlayerTimer().getSeconds(p.getUniqueId())-plugin.getStorageManager().getCooldown(p.getUniqueId());
                            //CustomTimestamp remainingCooldown = new CustomTimestamp((currentCooldownTime-plugin.getConfigFile().getConfig().getInt("cooldown")*60L)*-1);
                            p.sendMessage(
                                    ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getMessage("cooldown_message"))
                                            .replace("%time%", TimeUtils.formatTime((currentCooldownTime-plugin.getConfigFile().getConfig().getInt("cooldown")*60L)*-1))
                                            .replace("%player%", p.getName())
                            );
                            break;
                        }

                        plugin.getStorageManager().addPoints(p.getUniqueId(), 1);
                        p.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getMessage("point_claimed"))
                                .replaceAll("%player%", p.getName()).replaceAll("%time%", String.valueOf(plugin.getConfigFile().getConfig().getLong("cooldown")))
                        );
                        plugin.getStorageManager().setCooldown(p.getUniqueId(), plugin.getPlayerTimer().getSeconds(p.getUniqueId()));
                        break;
                    case "shop":
                        new ShopGUI(p);
                        break;
                    case "points":
                        p.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', plugin.getConfigFile().getMessage("points"))
                                .replaceAll("%player%", p.getName()).replaceAll("%points%", String.valueOf(plugin.getStorageManager().getPoints(p.getUniqueId())))
                        );
                        break;
                    case "rl":
                    case "reload":
                        if (!p.hasPermission("playertimer.reload")) {
                            p.sendMessage(ChatColor.RED + "You are missing PLAYERTIMER.RELOAD permissions.");
                        }

                        plugin.getConfigFile().setup();
                        break;
                    case "help":
                        String help_message = "Subcommands for playtime:\n\n/pt - Shows your play time\n/pt claim - Claim a point every " + String.valueOf(plugin.getConfigFile().getConfig().getInt("cooldown")) + " minutes\n/pt points - Take a look at your actual points\n/pt shop - Opens the points shop\n/pt lookup <name> - Look up for a player's name";
                        p.sendMessage(help_message);
                        break;
                    case "lookup":
                        if (!p.hasPermission("playertimer.lookup")) {
                            p.sendMessage(ChatColor.RED + "You are missing PLAYERTIMER.LOOKUP permissions.");
                        }

                        if (args.length != 2) {
                            p.sendMessage(ChatColor.RED + "You need to specify the player's name!");
                            break;
                        }

                        YamlConfiguration playerData = plugin.getStorageManager().getCacheConfig(args[1]);
                        if (playerData == null) {
                            p.sendMessage(ChatColor.RED + "The specified player is not online or doesn't exist");
                            break;
                        }

                        UUID uuid = UUID.fromString(playerData.getString("uuid"));

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&a" + playerData.getString("name") + " has been playing for %time%."
                                        .replaceAll(
                                                "%time%", TimeUtils.formatTime(plugin.getPlayerTimer().getSeconds(uuid)))));
                        break;
                    default:
                        if (!p.hasPermission("playertimer.lookup")) {
                            p.sendMessage(ChatColor.RED + "You are missing PLAYERTIMER.LOOKUP permissions.");
                        }

                        if (args.length != 1) {
                            p.sendMessage(ChatColor.RED + "You need to specify the player's name!");
                            break;
                        }

                        YamlConfiguration playerDataDefault = plugin.getStorageManager().getCacheConfig(args[0]);
                        if (playerDataDefault == null) {
                            p.sendMessage(ChatColor.RED + "The specified player is not online or doesn't exist");
                            break;
                        }

                        UUID playerUUID = UUID.fromString(playerDataDefault.getString("uuid"));

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&a" + playerDataDefault.getString("name") + " has been playing for %time%."
                                        .replaceAll(
                                                "%time%", TimeUtils.formatTime(plugin.getPlayerTimer().getSeconds(playerUUID)))));
                }

            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfigFile().getMessage("player_time")
                                .replaceAll(
                                        "%time%",
                                        TimeUtils.formatTime(plugin.getPlayerTimer().getSeconds(p.getUniqueId())).replaceAll("%player%", p.getName()))));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must execute this command ingame!");
        }

        return false;
    }

}
