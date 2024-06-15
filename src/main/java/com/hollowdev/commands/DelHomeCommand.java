package com.hollowdev.commands;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {
    private final SurvivalPlugin plugin;

    public DelHomeCommand(SurvivalPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can delete homes.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("Usage: /delhome <name>");
            return true;
        }

        String homeName = args[0];
        String homePath = "homes." + player.getUniqueId() + "." + homeName;

        if (plugin.getConfig().contains(homePath)) {
            plugin.getConfig().set(homePath, null);
            plugin.saveConfig();
            player.sendMessage("Home " + homeName + " deleted!");
        } else {
            player.sendMessage("Home " + homeName + " does not exist.");
        }
        return true;
    }
}