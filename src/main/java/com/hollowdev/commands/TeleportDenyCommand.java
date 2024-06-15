package com.hollowdev.commands;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeleportDenyCommand implements CommandExecutor {
    private final SurvivalPlugin plugin;
    private final TeleportRequestCommand teleportRequestCommand;

    public TeleportDenyCommand(SurvivalPlugin plugin, TeleportRequestCommand teleportRequestCommand) {
        this.plugin = plugin;
        this.teleportRequestCommand = teleportRequestCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can deny teleport requests.");
            return true;
        }

        Player target = (Player) sender;
        UUID requesterUUID = teleportRequestCommand.getRequester(target.getUniqueId());
        if (requesterUUID == null) {
            target.sendMessage("No teleport requests to deny.");
            return true;
        }

        Player requester = Bukkit.getPlayer(requesterUUID);
        if (requester != null) {
            requester.sendMessage(target.getName() + " has denied your teleport request.");
        }
        target.sendMessage("Teleport request denied.");
        teleportRequestCommand.removeRequest(target.getUniqueId());
        return true;
    }
}