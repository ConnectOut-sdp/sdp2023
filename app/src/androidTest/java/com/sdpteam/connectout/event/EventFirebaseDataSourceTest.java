package com.sdpteam.connectout.event;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static com.sdpteam.connectout.profile.Profile.NULL_USER;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EventFirebaseDataSourceTest {

    private final String eventId1 = "A_" + generateRandomPath();
    private final String eventId2 = "A_" + generateRandomPath();
    private final String eventId3 = "A_" + generateRandomPath();

    private final EventFirebaseDataSource model = new EventFirebaseDataSource();

    @After
    public void cleanup() {
        model.deleteEvent(eventId1);
        model.deleteEvent(eventId2);
        model.deleteEvent(eventId3);
        new ProfileFirebaseDataSource().deleteProfile(eventId2);
        waitABit();
    }

    @Test
    public void modelReturnNullOnNonExistingEventEId() {

        Event foundEvent = fJoin(model.getEvent("invalid"));
        assertNull(foundEvent);
    }

    @Test
    public void modelReturnNullOnNonExistingEventUId() {

        Event foundEvent = fJoin(model.getEvent("invalid", "no title"));
        assertNull(foundEvent);
    }

    @Test
    public void testGetValueWithValidUidAndTitle() {
        String title = "Tennis match";
        GPSCoordinates gpsCoordinates = new GPSCoordinates(1.5, 1.5);
        String description = "Looking for a tennis partner";
        String ownerId = "user1";
        Event event = new Event(eventId1, title, description, gpsCoordinates, ownerId);
        model.saveEvent(event);
        waitABit();
        // retrieve the event from the database using its owner ID and title and check that it matches the original event
        Event retrievedEvent = fJoin(model.getEvent(ownerId, title));
        assertThat(retrievedEvent.getTitle(), is(title));
        assertThat(retrievedEvent.getId(), is(eventId1));
        assertThat(retrievedEvent.getCoordinates().getLatitude(), is(1.5));
        assertThat(retrievedEvent.getCoordinates().getLongitude(), is(1.5));
        assertThat(retrievedEvent.getDescription(), is(description));
        assertThat(retrievedEvent.getOrganizer(), is(ownerId));
    }

    @Test
    public void testManualSaveAndGetCorrectValues() {
        String title = "Tenis match";
        String description = "Search for tenis partner";

        Event e = new Event(eventId1, title, description, new GPSCoordinates(1.5, 1.5), NULL_USER);
        model.saveEvent(e);
        waitABit();
        Event foundEvent = fJoin(model.getEvent(eventId1));

        assertThat(foundEvent.getTitle(), is(title));
        assertThat(foundEvent.getId(), is(eventId1));
        assertThat(foundEvent.getCoordinates().getLatitude(), is(1.5));
        assertThat(foundEvent.getCoordinates().getLongitude(), is(1.5));
        assertThat(foundEvent.getDescription(), is(description));
        assertThat(foundEvent.getOrganizer(), is(NULL_USER));
    }

    @Test
    public void testManualSaveAndGetOnlyCorrectUId() {
        String title = "Tenis match";
        String description = "Search for tenis partner";

        Event e = new Event(eventId1, title, description, new GPSCoordinates(1.5, 1.5), NULL_USER);
        model.saveEvent(e);
        waitABit();
        Event foundEvent = fJoin(model.getEvent(NULL_USER, "wrong title"));

        assertNull(foundEvent);
    }

    @Test
    public void testManualSaveAndGetOnlyCorrectTitle() {
        String title = "Tenis match";
        String description = "Search for tenis partner";

        Event e = new Event(eventId1, title, description, new GPSCoordinates(1.5, 1.5), NULL_USER);
        model.saveEvent(e);
        waitABit();
        Event foundEvent = fJoin(model.getEvent("wrong id", title));

        assertNull(foundEvent);
    }

    @Test
    public void testGetWithFilters() {
        final Event e1 = new Event(eventId1, "judo", "", new GPSCoordinates(1.5, 1.5), "");
        final Event e2 = new Event(eventId2, "tennis", "", new GPSCoordinates(1.5, 1.5), "");
        final Event e3 = new Event(eventId3, "football", "", new GPSCoordinates(1.5, 1.5), "");
        Profile p = new Profile(eventId2, "okok", "okok@gmail.com", "okok okok", Profile.Gender.FEMALE, 3.3, 6, "");
        fJoin(new ProfileFirebaseDataSource().saveProfile(p));
        e1.addParticipant(p.getId());
        e2.addParticipant(p.getId());

        final EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(e1);
        model.saveEvent(e2);
        model.saveEvent(e3);
        waitABit();

        final EventFilter filter = e -> eventId1.equals(e.getId()) || eventId2.equals(e.getId());
        final List<Event> results = fJoin(model.getEventsByFilter(filter));

        assertEquals(2, results.size());
        assertFalse(results.get(0).getParticipants().isEmpty());
        // ::test needed for CI
        assertTrue(results.stream().allMatch(filter::test));
    }

    @Test
    public void doesNotSaveNullEvent() {
        Event e = null;
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        assertFalse(model.saveEvent(e));
    }

    @Test
    public void retrievingNonEventsIsNull() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Events").child("NotEid").setValue(new ArrayList<>());

        Event foundEvent = fJoin(model.getEvent("NotEid"));

        assertNull(foundEvent);
    }

    @Test
    public void joinsEventCorrectly() throws ExecutionException, InterruptedException, TimeoutException {
        final Event e = new Event(eventId1, "judo", "", new GPSCoordinates(1.5, 1.5), "");
        model.saveEvent(e);
        waitABit();

        fJoin(model.joinEvent(eventId1, "14"));
        fJoin(model.joinEvent(eventId1, "13"));
        fJoin(model.joinEvent(eventId1, "15"));

        Event obtained = model.getEvent(eventId1).get(2, TimeUnit.SECONDS);
        assertThat(obtained.getParticipants().size(), is(3));
    }

    @Test
    public void joinsFailWithExistingParticipant() {
        final Event e = new Event(eventId1, "judo", "", new GPSCoordinates(1.5, 1.5), "");
        model.saveEvent(e);
        waitABit();

        fJoin(model.leaveEvent(eventId1, "14"));

        assertTrue(fJoin(model.joinEvent(eventId1, "14")));
        assertFalse(fJoin(model.joinEvent(eventId1, "14")));
    }

    @Test
    public void releaseEventCorrectly() {
        final Event e = new Event(eventId1, "judo", "", new GPSCoordinates(1.5, 1.5), "");
        model.saveEvent(e);
        waitABit();

        CompletableFuture<Boolean> f1 = model.joinEvent(eventId1, "14");
        CompletableFuture<Boolean> f2 = model.joinEvent(eventId1, "13");
        CompletableFuture<Boolean> f3 = model.joinEvent(eventId1, "15");

        fJoin(f1);
        fJoin(f2);
        fJoin(f3);
        Event obtained = fJoin(model.getEvent(eventId1));
        assertThat(obtained.getParticipants().size(), is(3));

        f1 = model.leaveEvent(eventId1, "14");
        f2 = model.leaveEvent(eventId1, "13");
        f3 = model.leaveEvent(eventId1, "15");

        fJoin(f1);
        fJoin(f2);
        fJoin(f3);

        obtained = fJoin(model.getEvent(eventId1));
        assertThat(obtained.getParticipants().size(), is(0));
    }

    @Test
    public void leaveFailWithNonExistingParticipant() {
        final Event e = new Event(eventId1, "judo", "", new GPSCoordinates(1.5, 1.5), "");
        model.saveEvent(e);
        waitABit();

        fJoin(model.leaveEvent(eventId1, "14"));

        assertFalse(fJoin(model.leaveEvent(eventId1, "14")));
    }

    @Test
    public void deleteEvent() {
        final Event e = new Event(eventId1, "judo", "", new GPSCoordinates(1.5, 1.5), "");
        model.saveEvent(e);
        waitABit();
        assertTrue((model.deleteEvent(eventId1)));
    }
}
