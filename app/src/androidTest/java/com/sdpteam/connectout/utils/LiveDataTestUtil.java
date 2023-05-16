package com.sdpteam.connectout.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {

    /**
     * Note: Sometimes it doesn't work, use getOrAwaitValue() instead !
     * <p>
     * Converts a LiveData into a Future, which is usefully for testing
     *
     * @param liveData live data to transform into a Future
     * @return a classical completable future (can do .join() for example!)
     */
    @Deprecated
    public static <T> CompletableFuture<T> toCompletableFuture(LiveData<T> liveData) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        LiveDataObserver<T> observer = new LiveDataObserver<>(completableFuture);
        // liveData.observeForever(observer); // does not work, must be done on main thread :
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            liveData.observeForever(observer);
        });

        return completableFuture;
    }

    /**
     * Get the value from a LiveData object or waiting for LiveData to emit, for 2 seconds and return it.
     * <p>
     * It is a replacement for .toCompletableFuture().join() which does not work.
     * <p>
     * Credits to https://gist.github.com/JoseAlcerreca/1e9ee05dcdd6a6a6fa1cbfc125559bba
     * <p>
     * !! DONT FORGET TO PUT this at the beginning of your test class !!
     *
     * @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
     **/
    public static <T> T getOrAwaitValue(final LiveData<T> liveData) {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        Handler handler = new Handler(Looper.getMainLooper());
        liveData.observeForever(observer);

        // Don't wait indefinitely if the LiveData is not set.
        try {
            if (!latch.await(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("LiveData value was never set.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //noinspection unchecked
        return (T) data[0];
    }

    private static class LiveDataObserver<T> implements Observer<T> {
        private final CompletableFuture<T> completableFuture;

        LiveDataObserver(CompletableFuture<T> completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public void onChanged(T t) {
            completableFuture.complete(t);
        }
    }
}