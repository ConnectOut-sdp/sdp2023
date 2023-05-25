package com.sdpteam.connectout.validation;

import android.widget.TextView;

public class ValidationUtils {
    public static boolean handleValidationFailure(boolean condition, TextView input, String error) {
        if (!condition) {
            input.setError(error);
            input.requestFocus();
        }
        return condition;
    }
}
