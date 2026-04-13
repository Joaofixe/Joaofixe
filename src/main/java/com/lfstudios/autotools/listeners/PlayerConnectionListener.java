package com.lfstudios.autotools.listeners;

import com.lfstudios.autotools.LFAutoTools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerConnectionListener implements Listener {
    private final LFAutoTools plugin;

    public PlayerConnectionListener(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPlayerDataService().get(event.getPlayer().getUniqueId());
        if (plugin.getUpdateManager() != null) {
            plugin.getUpdateManager().notifyPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPlayerDataService().unload(event.getPlayer().getUniqueId());
    }
}
