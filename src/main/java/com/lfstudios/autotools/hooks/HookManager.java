package com.lfstudios.autotools.hooks;

import com.lfstudios.autotools.LFAutoTools;

import java.util.HashMap;
import java.util.Map;

public final class HookManager {
    private final LFAutoTools plugin;
    private final Map<String, Hook> hooks = new HashMap<>();

    public HookManager(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    public void load() {
        hooks.clear();
        register(new SimplePluginHook("Vault"));
        register(new SimplePluginHook("ShopGUIPlus"));
        register(new SimplePluginHook("AdvancedEnchantments"));
        register(new SimplePluginHook("ItemsAdder"));
        register(new SimplePluginHook("Nexo"));
        register(new SimplePluginHook("AdvancedChests"));
        register(new SimplePluginHook("WorldGuard"));
        hooks.values().forEach(h -> plugin.getLogger().info("Hook " + h.getName() + ": " + (h.isAvailable() ? "loaded" : "not found")));
    }

    private void register(Hook hook) {
        hooks.put(hook.getName().toLowerCase(), hook);
    }

    public boolean isAvailable(String name) {
        Hook hook = hooks.get(name.toLowerCase());
        return hook != null && hook.isAvailable();
    }
}
