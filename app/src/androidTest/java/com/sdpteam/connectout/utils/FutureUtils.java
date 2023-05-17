package com.sdpteam.connectout.utils;

import android.os.SystemClock;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureUtils {
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

    /**
     * Use this one for sleeps if you don't have access to the future
     */
    public static void waitABit() {
        SystemClock.sleep(1500);
    }
}
