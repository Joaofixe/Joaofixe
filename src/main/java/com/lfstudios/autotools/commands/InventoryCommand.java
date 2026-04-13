package com.lfstudios.autotools.commands;

import com.lfstudios.autotools.LFAutoTools;
import com.lfstudios.autotools.api.PlayerData;
import com.lfstudios.autotools.core.Module;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public final class InventoryCommand implements CommandExecutor {
    private final LFAutoTools plugin;

    public InventoryCommand(LFAutoTools plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        PlayerData data = plugin.getPlayerDataService().get(player.getUniqueId());
        if (args.length == 0) return false;
        switch (args[0].toLowerCase()) {
            case "link" -> {
                if (!plugin.getModuleManager().isEnabled(Module.INVENTORY_LINKING)) return true;
                var target = player.getTargetBlockExact(6);
                if (target == null || !plugin.getInventoryLinkService().canLink(player, target)) {
                    player.sendMessage("§cLook at a valid container.");
                    return true;
                }
                plugin.getInventoryLinkService().link(data, target);
                player.sendMessage("§aInventory linked.");
            }
            case "unlink" -> {
                plugin.getInventoryLinkService().unlink(data);
                player.sendMessage("§eInventory unlinked.");
            }
            case "push" -> {
                if (!plugin.getModuleManager().isEnabled(Module.INVENTORY_PUSH)) return true;
                Inventory inv = plugin.getInventoryLinkService().resolveInventory(data);
                if (inv == null) return true;
                int moved = plugin.getInventoryTransferService().push(player, inv);
                player.sendMessage("§aPushed " + moved + " items.");
            }
            case "pull" -> {
                if (!plugin.getModuleManager().isEnabled(Module.INVENTORY_PULL)) return true;
                Inventory inv = plugin.getInventoryLinkService().resolveInventory(data);
                if (inv == null) return true;
                int moved = plugin.getInventoryTransferService().pull(player, inv);
                player.sendMessage("§aPulled " + moved + " items.");
            }
            case "autopush" -> {
                if (!plugin.getModuleManager().isEnabled(Module.INVENTORY_AUTOPUSH)) return true;
                boolean current = data.isEnabled(com.lfstudios.autotools.api.PlayerFeature.AUTOPUSH);
                data.setEnabled(com.lfstudios.autotools.api.PlayerFeature.AUTOPUSH, !current);
                player.sendMessage("§eAutoPush: " + (!current ? "enabled" : "disabled"));
            }
            case "filter" -> handleFilter(player, data, Arrays.copyOfRange(args, 1, args.length));
            default -> {
                return false;
            }
        }
        return true;
    }

    private void handleFilter(Player player, PlayerData data, String[] args) {
        if (!plugin.getModuleManager().isEnabled(Module.FILTERS) || args.length == 0) return;
        switch (args[0]) {
            case "create" -> {
                if (args.length < 3) return;
                plugin.getFilterService().create(data, args[1], args[2]);
                data.setActiveFilter(args[1]);
                player.sendMessage("§aFilter created: " + args[1]);
            }
            case "edit" -> {
                if (args.length < 4) return;
                String name = args[1];
                String mode = args[2];
                String value = args[3];
                String current = data.getFilters().getOrDefault(name, "");
                if (mode.equals("set")) data.getFilters().put(name, value);
                if (mode.equals("add")) data.getFilters().put(name, current + "," + value);
                if (mode.equals("remove")) data.getFilters().put(name, current.replace(value, ""));
                player.sendMessage("§eFilter updated.");
            }
            case "delete" -> {
                if (args.length < 2) return;
                plugin.getFilterService().delete(data, args[1]);
                player.sendMessage("§cFilter deleted.");
            }
            case "list" -> player.sendMessage("§7Filters: " + String.join(", ", data.getFilters().keySet()));
        }
    }
}
