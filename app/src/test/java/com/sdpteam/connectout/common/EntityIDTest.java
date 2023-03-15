package com.sdpteam.connectout.common;

import static org.junit.Assert.assertEquals;

import com.sdpteam.connectout.event.EventID;
import com.sdpteam.connectout.profile.ProfileID;

import org.junit.Test;

import java.util.Objects;

public class EntityIDTest {

    @Test
    public void test_equals() {
        final EntityID id1 = new EntityID("a");
        final EntityID id2 = new EntityID("a");
        assertEquals(true, id1.equals(id2));
    }

    @Test
    public void test_hashCode() {
        final EntityID id = new EventID("abc");
        assertEquals(Objects.hash("abc"), id.hashCode());
    }

    @Test
    public void test_toString() {
        final EntityID id = new ProfileID("123");
        assertEquals("123", id.toString());
    }
}