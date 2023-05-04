package com.sdpteam.connectout.event;

import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventDataSource {

    /**
     * Saves the given Event in the firebase database
     *
     * @param event (Event): The given event to save
     * @return (boolean): True if value is saved
     * <p>
     * /!\ the save return value will be useful for the offline mode /!\
     */
    boolean saveEvent(Event event);

    /**
     * Fetches the wanted Event from the firebase database using its id.
     *
     * @param eventId (String): Unique identifier of the event
     * @return (LiveData < Event >): Container of the wanted event
     */
    CompletableFuture<Event> getEvent(String eventId);

    /**
     * @param eventId       (String): Id of the event to which we add the participant
     * @param participantId (String): Id of the added participant
     * @return (CompletableFuture < Boolean >): completes to true if participant has joined the event.
     */
    CompletableFuture<Boolean> joinEvent(String eventId, String participantId);

    /**
     * @param eventId (String): Id of the event to which we add the participant as interested
     * @param participantId (String): Id of the interested participant
     * @return (CompletableFuture < Boolean >): completes to true if participant has joined the event as interested.
     */
    CompletableFuture<Boolean> joinEventAsInterested(String eventId, String participantId);

    /**
     * @param eventId       (String): Id of the event to which we remove the participant
     * @param participantId (String): Id of the removed participant
     * @return (CompletableFuture < Boolean >): completes to true if participant has left the event.
     */
    CompletableFuture<Boolean> leaveEvent(String eventId, String participantId);

    /**
     * Fetches the wanted Event from the firebase database using its owner id & title.
     *
     * @param userId (String): Id of the owner of the event
     * @param title  (Title): Title of the event
     * @return (LiveData < Event >): Container of the wanted event
     */
    CompletableFuture<Event> getEvent(String userId, String title);

    /**
     * @return (String): Event Id that is truly unique in the model.
     */
    String getUniqueId();

    /**
     * @param eventFilter    (EventFilter): Custom filter to apply upon the event's attribute
     * @param profilesFilter (ProfilesFilter): Custom filter to apply upon the participants profile's attribute
     * @return (CompletableFuture < List < Event > >): a changeable list of different events.
     */
    CompletableFuture<List<Event>> getEventsByFilter(EventFilter eventFilter, ProfilesFilter profilesFilter);

    void saveEventRestrictions(String eventId, Event.EventRestrictions restrictions);

    /**
     * Deletes the given Event in the firebase database
     *
     * @param eventId (String): The given eventId to delete
     * @return (boolean): True if value is deleted
     * <p>
     * /!\ the delete return value will be useful for the offline mode /!\
     */
    boolean deleteEvent(String eventId);
}
