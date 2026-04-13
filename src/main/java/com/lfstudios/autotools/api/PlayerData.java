package com.lfstudios.autotools.api;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerData {
    private final UUID uuid;
    private final Map<PlayerFeature, Boolean> features = new EnumMap<>(PlayerFeature.class);
    private final Map<String, String> filters = new HashMap<>();
    private String selectedLanguage = "en";
    private String linkedInventory = "";
    private String activeFilter = "";

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        for (PlayerFeature feature : PlayerFeature.values()) {
            features.put(feature, false);
        }
    }

    public UUID getUuid() { return uuid; }
    public boolean isEnabled(PlayerFeature feature) { return features.getOrDefault(feature, false); }
    public void setEnabled(PlayerFeature feature, boolean enabled) { features.put(feature, enabled); }
    public Map<PlayerFeature, Boolean> getFeatures() { return features; }
    public Map<String, String> getFilters() { return filters; }
    public String getSelectedLanguage() { return selectedLanguage; }
    public void setSelectedLanguage(String selectedLanguage) { this.selectedLanguage = selectedLanguage; }
    public String getLinkedInventory() { return linkedInventory; }
    public void setLinkedInventory(String linkedInventory) { this.linkedInventory = linkedInventory; }
    public String getActiveFilter() { return activeFilter; }
    public void setActiveFilter(String activeFilter) { this.activeFilter = activeFilter; }
}
