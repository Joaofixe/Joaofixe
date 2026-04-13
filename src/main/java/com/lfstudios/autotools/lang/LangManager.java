package com.lfstudios.autotools.lang;

import com.lfstudios.autotools.LFAutoTools;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class LangManager {
    private final LFAutoTools plugin;
    private final Map<String, YamlConfiguration> langs = new HashMap<>();
    private final MiniMessage mm = MiniMessage.miniMessage();

    public LangManager(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    public void load() {
        langs.clear();
        for (String key : new String[]{"en", "pt-br", "es", "fr"}) {
            String path = "lang/" + key + ".yml";
            plugin.saveResource(path, false);
            langs.put(key, YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), path)));
        }
    }

    public Component message(Player player, String key) {
        String lang = plugin.getPlayerDataService().get(player.getUniqueId()).getSelectedLanguage();
        return raw(lang, key);
    }

    public Component raw(String lang, String key) {
        YamlConfiguration cfg = langs.getOrDefault(lang, langs.get("en"));
        return mm.deserialize(cfg.getString(key, "<red>Missing language key: " + key));
    }
}
