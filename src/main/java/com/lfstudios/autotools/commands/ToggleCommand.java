package com.lfstudios.autotools.commands;

import com.lfstudios.autotools.LFAutoTools;
import com.lfstudios.autotools.api.PlayerFeature;
import com.lfstudios.autotools.core.Module;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ToggleCommand implements CommandExecutor {
    private final LFAutoTools plugin;
    private final PlayerFeature feature;
    private final Module module;
    private final String perm;

    public ToggleCommand(LFAutoTools plugin, PlayerFeature feature, Module module, String perm) {
        this.plugin = plugin;
        this.feature = feature;
        this.module = module;
        this.perm = perm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (!player.hasPermission(perm)) return true;
        if (!plugin.getModuleManager().isEnabled(module)) {
            player.sendMessage("§cThis module is disabled by configuration.");
            return true;
        }
        boolean enabled = plugin.getPlayerDataService().get(player.getUniqueId()).isEnabled(feature);
        plugin.getPlayerDataService().get(player.getUniqueId()).setEnabled(feature, !enabled);
        player.sendMessage("§e" + command.getName() + ": §f" + (!enabled ? "enabled" : "disabled"));
        return true;
    }
}
