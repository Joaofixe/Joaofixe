package com.lfstudios.autotools.listeners;

import com.lfstudios.autotools.LFAutoTools;
import com.lfstudios.autotools.api.PlayerData;
import com.lfstudios.autotools.api.PlayerFeature;
import com.lfstudios.autotools.core.Module;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

public final class MiningListener implements Listener {
    private final LFAutoTools plugin;

    public MiningListener(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrop(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataService().get(player.getUniqueId());
        for (Item drop : event.getItems()) {
            ItemStack stack = drop.getItemStack();
            if (plugin.getModuleManager().isEnabled(Module.AUTOSMELT) && data.isEnabled(PlayerFeature.AUTOSMELT)) {
                stack = plugin.getAutoSmeltService().transform(stack);
            }
            if (plugin.getModuleManager().isEnabled(Module.AUTOSELL) && data.isEnabled(PlayerFeature.AUTOSELL)) {
                plugin.getAutoSellService().sell(player, stack);
                drop.remove();
                continue;
            }
            if (plugin.getModuleManager().isEnabled(Module.AUTOPICKUP) && data.isEnabled(PlayerFeature.AUTOPICKUP)) {
                plugin.getAutoPickupService().giveOrDrop(player, stack);
                drop.remove();
                continue;
            }
            drop.setItemStack(stack);
        }
        if (plugin.getModuleManager().isEnabled(Module.AUTOCOMPRESS) && data.isEnabled(PlayerFeature.AUTOCOMPRESS)) {
            plugin.getAutoCompressService().compressInventory(player);
        }
        if (plugin.getModuleManager().isEnabled(Module.AUTOSORT) && data.isEnabled(PlayerFeature.AUTOSORT)) {
            plugin.getAutoSortService().sort(player);
        }
    }
}
