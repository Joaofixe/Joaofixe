package com.lfstudios.autotools;

import com.lf.core.LFBrandingUtil;
import com.lfstudios.autotools.autos.*;
import com.lfstudios.autotools.commands.*;
import com.lfstudios.autotools.config.ConfigFile;
import com.lfstudios.autotools.config.ConfigManager;
import com.lfstudios.autotools.core.Module;
import com.lfstudios.autotools.core.ModuleManager;
import com.lfstudios.autotools.core.PlayerDataService;
import com.lfstudios.autotools.gui.AutoGuiManager;
import com.lfstudios.autotools.hooks.HookManager;
import com.lfstudios.autotools.inventory.FilterService;
import com.lfstudios.autotools.inventory.InventoryLinkService;
import com.lfstudios.autotools.inventory.InventoryTransferService;
import com.lfstudios.autotools.lang.LangManager;
import com.lfstudios.autotools.listeners.MiningListener;
import com.lfstudios.autotools.listeners.PlayerConnectionListener;
import com.lfstudios.autotools.storage.SqlStorageService;
import com.lfstudios.autotools.storage.StorageService;
import com.lfstudios.autotools.updates.HttpUpdateSource;
import com.lfstudios.autotools.updates.UpdateManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class LFAutoTools extends JavaPlugin {
    private ConfigManager configManager;
    private ModuleManager moduleManager;
    private LangManager langManager;
    private HookManager hookManager;
    private StorageService storageService;
    private PlayerDataService playerDataService;
    private UpdateManager updateManager;

    private AutoGuiManager autoGuiManager;
    private AutoSmeltService autoSmeltService;
    private AutoCompressService autoCompressService;
    private AutoSortService autoSortService;
    private AutoPickupService autoPickupService;
    private AutoSellService autoSellService;
    private InventoryLinkService inventoryLinkService;
    private InventoryTransferService inventoryTransferService;
    private FilterService filterService;
    private Economy economy;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadAll();
        moduleManager = new ModuleManager();
        moduleManager.reload(configManager.get(ConfigFile.CONFIG));
        langManager = new LangManager(this);
        langManager.load();
        setupEconomy();

        storageService = new SqlStorageService(this);
        storageService.init();
        playerDataService = new PlayerDataService(storageService);

        hookManager = new HookManager(this);
        if (moduleManager.isEnabled(Module.HOOKS)) {
            hookManager.load();
        }

        autoGuiManager = new AutoGuiManager(this);
        autoSmeltService = new AutoSmeltService(this);
        autoCompressService = new AutoCompressService(this);
        autoSortService = new AutoSortService();
        autoPickupService = new AutoPickupService();
        autoSellService = new AutoSellService(this);
        inventoryLinkService = new InventoryLinkService();
        inventoryTransferService = new InventoryTransferService();
        filterService = new FilterService();

        autoSmeltService.reload();
        autoCompressService.reload();
        autoSellService.reload();

        registerCommands();
        registerListeners();

        if (moduleManager.isEnabled(Module.UPDATE_MANAGER)) {
            updateManager = new UpdateManager(this, new HttpUpdateSource(this));
            updateManager.start();
        }

        LFBrandingUtil.sendEnableBanner(getDescription().getName(), getDescription().getVersion(), "AutoTools System");
    }

    @Override
    public void onDisable() {
        if (updateManager != null) updateManager.stop();
        if (playerDataService != null) playerDataService.saveAll();
        if (storageService != null) storageService.shutdown();
        LFBrandingUtil.sendDisableBanner(getDescription().getName(), "AutoTools System");
    }

    public void reloadPlugin() {
        configManager.reloadAll();
        moduleManager.reload(configManager.get(ConfigFile.CONFIG));
        langManager.load();
        autoSmeltService.reload();
        autoCompressService.reload();
        autoSellService.reload();
        if (moduleManager.isEnabled(Module.HOOKS)) hookManager.load();
        if (updateManager != null) updateManager.stop();
        if (moduleManager.isEnabled(Module.UPDATE_MANAGER)) {
            updateManager = new UpdateManager(this, new HttpUpdateSource(this));
            updateManager.start();
        }
    }

    private void registerCommands() {
        getCommand("autogui").setExecutor(new AutoGuiCommand(this));
        getCommand("autosort").setExecutor(new ToggleCommand(this, com.lfstudios.autotools.api.PlayerFeature.AUTOSORT, Module.AUTOSORT, "lfautotools.autosort"));
        getCommand("autosell").setExecutor(new ToggleCommand(this, com.lfstudios.autotools.api.PlayerFeature.AUTOSELL, Module.AUTOSELL, "lfautotools.autosell"));
        getCommand("autosmelt").setExecutor(new ToggleCommand(this, com.lfstudios.autotools.api.PlayerFeature.AUTOSMELT, Module.AUTOSMELT, "lfautotools.autosmelt"));
        getCommand("autocompress").setExecutor(new ToggleCommand(this, com.lfstudios.autotools.api.PlayerFeature.AUTOCOMPRESS, Module.AUTOCOMPRESS, "lfautotools.autocompress"));
        getCommand("autopickup").setExecutor(new ToggleCommand(this, com.lfstudios.autotools.api.PlayerFeature.AUTOPICKUP, Module.AUTOPICKUP, "lfautotools.autopickup"));
        getCommand("inventory").setExecutor(new InventoryCommand(this));
        getCommand("lfauto").setExecutor(new LFAutoAdminCommand(this));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MiningListener(this), this);
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) economy = rsp.getProvider();
    }

    public ConfigManager getConfigManager() { return configManager; }
    public ModuleManager getModuleManager() { return moduleManager; }
    public LangManager getLangManager() { return langManager; }
    public HookManager getHookManager() { return hookManager; }
    public PlayerDataService getPlayerDataService() { return playerDataService; }
    public AutoGuiManager getAutoGuiManager() { return autoGuiManager; }
    public AutoSmeltService getAutoSmeltService() { return autoSmeltService; }
    public AutoCompressService getAutoCompressService() { return autoCompressService; }
    public AutoSortService getAutoSortService() { return autoSortService; }
    public AutoPickupService getAutoPickupService() { return autoPickupService; }
    public AutoSellService getAutoSellService() { return autoSellService; }
    public InventoryLinkService getInventoryLinkService() { return inventoryLinkService; }
    public InventoryTransferService getInventoryTransferService() { return inventoryTransferService; }
    public FilterService getFilterService() { return filterService; }
    public Economy getEconomy() { return economy; }
    public UpdateManager getUpdateManager() { return updateManager; }
}
