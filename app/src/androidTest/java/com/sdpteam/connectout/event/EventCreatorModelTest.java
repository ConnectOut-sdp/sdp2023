package com.sdpteam.connectout.event;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.profile.EditProfileActivity;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import org.junit.Test;

public class EventCreatorModelTest {

    @Test
    public void modelReturnNullOnNonExistingEventEId() {

        EventCreatorModel model = new EventCreatorModel();
        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getEvent("invalid")).join();
        assertNull(foundEvent);
    }

    @Test
    public void modelReturnNullOnNonExistingEventUId() {

        EventCreatorModel model = new EventCreatorModel();
        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getEvent("invalid", "no title")).join();
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
        EventCreatorModel model = new EventCreatorModel();
        model.saveEvent(event);

        // retrieve the event from the database using its owner ID and title and check that it matches the original event
        Event retrievedEvent = LiveDataTestUtil.toCompletableFuture(model.getEvent(ownerId, title)).join();
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
        EventCreatorModel model = new EventCreatorModel();
        model.saveEvent(e);

        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getEvent("1")).join();

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
        EventCreatorModel model = new EventCreatorModel();
        model.saveEvent(e);

        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getEvent(EditProfileActivity.NULL_USER, "wrong title")).join();

        assertNull(foundEvent);
    }

    @Test
    public void testManualSaveAndGetOnlyCorrectTitle() {
        String title = "Tenis match";
        String description = "Search for tenis partner";

        Event e = new Event("1", title, description, new GPSCoordinates(1.5, 1.5), EditProfileActivity.NULL_USER);
        EventCreatorModel model = new EventCreatorModel();
        model.saveEvent(e);

        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getEvent("wrong id", title)).join();

        assertNull(foundEvent);
    }

    @Test
    public void doesNotSaveNullEvent() {
        Event e = null;
        EventCreatorModel model = new EventCreatorModel();
        assertFalse(model.saveEvent(e));

    }

    @Test
    public void retrievingNonEventsHasNullAttributes() {

        EventCreatorModel model = new EventCreatorModel();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Events").child("NotEid").setValue(Profile.NULL_PROFILE);


        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getEvent("NotEid")).join();


        assertThat(foundEvent.getTitle(), is(Event.NULL_EVENT.getTitle()));
        assertThat(foundEvent.getDescription(), is(Event.NULL_EVENT.getDescription()));
        assertThat(foundEvent.getId(), is(Event.NULL_EVENT.getId()));
        assertThat(foundEvent.getCoordinates(), is(Event.NULL_EVENT.getCoordinates()));
        assertThat(foundEvent.getOrganizer(), is(EditProfileActivity.NULL_USER));
    }


}
