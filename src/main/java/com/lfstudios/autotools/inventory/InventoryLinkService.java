package com.lfstudios.autotools.inventory;

import com.lfstudios.autotools.api.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class InventoryLinkService {
    public void link(PlayerData data, Block target) {
        Location location = target.getLocation();
        data.setLinkedInventory(location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ());
    }

    public void unlink(PlayerData data) {
        data.setLinkedInventory("");
    }

    public Inventory resolveInventory(PlayerData data) {
        if (data.getLinkedInventory().isEmpty()) return null;
        String[] split = data.getLinkedInventory().split(":");
        if (split.length != 4) return null;
        Location location = new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
        if (location.getWorld() == null) return null;
        Block block = location.getBlock();
        if (!(block.getState() instanceof Container container)) return null;
        return container.getInventory();
    }

    public boolean canLink(Player player, Block target) {
        return target.getState() instanceof Container && target.getWorld().equals(player.getWorld());
    }
}
