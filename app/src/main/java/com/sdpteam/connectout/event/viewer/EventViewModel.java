package com.sdpteam.connectout.event.viewer;

import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.chat.ChatActivity;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventRepository;
import com.sdpteam.connectout.event.creator.SetEventRestrictionsActivity;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileViewModel;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import com.sdpteam.connectout.event.EventDataSource;

public class EventViewModel extends ViewModel {

    private final EventDataSource eventDataSource;
    private final MutableLiveData<Event> eventLiveData;
    private String lastEventId;

    public EventViewModel(EventDataSource eventDataSource) {
        this.eventDataSource = eventDataSource;
        eventLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Event> getEventLiveData() {
        return eventLiveData;
    }

    public void getEvent(String eventId) {
        lastEventId = eventId;
        eventDataSource.getEvent(eventId).thenAccept(eventLiveData::setValue);
    }

    /**
     * Fetches the event with the last stored eventId and updates the eventLiveData accordingly.
     */
    public void refreshEvent() {
        eventDataSource.getEvent(lastEventId).thenAccept(eventLiveData::setValue);
    }

    /**
     * Updates the participation status of the specified user in the event to "attending".
     *
     * @param userId (String): id of the user to add to the event
     * @return (LiveData < Boolean >): true if participation status has been joined.
     */
    public LiveData<Boolean> joinEvent(String userId) {
        return updateParticipationStatus(userId, true);
    }

    /**
     * Updates the participation status of the specified user in the event to "not attending".
     *
     * @param userId (String): id of the user to remove from the event
     * @return (LiveData < Boolean >): true if participation status has been left.
     */
    public LiveData<Boolean> leaveEvent(String userId) {
        return updateParticipationStatus(userId, false);
    }

    /**
     * Toggles the participation status of the specified user in the event,
     * if the user is attending/left the event, it removes/add the user from the event.
     *
     * @param userId (String): user's id, kept for backward compatibility issues with tests
     * @param profileViewModel (ProfileViewModel): user whose participation status needs to be toggled
     * @param impossibleRegistrationToast (Consumer<String>): creates Toast in case registration is impossible
     * @param isRegistrationPossible (BiFunction<Profile, Event, Event.EventRestrictions.RestrictionStatus>):
     *                               returns the status of the registration
     * @return (LiveData<Boolean>): upon update, true if participation status has been changed.
     */
    public LiveData<Boolean> toggleParticipation(String userId, ProfileViewModel profileViewModel, Consumer<String> impossibleRegistrationToast,
                                                 BiFunction<Profile, Event, Event.EventRestrictions.RestrictionStatus> isRegistrationPossible,
                                                 Consumer<Event> intentSetEventRestrictions) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();
        eventDataSource.getEvent(lastEventId).thenAccept(event ->{
            if (event.getOrganizer().equals(userId)){
                //if the user is the organizer, he can't join or leave the event, but clicking on the button enables restriction editing
                intentSetEventRestrictions.accept(event);
                result.setValue(true);
            }
            else{
                boolean userRegistered = event.getParticipants().contains(userId);
                if(!userRegistered){
                    profileViewModel.fetchProfile(userId);
                    profileViewModel.getProfileLiveData().observeForever(profile -> {
                        Event.EventRestrictions.RestrictionStatus status = isRegistrationPossible.apply(profile, event);
                        if (status != Event.EventRestrictions.RestrictionStatus.ALL_RESTRICTIONS_SATISFIED){
                            impossibleRegistrationToast.accept(status.getMessage());
                            result.setValue(false);
                        }
                        else{
                            updateParticipationStatus(userId, !userRegistered).observeForever(result::setValue);
                        }

                    });
                }
                else{
                    updateParticipationStatus(userId, !userRegistered).observeForever(result::setValue);
                }
            }
        });

        return result;
    }

    /**
     * Updates the participation status of the specified user in the event, and refreshes the eventLiveData with the updated event.
     *
     * @param userId          (String): id of the user whose participation status needs to be updated
     * @param isParticipating (boolean): true if the user is attending the event, false otherwise
     * @return (LiveData < Boolean >): upon update, true if participation status has been changed.
     */
    private LiveData<Boolean> updateParticipationStatus(String userId, boolean isParticipating) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        // Check if an event is selected
        if (lastEventId != null) {
            if (isParticipating) {
                eventDataSource.joinEvent(lastEventId, userId).thenAccept(b ->
                        {
                            refreshEvent();
                            result.setValue(b);
                        }
                );
            } else {
                eventDataSource.leaveEvent(lastEventId, userId).thenAccept(b -> {
                    refreshEvent();
                    result.setValue(b);
                });
            }
        }
        return result;
    }

    public void saveEventRestrictions(String eventId, Event.EventRestrictions restrictions){
        eventRepository.saveEventRestrictions(eventId, restrictions);
    }
}
