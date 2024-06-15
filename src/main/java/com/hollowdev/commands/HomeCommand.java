package com.hollowdev.commands;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeCommand implements CommandExecutor {
    private final SurvivalPlugin plugin;

    public HomeCommand(SurvivalPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use homes.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("Usage: /home <name>");
            return true;
        }

        String homeName = args[0];
        String homePath = "homes." + player.getUniqueId() + "." + homeName;

        if (!plugin.getConfig().contains(homePath)) {
            player.sendMessage("Home " + homeName + " does not exist.");
            return true;
        }

        Location homeLocation = new Location(
                Bukkit.getWorld(plugin.getConfig().getString(homePath + ".world")),
                plugin.getConfig().getDouble(homePath + ".x"),
                plugin.getConfig().getDouble(homePath + ".y"),
                plugin.getConfig().getDouble(homePath + ".z")
        );

        player.sendMessage("Stand still for 3 seconds to teleport to your home.");
        new BukkitRunnable() {
            private final Location initialLocation = player.getLocation();
            private int countdown = 3;

            @Override
            public void run() {
                if (countdown == 0) {
                    player.teleport(homeLocation);
                    player.sendMessage("Teleported to home " + homeName + ".");
                    cancel();
                } else if (player.getLocation().distance(initialLocation) > 0.1) {
                    player.sendMessage("You moved! Teleportation cancelled.");
                    cancel();
                } else {
                    countdown--;
                }
            }
        }.runTaskTimer(plugin, 0, 20);

        return true;
    }
}