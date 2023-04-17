package com.sdpteam.connectout.utils;

import androidx.lifecycle.Observer;

/**
 * Provides an observer that is only used once and stops observing after receiving a value.
 * This can be used as a workaround for the absence of the observeOnce() method in Java.
 */
public class SingleShotObserver<T> implements Observer<T> {

    private final Observer<T> observer;
    private boolean hasReceivedValue = false;

    public SingleShotObserver(Observer<T> observer) {
        this.observer = observer;
    }

    @Override
    public void onChanged(T t) {
        if (!hasReceivedValue) {
            hasReceivedValue = true;
            observer.onChanged(t);
        }
    }
}
