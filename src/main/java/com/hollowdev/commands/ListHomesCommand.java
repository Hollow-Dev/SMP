package com.hollowdev.commands;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Set;

public class ListHomesCommand implements CommandExecutor {
    private final SurvivalPlugin plugin;

    public ListHomesCommand(SurvivalPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can list homes.");
            return true;
        }

        Player player = (Player) sender;
        String playerPath = "homes." + player.getUniqueId();

        ConfigurationSection playerHomes = plugin.getConfig().getConfigurationSection(playerPath);
        if (playerHomes == null) {
            player.sendMessage("You have no homes set.");
            return true;
        }

        Set<String> homes = playerHomes.getKeys(false);
        if (homes.isEmpty()) {
            player.sendMessage("You have no homes set.");
        } else {
            player.sendMessage("Your homes: " + String.join(", ", homes));
        }
        return true;
    }
}