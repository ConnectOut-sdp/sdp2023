package com.sdpteam.connectout;

import androidx.annotation.NonNull;

import java.util.Arrays;

public enum Path {
    Slash("/"),
    Test("Test"),
    Profile("Profile"),
    Users("Users"),
    Events("Events");

    private String path;
    private final String initPath;

    Path(String path) {
        this.path = path;
        initPath = path;
    }

    @NonNull
    @Override
    public String toString() {
        return path;
    }

    /**
     * Transform the Paths into a testPaths in order to not pollute user's firebase.
     */
    public static void applyTest() {
        Arrays.stream(Path.values()).forEach(p -> p.path = Test.initPath + Slash.initPath + p.path);
    }

    /**
     * Return the Paths to normal paths in order to act on the user's firebase.
     */
    public static void removeTest() {
        Arrays.stream(Path.values()).forEach(p -> p.path = p.initPath);
    }


}
