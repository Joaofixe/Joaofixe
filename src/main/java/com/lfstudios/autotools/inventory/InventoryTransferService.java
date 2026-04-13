package com.lfstudios.autotools.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryTransferService {
    public int push(Player player, Inventory linked) {
        int moved = 0;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item == null) continue;
            var left = linked.addItem(item);
            if (left.isEmpty()) {
                moved += item.getAmount();
                player.getInventory().removeItem(item);
            }
        }
        return moved;
    }

    public int pull(Player player, Inventory linked) {
        int moved = 0;
        for (ItemStack item : linked.getContents()) {
            if (item == null) continue;
            var left = player.getInventory().addItem(item);
            if (left.isEmpty()) {
                moved += item.getAmount();
                linked.removeItem(item);
            }
        }
        return moved;
    }
}
