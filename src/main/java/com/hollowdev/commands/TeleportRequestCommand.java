package com.hollowdev.commands;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TeleportRequestCommand implements CommandExecutor {
    private final SurvivalPlugin plugin;
    private final HashMap<UUID, UUID> teleportRequests;

    public TeleportRequestCommand(SurvivalPlugin plugin) {
        this.plugin = plugin;
        this.teleportRequests = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can send teleport requests.");
            return true;
        }

        Player requester = (Player) sender;
        if (args.length != 1) {
            requester.sendMessage("Usage: /tpa <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            requester.sendMessage("Player not found.");
            return true;
        }

        teleportRequests.put(target.getUniqueId(), requester.getUniqueId());
        target.sendMessage(requester.getName() + " has requested to teleport to you. Use /tpaccept or /tpdeny.");
        requester.sendMessage("Teleport request sent to " + target.getName() + ".");
        return true;
    }

    public UUID getRequester(UUID targetUUID) {
        return teleportRequests.get(targetUUID);
    }

    public void removeRequest(UUID targetUUID) {
        teleportRequests.remove(targetUUID);
    }
}