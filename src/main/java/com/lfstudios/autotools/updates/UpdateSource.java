package com.lfstudios.autotools.updates;

import java.util.concurrent.CompletableFuture;

public interface UpdateSource {
    CompletableFuture<String> fetchLatestVersion();
}
