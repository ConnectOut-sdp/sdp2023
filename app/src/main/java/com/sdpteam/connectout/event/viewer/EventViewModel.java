package com.sdpteam.connectout.event.viewer;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventRepository;


public class EventViewModel extends ViewModel {

    private final EventRepository model;
    private final MutableLiveData<Event> event;
    private String lastEventId;

    public EventViewModel(EventRepository model) {
        this.model = model;
        event = new MutableLiveData<>();
    }

    public  MutableLiveData<Event> getEventLiveData(){
        return event;
    }
    public void getEvent(String eventId) {
        lastEventId = eventId;
        model.getEvent(eventId).thenAccept(event::setValue);
    }

    public void joinEvent(){
        participateToEvent(true);
    }
    public void leaveEvent(){
        participateToEvent(false);
    }

    private void participateToEvent(boolean isParticipating){
        AuthenticatedUser au = new GoogleAuth().loggedUser();
        if(au != null && lastEventId != null) {
            String currentUserId = au.uid;
            if (isParticipating) {

                model.joinEvent(lastEventId, currentUserId);
            } else {
                model.leaveEvent(lastEventId, currentUserId);
            }
        }
    }
}
