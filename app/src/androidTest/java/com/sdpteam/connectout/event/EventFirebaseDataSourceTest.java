package com.sdpteam.connectout.event;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.profile.EditProfileActivity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EventFirebaseDataSourceTest {

    @Test
    public void modelReturnNullOnNonExistingEventEId() {

        EventFirebaseDataSource model = new EventFirebaseDataSource();
        Event foundEvent = model.getEvent("invalid").join();
        assertNull(foundEvent);
    }

    @Test
    public void modelReturnNullOnNonExistingEventUId() {

        EventFirebaseDataSource model = new EventFirebaseDataSource();
        Event foundEvent = model.getEvent("invalid", "no title").join();
        assertNull(foundEvent);
    }

    @Test
    public void testGetValueWithValidUidAndTitle() {
        String title = "Tennis match";
        GPSCoordinates gpsCoordinates = new GPSCoordinates(1.5, 1.5);
        String description = "Looking for a tennis partner";
        String ownerId = "user1";
        String eventId = "1";
        Event event = new Event(eventId, title, description, gpsCoordinates, ownerId);
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(event);

        // retrieve the event from the database using its owner ID and title and check that it matches the original event
        Event retrievedEvent = model.getEvent(ownerId, title).join();
        assertThat(retrievedEvent.getTitle(), is(title));
        assertThat(retrievedEvent.getId(), is("1"));
        assertThat(retrievedEvent.getCoordinates().getLatitude(), is(1.5));
        assertThat(retrievedEvent.getCoordinates().getLongitude(), is(1.5));
        assertThat(retrievedEvent.getDescription(), is(description));
        assertThat(retrievedEvent.getOrganizer(), is(ownerId));
    }

    @Test
    public void testManualSaveAndGetCorrectValues() {
        String title = "Tenis match";
        String description = "Search for tenis partner";

        Event e = new Event("1", title, description, new GPSCoordinates(1.5, 1.5), EditProfileActivity.NULL_USER);
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(e);

        Event foundEvent = model.getEvent("1").join();

        assertThat(foundEvent.getTitle(), is(title));
        assertThat(foundEvent.getId(), is("1"));
        assertThat(foundEvent.getCoordinates().getLatitude(), is(1.5));
        assertThat(foundEvent.getCoordinates().getLongitude(), is(1.5));
        assertThat(foundEvent.getDescription(), is(description));
        assertThat(foundEvent.getOrganizer(), is(EditProfileActivity.NULL_USER));
    }

    @Test
    public void testManualSaveAndGetOnlyCorrectUId() {
        String title = "Tenis match";
        String description = "Search for tenis partner";

        Event e = new Event("1", title, description, new GPSCoordinates(1.5, 1.5), EditProfileActivity.NULL_USER);
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(e);

        Event foundEvent = model.getEvent(EditProfileActivity.NULL_USER, "wrong title").join();

        assertNull(foundEvent);
    }

    @Test
    public void testManualSaveAndGetOnlyCorrectTitle() {
        String title = "Tenis match";
        String description = "Search for tenis partner";

        Event e = new Event("1", title, description, new GPSCoordinates(1.5, 1.5), EditProfileActivity.NULL_USER);
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(e);

        Event foundEvent = model.getEvent("wrong id", title).join();

        assertNull(foundEvent);
    }

    @Test
    public void testGetWithFilters() {
        final Event e1 = new Event("1", "judo", "", new GPSCoordinates(1.5, 1.5), "");
        final Event e2 = new Event("2", "tennis", "", new GPSCoordinates(1.5, 1.5), "");
        final Event e3 = new Event("3", "football", "", new GPSCoordinates(1.5, 1.5), "");

        final EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(e1);
        model.saveEvent(e2);
        model.saveEvent(e3);

        final EventFilter filter = e -> "1".equals(e.getId()) || "2".equals(e.getId());
        final List<Event> results = model.getEventsByFilter(filter).join();

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(filter));
    }

    @Test
    public void doesNotSaveNullEvent() {
        Event e = null;
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        assertFalse(model.saveEvent(e));
    }

    @Test
    public void retrievingNonEventsIsNull() {

        EventFirebaseDataSource model = new EventFirebaseDataSource();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Events").child("NotEid").setValue(new ArrayList<>());

        Event foundEvent = model.getEvent("NotEid").join();

        assertNull(foundEvent);
    }
}
