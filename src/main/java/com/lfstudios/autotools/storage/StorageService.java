package com.lfstudios.autotools.storage;

import com.lfstudios.autotools.api.PlayerData;

import java.util.Optional;
import java.util.UUID;

public interface StorageService {
    void init();
    void shutdown();
    Optional<PlayerData> load(UUID uuid);
    void save(PlayerData data);
}
