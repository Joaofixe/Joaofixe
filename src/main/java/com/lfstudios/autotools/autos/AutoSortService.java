package com.lfstudios.autotools.autos;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class AutoSortService {
    public void sort(Player player) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item != null) items.add(item.clone());
        }
        items.sort(Comparator.comparing(i -> i.getType().name()));
        player.getInventory().clear();
        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }
    }
}
