package com.hollowdev;

import com.hollowdev.commands.*;
import com.hollowdev.DeathCounter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class SurvivalPlugin extends JavaPlugin {
    private Scoreboard scoreboard;
    private TeleportRequestCommand teleportRequestCommand;

    @Override
    public void onEnable() {
        getLogger().info("SMP has Loaded!");
        saveDefaultConfig();

        // Initialize scoreboard
        setupScoreboard();

        // Register commands
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("delhome").setExecutor(new DelHomeCommand(this));
        getCommand("listhomes").setExecutor(new ListHomesCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));

        // Initialize teleport request command
        teleportRequestCommand = new TeleportRequestCommand(this);
        getCommand("tpa").setExecutor(teleportRequestCommand);
        getCommand("tpaccept").setExecutor(new TeleportAcceptCommand(this, teleportRequestCommand));
        getCommand("tpdeny").setExecutor(new TeleportDenyCommand(this, teleportRequestCommand));

        // Register events
        getServer().getPluginManager().registerEvents(new DeathCounter(this), this);
        getServer().getPluginManager().registerEvents(new DeathTracker(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("SMP has been disabled!");
    }

    private void setupScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("SMP", "dummy", "SMP");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Update scoreboard for all online players
        Bukkit.getOnlinePlayers().forEach(this::updateScoreboardForPlayer);
    }

    public void updateScoreboardForPlayer(org.bukkit.entity.Player player) {
        player.setScoreboard(scoreboard);
    }

    public void updateDeathCount(org.bukkit.entity.Player player) {
        int deaths = getConfig().getInt("deaths." + player.getUniqueId(), 0);
        scoreboard.getObjective("SMP").getScore(player.getName()).setScore(deaths);
    }
}