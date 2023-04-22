package com.sdpteam.connectout.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureUtil {
    /**
     * join a future with timeout
     */
    public static <E> E fJoin(CompletableFuture<E> future) {
        try {
            return future.get(20, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
