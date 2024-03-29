package com.sdpteam.connectout.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.android.gms.maps.model.LatLng;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

public class EventTest {

    private final static GPSCoordinates TEST_COORDINATES = new GPSCoordinates(new LatLng(0.1, 0.2));
    private final static Event TEST_EVENT = new Event("", "event1", "descr", TEST_COORDINATES, "Bob");

    @Test
    public void testConstructorWithTitle() {
        assertThat(TEST_EVENT.getTitle(), is("event1"));
    }

    @Test
    public void testConstructorWithCoordinates() {
        assertThat(TEST_EVENT.getCoordinates().getLatitude(), is(0.1));
        assertThat(TEST_EVENT.getCoordinates().getLongitude(), is(0.2));
    }

    @Test
    public void testConstructorWithDescription() {
        assertThat(TEST_EVENT.getDescription(), is("descr"));
    }

    @Test
    public void addsParticipantToEventOnce() {
        assertTrue(TEST_EVENT.addParticipant("2"));
        assertTrue(TEST_EVENT.hasJoined("2"));
        assertFalse(TEST_EVENT.addParticipant("2"));
        assertTrue(TEST_EVENT.hasJoined("2"));
    }

    @Test
    public void removeParticipantToEventOnce() {
        TEST_EVENT.addParticipant("1");
        assertTrue(TEST_EVENT.hasJoined("1"));
        assertTrue(TEST_EVENT.removeParticipant("1"));
        assertFalse(TEST_EVENT.hasJoined("1"));
        assertFalse(TEST_EVENT.removeParticipant("1"));
    }

    @Test
    public void addInterestedParticipant() {
        TEST_EVENT.addInterestedParticipant("1");
        assertTrue(TEST_EVENT.isInterested("1"));
        assertFalse(TEST_EVENT.hasJoined("1"));
    }

    @Test
    public void removeInterestedParticipant() {
        TEST_EVENT.addInterestedParticipant("1");
        assertTrue(TEST_EVENT.isInterested("1"));
        assertFalse(TEST_EVENT.hasJoined("1"));

        TEST_EVENT.removeParticipant("1");
        assertFalse(TEST_EVENT.isInterested("1"));
        assertFalse(TEST_EVENT.hasJoined("1"));
    }

    @Test
    public void joinAfterBeingInterested() {
        TEST_EVENT.addInterestedParticipant("1");
        assertTrue(TEST_EVENT.isInterested("1"));
        assertFalse(TEST_EVENT.hasJoined("1"));

        TEST_EVENT.addParticipant("1");

        assertFalse(TEST_EVENT.isInterested("1"));
        assertTrue(TEST_EVENT.hasJoined("1"));
    }

    @Test
    public void testImageUrl() {
        assertNull(TEST_EVENT.getPreviewImageUrl());
    }
}