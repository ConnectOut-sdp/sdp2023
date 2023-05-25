package com.sdpteam.connectout.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NullStringUtilTest {
    @Test
    public void quickTest() {
        assertEquals("", NullStringUtil.nonNullString(null));
        assertEquals("default", NullStringUtil.nonNullString(null, "default"));
    }
}
