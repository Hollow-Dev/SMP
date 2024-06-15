package com.hollowdev;

import com.hollowdev.SurvivalPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathTracker implements Listener {
    private final SurvivalPlugin plugin;

    public DeathTracker(SurvivalPlugin plugin) {
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

        // Send death coordinates to player
        player.sendMessage("You died at: " + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ());
    }
}
