package com.hollowdev.commands;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private final SurvivalPlugin plugin;

    public SetHomeCommand(SurvivalPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can set homes.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("Usage: /sethome <name>");
            return true;
        }

        String homeName = args[0];
        String playerPath = "homes." + player.getUniqueId();
        String homePath = playerPath + "." + homeName;

        // Ensure the configuration section exists
        ConfigurationSection playerHomes = plugin.getConfig().getConfigurationSection(playerPath);
        if (playerHomes == null) {
            playerHomes = plugin.getConfig().createSection(playerPath);
        }

        // Check if the player has already set 3 homes
        if (playerHomes.getKeys(false).size() >= 3) {
            player.sendMessage("You can only set 3 homes.");
            return true;
        }

        // Set the home coordinates
        plugin.getConfig().set(homePath + ".world", player.getLocation().getWorld().getName());
        plugin.getConfig().set(homePath + ".x", player.getLocation().getX());
        plugin.getConfig().set(homePath + ".y", player.getLocation().getY());
        plugin.getConfig().set(homePath + ".z", player.getLocation().getZ());
        plugin.saveConfig();

        player.sendMessage("Home " + homeName + " set!");
        return true;
    }
}