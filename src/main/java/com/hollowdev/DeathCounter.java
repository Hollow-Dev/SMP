package com.hollowdev;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.*;

public class DeathCounter implements Listener {
    private final SurvivalPlugin plugin;

    public DeathCounter(SurvivalPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        org.bukkit.entity.Player player = event.getEntity();
        String path = "deaths." + player.getUniqueId();

        int deaths = plugin.getConfig().getInt(path, 0) + 1;
        plugin.getConfig().set(path, deaths);
        plugin.saveConfig();

        plugin.updateDeathCount(player);
    }
}