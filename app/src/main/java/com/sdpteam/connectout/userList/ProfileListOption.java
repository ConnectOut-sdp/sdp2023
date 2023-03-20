package com.sdpteam.connectout.userList;

import androidx.annotation.NonNull;

/**
 * Enum describing the different types of filtering possible.
 */
public enum ProfileListOption {
    NONE(""),
    RATING("rating"),
    NAME("name");


    private final String name;
    ProfileListOption(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
