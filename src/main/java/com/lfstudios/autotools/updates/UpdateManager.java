package com.lfstudios.autotools.updates;

import com.lfstudios.autotools.LFAutoTools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class UpdateManager {
    private final LFAutoTools plugin;
    private final UpdateSource source;
    private String latestVersion;
    private int taskId = -1;

    public UpdateManager(LFAutoTools plugin, UpdateSource source) {
        this.plugin = plugin;
        this.source = source;
    }

    public void start() {
        FileConfiguration cfg = plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.UPDATES);
        if (!cfg.getBoolean("update.enabled", true)) return;
        long ticks = cfg.getLong("update.interval-seconds", 1800) * 20L;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::checkAsync, 40L, ticks);
        checkAsync();
    }

    private void checkAsync() {
        source.fetchLatestVersion().thenAccept(version -> {
            latestVersion = version;
            String current = plugin.getDescription().getVersion();
            if (!current.equalsIgnoreCase(version)) {
                plugin.getLogger().warning("A new update is available: " + version + " (current: " + current + ")");
            }
        });
    }

    public void stop() {
        if (taskId != -1) Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
    }

    public void notifyPlayer(Player player) {
        if (latestVersion == null) return;
        if (!player.hasPermission("lfautotools.update.notify")) return;
        String current = plugin.getDescription().getVersion();
        if (!current.equalsIgnoreCase(latestVersion)) {
            player.sendMessage("§e[LF-AutoTools] Update available: §f" + latestVersion + " §7(Current: " + current + ")");
        }
    }
}
