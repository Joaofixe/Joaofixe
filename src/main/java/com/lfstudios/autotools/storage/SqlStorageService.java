package com.lfstudios.autotools.storage;

import com.lfstudios.autotools.LFAutoTools;
import com.lfstudios.autotools.api.PlayerData;
import com.lfstudios.autotools.api.PlayerFeature;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

public final class SqlStorageService implements StorageService {
    private final LFAutoTools plugin;
    private HikariDataSource dataSource;
    private StorageType type;

    public SqlStorageService(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        FileConfiguration cfg = plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.STORAGE);
        type = StorageType.valueOf(cfg.getString("storage.type", "SQLITE").toUpperCase());
        HikariConfig hikari = new HikariConfig();
        if (type == StorageType.SQLITE) {
            File file = new File(plugin.getDataFolder(), cfg.getString("storage.sqlite.file", "data.db"));
            hikari.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
        } else {
            hikari.setJdbcUrl("jdbc:mysql://" + cfg.getString("storage.mysql.host") + ":" + cfg.getInt("storage.mysql.port") + "/" + cfg.getString("storage.mysql.database") + cfg.getString("storage.mysql.params", ""));
            hikari.setUsername(cfg.getString("storage.mysql.username"));
            hikari.setPassword(cfg.getString("storage.mysql.password"));
        }
        hikari.setMaximumPoolSize(cfg.getInt("storage.pool.maximum-pool-size", 8));
        hikari.setMinimumIdle(cfg.getInt("storage.pool.minimum-idle", 2));
        hikari.setPoolName("LF-AutoTools-Storage");
        dataSource = new HikariDataSource(hikari);
        createTables();
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS lfautotools_players (uuid VARCHAR(36) PRIMARY KEY, language VARCHAR(16), linked_inventory TEXT, active_filter VARCHAR(64), feature_states TEXT, filters TEXT)";
        try (Connection conn = dataSource.getConnection(); Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException ex) {
            plugin.getLogger().severe("Failed to create storage tables: " + ex.getMessage());
        }
    }

    @Override
    public void shutdown() {
        if (dataSource != null) dataSource.close();
    }

    @Override
    public Optional<PlayerData> load(UUID uuid) {
        String sql = "SELECT * FROM lfautotools_players WHERE uuid = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                PlayerData data = new PlayerData(uuid);
                data.setSelectedLanguage(rs.getString("language"));
                data.setLinkedInventory(rs.getString("linked_inventory"));
                data.setActiveFilter(rs.getString("active_filter"));
                decodeFeatures(data, rs.getString("feature_states"));
                decodeFilters(data, rs.getString("filters"));
                return Optional.of(data);
            }
        } catch (SQLException ex) {
            plugin.getLogger().warning("Failed to load player data: " + ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void save(PlayerData data) {
        String sql = "INSERT INTO lfautotools_players (uuid, language, linked_inventory, active_filter, feature_states, filters) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT(uuid) DO UPDATE SET language=excluded.language, linked_inventory=excluded.linked_inventory, active_filter=excluded.active_filter, feature_states=excluded.feature_states, filters=excluded.filters";
        if (type == StorageType.MYSQL) {
            sql = "INSERT INTO lfautotools_players (uuid, language, linked_inventory, active_filter, feature_states, filters) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE language=VALUES(language), linked_inventory=VALUES(linked_inventory), active_filter=VALUES(active_filter), feature_states=VALUES(feature_states), filters=VALUES(filters)";
        }
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data.getUuid().toString());
            ps.setString(2, data.getSelectedLanguage());
            ps.setString(3, data.getLinkedInventory());
            ps.setString(4, data.getActiveFilter());
            ps.setString(5, encodeFeatures(data));
            ps.setString(6, encodeFilters(data));
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().warning("Failed to save player data: " + ex.getMessage());
        }
    }

    private String encodeFeatures(PlayerData data) {
        StringJoiner joiner = new StringJoiner(";");
        for (PlayerFeature feature : PlayerFeature.values()) {
            joiner.add(feature.name() + ":" + data.isEnabled(feature));
        }
        return joiner.toString();
    }

    private void decodeFeatures(PlayerData data, String raw) {
        if (raw == null || raw.isEmpty()) return;
        for (String part : raw.split(";")) {
            String[] split = part.split(":");
            if (split.length == 2) {
                try {
                    data.setEnabled(PlayerFeature.valueOf(split[0]), Boolean.parseBoolean(split[1]));
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }

    private String encodeFilters(PlayerData data) {
        StringJoiner joiner = new StringJoiner(";");
        data.getFilters().forEach((key, value) -> joiner.add(key + "=" + value));
        return joiner.toString();
    }

    private void decodeFilters(PlayerData data, String raw) {
        if (raw == null || raw.isEmpty()) return;
        for (String part : raw.split(";")) {
            String[] split = part.split("=");
            if (split.length == 2) {
                data.getFilters().put(split[0], split[1]);
            }
        }
    }
}
