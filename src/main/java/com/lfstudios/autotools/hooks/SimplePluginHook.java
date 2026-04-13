package com.lfstudios.autotools.hooks;

import org.bukkit.Bukkit;

public final class SimplePluginHook implements Hook {
    private final String pluginName;

    public SimplePluginHook(String pluginName) {
        this.pluginName = pluginName;
    }

    @Override
    public String getName() {
        return pluginName;
    }

    @Override
    public boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null;
    }
}
