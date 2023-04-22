package com.sdpteam.connectout.utils;

public class Chronometer {
    private long startTime;
    private long elapsedTime;
    private boolean running;
    private long threshold;

    public Chronometer() {
        startTime = 0;
        elapsedTime = 0;
        running = false;
        threshold = Long.MAX_VALUE; // set a default threshold
    }

    public void start() {
        if (!running) {
            startTime = System.currentTimeMillis();
            running = true;
        }
    }

    public void stop() {
        if (running) {
            elapsedTime += System.currentTimeMillis() - startTime;
            running = false;
        }
    }

    public void reset() {
        startTime = 0;
        elapsedTime = 0;
        running = false;
    }

    public long getElapsedTime() {
        if (running) {
            return elapsedTime + (System.currentTimeMillis() - startTime);
        } else {
            return elapsedTime;
        }
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public boolean hasExceededThreshold() {
        return getElapsedTime() > threshold;
    }
}
