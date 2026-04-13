package com.lfstudios.autotools.updates;

import com.lfstudios.autotools.LFAutoTools;
import org.bukkit.configuration.file.FileConfiguration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class HttpUpdateSource implements UpdateSource {
    private final LFAutoTools plugin;

    public HttpUpdateSource(LFAutoTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<String> fetchLatestVersion() {
        FileConfiguration cfg = plugin.getConfigManager().get(com.lfstudios.autotools.config.ConfigFile.UPDATES);
        String url = cfg.getString("update.source.url", "");
        if (url.isEmpty()) {
            return CompletableFuture.completedFuture(plugin.getDescription().getVersion());
        }
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(8)).GET().build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> r.body().trim())
                .exceptionally(ex -> plugin.getDescription().getVersion());
    }
}
