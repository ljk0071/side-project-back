package com.side.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsyncUtil {

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return AsyncUtil.runAsync(runnable, ForkJoinPool.commonPool());
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return CompletableFuture.runAsync(runnable, executor)
                                .exceptionally(e -> {
                                    if (e.getCause().getClass().getPackageName().startsWith("com.side")) {
                                        log.error(e.getCause().getMessage());
                                    } else {
                                        log.error("[IN ASYNC UTIL]", e);
                                    }
                                    return null;
                                });
    }
}
