package com.hollowdev.commands;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TeleportAcceptCommand implements CommandExecutor {
    private final SurvivalPlugin plugin;
    private final TeleportRequestCommand teleportRequestCommand;

    public TeleportAcceptCommand(SurvivalPlugin plugin, TeleportRequestCommand teleportRequestCommand) {
        this.plugin = plugin;
        this.teleportRequestCommand = teleportRequestCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can accept teleport requests.");
            return true;
        }

        Player target = (Player) sender;
        UUID requesterUUID = teleportRequestCommand.getRequester(target.getUniqueId());
        if (requesterUUID == null) {
            target.sendMessage("No teleport requests to accept.");
            return true;
        }

        Player requester = Bukkit.getPlayer(requesterUUID);
        if (requester == null) {
            target.sendMessage("Requester is no longer online.");
            teleportRequestCommand.removeRequest(target.getUniqueId());
            return true;
        }

        target.sendMessage("Teleport request accepted. Stay still for 3 seconds.");
        requester.sendMessage("Your teleport request has been accepted. Teleporting in 3 seconds...");

        new BukkitRunnable() {
            private final Location initialLocation = target.getLocation();
            private int countdown = 3;

            @Override
            public void run() {
                if (countdown == 0) {
                    requester.teleport(target.getLocation());
                    requester.sendMessage("Teleported to " + target.getName() + ".");
                    teleportRequestCommand.removeRequest(target.getUniqueId());
                    cancel();
                } else if (target.getLocation().distance(initialLocation) > 0.1) {
                    target.sendMessage("You moved! Teleportation cancelled.");
                    requester.sendMessage(target.getName() + " moved. Teleportation cancelled.");
                    teleportRequestCommand.removeRequest(target.getUniqueId());
                    cancel();
                } else {
                    countdown--;
                }
            }
        }.runTaskTimer(plugin, 0, 20);
        return true;
    }
}
