package com.lfstudios.autotools.config;

public enum ConfigFile {
    CONFIG("config.yml"),
    MENUS("menus.yml"),
    SHOPS("shops.yml"),
    FILTERS("filters.yml"),
    STORAGE("storage.yml"),
    HOOKS("hooks.yml"),
    UPDATES("updates.yml");

    private final String fileName;

    ConfigFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
