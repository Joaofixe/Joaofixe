package com.lfstudios.autotools.autos;

import com.lfstudios.autotools.LFAutoTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class AutoCompressService {
    private final LFAutoTools plugin;
    private final Map<Material, Material> recipes = new HashMap<>();

    public AutoCompressService(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        recipes.clear();
        plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.CONFIG).getConfigurationSection("autocompress.recipes").getKeys(false)
                .forEach(k -> recipes.put(Material.valueOf(k), Material.valueOf(plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.CONFIG).getString("autocompress.recipes." + k))));
    }

    public void compressInventory(Player player) {
        for (Map.Entry<Material, Material> entry : recipes.entrySet()) {
            int total = 0;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == entry.getKey()) {
                    total += item.getAmount();
                }
            }
            if (total < 9) continue;
            int blocks = total / 9;
            player.getInventory().removeItem(new ItemStack(entry.getKey(), blocks * 9));
            player.getInventory().addItem(new ItemStack(entry.getValue(), blocks));
        }
    }
}
