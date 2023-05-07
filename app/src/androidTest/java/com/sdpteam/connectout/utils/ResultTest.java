package com.sdpteam.connectout.utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ResultTest {
    @Test
    public void shouldThrowErrorIfGiveNullValueIfSuccess() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Result<>(null, true, "");
        });
    }

    @Test
    public void shouldThrowErrorIfGiveNullMsg() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Result<>(new Object(), true, null);
        });
    }

    @Test
    public void getValueReturnsTheGivenValue() {
        Object value = new Object();
        Result<Object> result = new Result<>(value, true);
        assertSame(result.value(), value);
    }

    @Test
    public void getValueAndMessage() {
        Object value = new Object();
        Result<Object> result = new Result<>(value, false, "message");
        assertSame(result.value(), value);
        assertFalse(result.isSuccess());
        assertThat(result.msg(), is("message"));
    }
}
