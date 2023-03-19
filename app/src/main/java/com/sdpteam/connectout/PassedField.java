package com.sdpteam.connectout;

import androidx.annotation.NonNull;

public enum PassedField {
    UserId("UserId");
    private final  String field;

    PassedField(String field){

        this.field = field;
    }

    @NonNull
    @Override
    public String toString() {
        return field;
    }
}
