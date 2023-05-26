package com.sdpteam.connectout.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class NullStringUtilTest {
    @Test
    public void nullStringGivesEmptyString() {
        assertEquals("", NullStringUtil.nonNullString(null));
    }

    @Test
    public void defaultValueWorks() {
        assertEquals("default", NullStringUtil.nonNullString(null, "default"));
    }

    @Test
    public void nullDefaultValueShouldNotBeNull() {
        assertThrows(NullPointerException.class, () -> {
            String s = NullStringUtil.nonNullString(null, null);
        });
    }

    @Test
    public void defaultConstructor() {
        assertNotNull(new NullStringUtil());
    }
}
