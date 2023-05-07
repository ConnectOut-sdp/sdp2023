package com.sdpteam.connectout.utils;

/**
 * It contains the value of the operation if it is successful!
 * This is a immutable object that describe the result of an operation (that return some value of any type)
 * (is it successful or not, can have an optional message (for instance can be the message error)
 *
 * @param <T> type of the value
 */
public class Result<T> {
    private final T value;
    private final boolean isSuccess;
    private final String msg;

    public Result(T value, boolean isSuccess, String msg) {
        if (value == null && isSuccess) {
            throw new IllegalArgumentException("value cannot be null if isSuccess is true");
        }
        if (msg == null) {
            throw new IllegalArgumentException("msg cannot be null");
        }

        this.value = value;
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public Result(T value, boolean isSuccess) {
        this(value, isSuccess, "");
    }

    public T value() {
        return value;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String msg() {
        return msg;
    }
}
