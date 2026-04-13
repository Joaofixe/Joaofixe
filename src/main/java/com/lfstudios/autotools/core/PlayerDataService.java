package com.lfstudios.autotools.core;

import com.lfstudios.autotools.api.PlayerData;
import com.lfstudios.autotools.storage.StorageService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerDataService {
    private final StorageService storageService;
    private final Map<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public PlayerDataService(StorageService storageService) {
        this.storageService = storageService;
    }

    public PlayerData get(UUID uuid) {
        return cache.computeIfAbsent(uuid, id -> storageService.load(id).orElseGet(() -> new PlayerData(id)));
    }

    public void save(UUID uuid) {
        PlayerData data = cache.get(uuid);
        if (data != null) {
            storageService.save(data);
        }
    }

    public void saveAll() {
        cache.values().forEach(storageService::save);
    }

    public void unload(UUID uuid) {
        save(uuid);
        cache.remove(uuid);
    }
}
