package com.sdpteam.connectout.utils;

import java.util.concurrent.CompletableFuture;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataTestUtil {

    /**
     * Converts a LiveData into a Future, which is usefully for testing
     *
     * @param liveData live data to transform into a Future
     * @return a classical completable future (can do .orTimeOut().join() for example!)
     */
    public static <T> CompletableFuture<T> toCompletableFuture(LiveData<T> liveData) {
        if(liveData == null){
            return null;
        }
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        LiveDataObserver<T> observer = new LiveDataObserver<>(completableFuture);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> liveData.observeForever(observer));

        return completableFuture;
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