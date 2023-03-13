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
        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getValue("invalid")).join();
        assertNull(foundEvent);
    }

    @Test
    public void modelReturnNullOnNonExistingEventUId() {

        EventCreatorModel model = new EventCreatorModel();
        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getValue("invalid", "no title")).join();
        assertNull(foundEvent);
    }

    @Test
    public void testGetValueWithValidUidAndTitle() {
        String title = "Tennis match";
        GPSCoordinates gpsCoordinates = new GPSCoordinates(1.5, 1.5);
        String description = "Looking for a tennis partner";
        String ownerId = "user1";
        String eventId = "1";
        Event event = new Event(title, gpsCoordinates, description, ownerId, eventId);
        EventCreatorModel model = new EventCreatorModel();
        model.saveValue(event);

        // retrieve the event from the database using its owner ID and title and check that it matches the original event
        Event retrievedEvent = LiveDataTestUtil.toCompletableFuture(model.getValue(ownerId, title)).join();
        assertThat(retrievedEvent.getTitle(), is(title));
        assertThat(retrievedEvent.getEventId(), is("1"));
        assertThat(retrievedEvent.getGpsCoordinates().getLatitude(), is(1.5));
        assertThat(retrievedEvent.getGpsCoordinates().getLongitude(), is(1.5));
        assertThat(retrievedEvent.getDescription(), is(description));
        assertThat(retrievedEvent.getOwnerId(), is(ownerId));
    }


    @Test
    public void testManualSaveAndGetCorrectValues() {
        String title = "Tenis match";
        String description = "Search for tenis partner";

        Event e = new Event(title, new GPSCoordinates(1.5, 1.5), description, EditProfileActivity.NULL_USER, "1");
        EventCreatorModel model = new EventCreatorModel();
        model.saveValue(e);

        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getValue("1")).join();

        assertThat(foundEvent.getTitle(), is(title));
        assertThat(foundEvent.getEventId(), is("1"));
        assertThat(foundEvent.getGpsCoordinates().getLatitude(), is(1.5));
        assertThat(foundEvent.getGpsCoordinates().getLongitude(), is(1.5));
        assertThat(foundEvent.getDescription(), is(description));
        assertThat(foundEvent.getOwnerId(), is(EditProfileActivity.NULL_USER));
    }

    @Test
    public void doesNotSaveNullEvent() {
        Event e = null;
        EventCreatorModel model = new EventCreatorModel();
        assertFalse(model.saveValue(e));

    }

    @Test
    public void retrievingNonEventsHasNullAttributes() {

        EventCreatorModel model = new EventCreatorModel();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Events").child("NotEid").setValue(new Profile());


        Event foundEvent = LiveDataTestUtil.toCompletableFuture(model.getValue("NotEid")).join();


        assertThat(foundEvent.getTitle(), is(Event.NULL_EVENT.getTitle()));
        assertThat(foundEvent.getDescription(), is(Event.NULL_EVENT.getDescription()));
        assertThat(foundEvent.getEventId(), is(Event.NULL_EVENT.getEventId()));
        assertThat(foundEvent.getGpsCoordinates(), is(Event.NULL_EVENT.getGpsCoordinates()));
        assertThat(foundEvent.getOwnerId(), is(EditProfileActivity.NULL_USER));
    }


}
