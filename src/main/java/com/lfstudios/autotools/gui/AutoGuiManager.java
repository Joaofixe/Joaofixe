package com.lfstudios.autotools.gui;

import com.lfstudios.autotools.LFAutoTools;
import com.lfstudios.autotools.api.PlayerData;
import com.lfstudios.autotools.api.PlayerFeature;
import com.lfstudios.autotools.core.Module;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class AutoGuiManager {
    private final LFAutoTools plugin;

    public AutoGuiManager(LFAutoTools plugin) {this.plugin = plugin;}

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(player, InventoryType.CHEST, Component.text("LF-AutoTools Hub"));
        PlayerData data = plugin.getPlayerDataService().get(player.getUniqueId());
        addFeature(inv, 10, Material.HOPPER, "AutoSort", data.isEnabled(PlayerFeature.AUTOSORT), plugin.getModuleManager().isEnabled(Module.AUTOSORT));
        addFeature(inv, 11, Material.GOLD_INGOT, "AutoSell", data.isEnabled(PlayerFeature.AUTOSELL), plugin.getModuleManager().isEnabled(Module.AUTOSELL));
        addFeature(inv, 12, Material.BLAST_FURNACE, "AutoSmelt", data.isEnabled(PlayerFeature.AUTOSMELT), plugin.getModuleManager().isEnabled(Module.AUTOSMELT));
        addFeature(inv, 13, Material.IRON_BLOCK, "AutoCompress", data.isEnabled(PlayerFeature.AUTOCOMPRESS), plugin.getModuleManager().isEnabled(Module.AUTOCOMPRESS));
        addFeature(inv, 14, Material.ENDER_CHEST, "AutoPickup", data.isEnabled(PlayerFeature.AUTOPICKUP), plugin.getModuleManager().isEnabled(Module.AUTOPICKUP));
        player.openInventory(inv);
    }

    private void addFeature(Inventory inv, int slot, Material material, String name, boolean enabled, boolean moduleEnabled) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Status: " + (enabled ? "Enabled" : "Disabled")));
        lore.add(Component.text("Module: " + (moduleEnabled ? "Available" : "Disabled in config")));
        meta.lore(lore);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }
}
