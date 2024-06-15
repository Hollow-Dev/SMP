package com.hollowdev;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    private final SurvivalPlugin plugin;

    public PlayerListener(SurvivalPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        plugin.updateScoreboardForPlayer(player);
        plugin.updateDeathCount(player);
    }
}