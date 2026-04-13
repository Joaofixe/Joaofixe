package com.lfstudios.autotools.autos;

import com.lfstudios.autotools.LFAutoTools;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class AutoSmeltService {
    private final LFAutoTools plugin;
    private final Map<Material, Material> recipes = new HashMap<>();

    public AutoSmeltService(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        recipes.clear();
        plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.CONFIG)
                .getConfigurationSection("autosmelt.recipes")
                .getKeys(false)
                .forEach(k -> recipes.put(Material.valueOf(k), Material.valueOf(plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.CONFIG).getString("autosmelt.recipes." + k, k))));
    }

    public ItemStack transform(ItemStack input) {
        Material result = recipes.get(input.getType());
        if (result == null) return input;
        return new ItemStack(result, input.getAmount());
    }
}
