package com.lfstudios.autotools.commands;

import com.lfstudios.autotools.LFAutoTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class LFAutoAdminCommand implements CommandExecutor {
    private final LFAutoTools plugin;

    public LFAutoAdminCommand(LFAutoTools plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("lfautotools.reload")) return true;
            plugin.reloadPlugin();
            sender.sendMessage("§aLF-AutoTools reloaded.");
            return true;
        }
        return false;
    }
}
