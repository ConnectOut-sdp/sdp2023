package com.sdpteam.connectout.event.creator;

import static com.sdpteam.connectout.profile.Profile.NULL_USER;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventDataSource;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.notifications.EventNotificationManager;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.RegisteredEvent;
import com.sdpteam.connectout.remoteStorage.FileStorageFirebase;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;

public class EventCreatorViewModel extends EventsViewModel {
    private final EventDataSource model;
    private final MutableLiveData<Event> eventLiveData;

    public EventCreatorViewModel(EventDataSource model) {
        super(model);
        this.model = model;
        this.eventLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Event> getEventLiveData() {
        return eventLiveData;
    }

    public void getEvent(String eventId) {
        model.getEvent(eventId).thenAccept(eventLiveData::setValue);
    }

    public void getEvent(String userId, String title) {
        model.getEvent(userId, title).thenAccept(eventLiveData::setValue);
    }

    public Boolean saveEvent(Event event) {
        boolean result = model.saveEvent(event);
        if (result) {
            subscribeToEvent(event);
        }
        return result;
    }

    /**
     * @param title       (String): title of the event
     * @param coordinates (GPSCoordinates): position of the event
     * @param description (String): description of the event
     * @param date        (long): date of the event
     * @param imageUrl    (String): url of the event image
     */
    public void saveEvent(String title, GPSCoordinates coordinates, String description, long date, String imageUrl) {
        AuthenticatedUser user = new GoogleAuth().loggedUser();
        String ownerId = (user == null) ? NULL_USER : user.uid;

        ArrayList<String> participants = new ArrayList<>(singletonList(ownerId));
        String eventId = getUniqueId();
        new ProfileFirebaseDataSource().registerToEvent(new RegisteredEvent(eventId), ownerId); //owner is registered by default
        Event newEvent = new Event(eventId, title, description, coordinates, ownerId, participants, new ArrayList<>(), date, new Event.EventRestrictions(), imageUrl);

        saveEvent(newEvent);
    }

    private void subscribeToEvent(Event event) {
        EventNotificationManager manager = new EventNotificationManager();
        manager.subscribeToEventTopic(event.getId());
    }

    public String getUniqueId() {
        return model.getUniqueId();
    }

    public CompletableFuture<String> uploadImage(Uri selectedImageUri) {
        if (selectedImageUri == null) {
            return CompletableFuture.completedFuture(null);
        }
        return new FileStorageFirebase().uploadFile(selectedImageUri, "jpg").thenApply(Uri::toString);
    }
}
