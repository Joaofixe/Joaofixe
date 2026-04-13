package com.lfstudios.autotools.autos;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class AutoPickupService {
    public void giveOrDrop(Player player, ItemStack stack) {
        var leftovers = player.getInventory().addItem(stack);
        leftovers.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
    }
}
