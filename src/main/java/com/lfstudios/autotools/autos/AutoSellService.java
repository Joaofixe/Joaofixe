package com.lfstudios.autotools.autos;

import com.lfstudios.autotools.LFAutoTools;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class AutoSellService {
    private final LFAutoTools plugin;
    private final Map<Material, Double> prices = new HashMap<>();

    public AutoSellService(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        prices.clear();
        plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.SHOPS).getConfigurationSection("internal-shop.prices").getKeys(false)
                .forEach(k -> prices.put(Material.valueOf(k), plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.SHOPS).getDouble("internal-shop.prices." + k)));
    }

    public void sell(Player player, ItemStack stack) {
        Double price = prices.get(stack.getType());
        if (price == null) return;
        Economy economy = plugin.getEconomy();
        if (economy == null) return;
        economy.depositPlayer(player, price * stack.getAmount());
    }
}
