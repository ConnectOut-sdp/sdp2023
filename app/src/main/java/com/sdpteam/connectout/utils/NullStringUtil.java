package com.sdpteam.connectout.utils;

import java.util.Objects;

public class NullStringUtil {

    public static String nonNullString(String s) {
        return nonNullString(s, "");
    }

    public static String nonNullString(String s, String defaultValue) {
        if (s == null) {
            return Objects.requireNonNull(defaultValue);
        }
        return s;
    }
}
