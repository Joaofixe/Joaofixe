package com.lfstudios.autotools.commands;

import com.lfstudios.autotools.LFAutoTools;
import com.lfstudios.autotools.core.Module;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AutoGuiCommand implements CommandExecutor {
    private final LFAutoTools plugin;

    public AutoGuiCommand(LFAutoTools plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (!plugin.getModuleManager().isEnabled(Module.AUTOGUI)) {
            player.sendMessage("§cAutoGUI is disabled.");
            return true;
        }
        plugin.getAutoGuiManager().open(player);
        return true;
    }
}
