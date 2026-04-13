package com.lfstudios.autotools.inventory;

import com.lfstudios.autotools.api.PlayerData;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class FilterService {
    public void create(PlayerData data, String name, String materials) {
        data.getFilters().put(name.toLowerCase(Locale.ROOT), normalize(materials));
    }

    public boolean delete(PlayerData data, String name) {
        return data.getFilters().remove(name.toLowerCase(Locale.ROOT)) != null;
    }

    public Set<Material> getActiveMaterials(PlayerData data) {
        if (data.getActiveFilter().isEmpty()) return Set.of();
        String raw = data.getFilters().getOrDefault(data.getActiveFilter(), "");
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> Material.matchMaterial(s.toUpperCase(Locale.ROOT)))
                .filter(m -> m != null)
                .collect(Collectors.toSet());
    }

    private String normalize(String input) {
        return Arrays.stream(input.split(","))
                .map(s -> s.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.joining(","));
    }
}
