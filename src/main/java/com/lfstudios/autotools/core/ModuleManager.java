package com.lfstudios.autotools.core;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

public final class ModuleManager {
    private final Map<Module, Boolean> states = new EnumMap<>(Module.class);

    public void reload(FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("modules");
        for (Module module : Module.values()) {
            boolean enabled = section == null || section.getBoolean(module.getConfigKey(), true);
            states.put(module, enabled);
        }
    }

    public boolean isEnabled(Module module) {
        return states.getOrDefault(module, false);
    }
}
