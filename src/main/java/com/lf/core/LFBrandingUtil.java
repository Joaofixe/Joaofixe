package com.lf.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class LFBrandingUtil {

    private static final String PREFIX = "&8[&6LF Studios&8]&r ";
    private static final String DIVIDER = "&8━━━━━━━━━━━━━━━━━━━━━━━━━━━━";

    private LFBrandingUtil() {}

    private static String c(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private static void send(String text) {
        Bukkit.getConsoleSender().sendMessage(c(text));
    }

    private static String detectPlatform() {
        String name = Bukkit.getName();
        if (name == null || name.isEmpty()) return "Bukkit";
        if (name.toLowerCase().contains("purpur")) return "Purpur";
        if (name.toLowerCase().contains("paper")) return "Paper";
        if (name.toLowerCase().contains("spigot")) return "Spigot";
        return name;
    }

    public static void sendEnableBanner(String pluginName, String version, String moduleName) {
        String platform = detectPlatform();
        String pad = "   ";

        send(" ");
        send(PREFIX + DIVIDER);
        send("&6██╗     &b███████╗");
        send("&6██║     &b██╔════╝" + pad + "&6" + pluginName + " &ev" + version);
        send("&6██║     &b█████╗  " + pad + "&7Running on &f" + platform);
        send("&6██║     &b██╔══╝  " + pad + "&7Module: &f" + moduleName);
        send("&6███████╗&b██║     " + pad + "&7Status: &aENABLED");
        send("&6╚══════╝&b╚═╝");
        send(PREFIX + DIVIDER);
        send(" ");
    }

    public static void sendDisableBanner(String pluginName, String moduleName) {
        String pad = "   ";

        send(" ");
        send(PREFIX + DIVIDER);
        send("&6██╗     &b███████╗");
        send("&6██║     &b██╔════╝" + pad + "&6" + pluginName);
        send("&6██║     &b█████╗  " + pad + "&7Module: &f" + moduleName);
        send("&6██║     &b██╔══╝  " + pad + "&7Shutting down...");
        send("&6███████╗&b██║     " + pad + "&7Status: &cDISABLED");
        send("&6╚══════╝&b╚═╝");
        send(PREFIX + DIVIDER);
        send(" ");
    }
}
