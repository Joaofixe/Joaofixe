package com.lfstudios.autotools.core;

public enum Module {
    AUTOGUI("autogui"),
    AUTOSORT("autosort"),
    AUTOSELL("autosell"),
    AUTOSMELT("autosmelt"),
    AUTOCOMPRESS("autocompress"),
    AUTOPICKUP("autopickup"),
    INVENTORY_LINKING("inventory-linking"),
    FILTERS("filters"),
    INVENTORY_PUSH("inventory-push"),
    INVENTORY_PULL("inventory-pull"),
    INVENTORY_AUTOPUSH("inventory-autopush"),
    CUSTOM_SHOPS("custom-shops"),
    WORLDGUARD_RESTRICTIONS("worldguard-restrictions"),
    MULTIPLIERS("multipliers"),
    HOOKS("hooks"),
    UPDATE_MANAGER("update-manager");

    private final String configKey;

    Module(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigKey() {
        return configKey;
    }
}
