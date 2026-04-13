package com.lfstudios.autotools.config;

import com.lfstudios.autotools.LFAutoTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

public final class ConfigManager {
    private final LFAutoTools plugin;
    private final Map<ConfigFile, YamlConfiguration> cache = new EnumMap<>(ConfigFile.class);

    public ConfigManager(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {
        for (ConfigFile configFile : ConfigFile.values()) {
            plugin.saveResource(configFile.getFileName(), false);
            File file = new File(plugin.getDataFolder(), configFile.getFileName());
            cache.put(configFile, YamlConfiguration.loadConfiguration(file));
        }
    }

    public void reloadAll() {
        cache.clear();
        loadAll();
    }

    public YamlConfiguration get(ConfigFile configFile) {
        return cache.get(configFile);
    }
}
