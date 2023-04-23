package com.sdpteam.connectout.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Rule;
import org.junit.Test;

import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.event.viewer.EventViewModel;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

public class EventViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void getEventReturnsTheCorrectEvent() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        viewModel.getEvent("1");
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();

        Event event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event1"));
    }

    @Test
    public void NothingOccursWithNonGivenId() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();

        assertThrows(RuntimeException.class, () -> LiveDataTestUtil.getOrAwaitValue(eventLiveData));
    }

    @Test
    public void reusesLastGivenId() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();
        viewModel.getEvent("1");
        viewModel.getEvent("2");

        Event event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event2"));
    }

    @Test
    public void JoinsWithLastGivenId() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();
        viewModel.getEvent("1");
        viewModel.getEvent("2");
        viewModel.joinEvent("3");

        Event event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event2"));
        assertTrue(event.getParticipants().contains("3"));
    }

    @Test
    public void LeavesWithLastGivenId() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();
        viewModel.getEvent("1");
        viewModel.getEvent("2");
        viewModel.joinEvent("3");

        Event event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event2"));
        assertTrue(event.getParticipants().contains("3"));
        viewModel.leaveEvent("3");

        event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event2"));
        assertFalse(event.getParticipants().contains("3"));
    }

    @Test
    public void doesNotJoinsWithNoLastGivenId() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();
        viewModel.joinEvent("3");

        for (int i = 1; i < 6; i++) {
            viewModel.getEvent(Integer.toString(i));
            Event event = LiveDataTestUtil.getOrAwaitValue(viewModel.getEventLiveData());
            assertFalse(event.getParticipants().contains("3"));
        }
    }

    @Test
    public void doesNotLeaveWithNoLastGivenId() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());

        viewModel.leaveEvent("3");

        for (int i = 1; i < 6; i++) {
            viewModel.getEvent(Integer.toString(i));
            Event event = LiveDataTestUtil.getOrAwaitValue(viewModel.getEventLiveData());
            assertFalse(event.getParticipants().contains("3"));
        }
    }

    @Test
    public void testMapViewModelWithRefresh() {
        EventViewModel mvm = new EventViewModel(new EventViewModelTest.FakeModel());

        mvm.getEvent("3");
        LiveData<Event> liveData = mvm.getEventLiveData();
        mvm.getEvent("4");
        mvm.refreshEvent();

        Event event = LiveDataTestUtil.getOrAwaitValue(liveData);
        assertThat(event.getTitle(), is("event4"));
    }

    @Test
    public void joinsAndLeavesEventCorrectly() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        viewModel.getEvent("1");
        viewModel.joinEvent("3");

        LiveData<Event> eventLiveData = viewModel.getEventLiveData();
        Event event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event1"));
        assertTrue(event.getParticipants().contains("3"));
        viewModel.leaveEvent("3");

        viewModel.refreshEvent();
        event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event1"));
        assertFalse(event.getParticipants().contains("3"));
    }

    @Test
    public void toggleParticipationJoinsEventWhenNotParticipating() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();
        viewModel.getEvent("1");
        viewModel.toggleParticipation("3");

        Event event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event1"));
        assertTrue(event.getParticipants().contains("3"));
    }

    @Test
    public void toggleParticipationLeavesEventWhenParticipating() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();
        viewModel.getEvent("1");
        viewModel.joinEvent("3");
        viewModel.toggleParticipation("3");

        Event event = LiveDataTestUtil.getOrAwaitValue(eventLiveData);

        assertThat(event.getTitle(), is("event1"));
        assertFalse(event.getParticipants().contains("3"));
    }

    @Test
    public void toggleParticipationDoesNothingWhenNoLastGivenId() {
        EventViewModel viewModel = new EventViewModel(new EventViewModelTest.FakeModel());
        LiveData<Event> eventLiveData = viewModel.getEventLiveData();
        viewModel.toggleParticipation("3");

        for (int i = 1; i < 6; i++) {
            viewModel.getEvent(Integer.toString(i));
            Event event = LiveDataTestUtil.getOrAwaitValue(viewModel.getEventLiveData());
            assertFalse(event.getParticipants().contains("3"));
        }
    }

    public static class FakeModel implements EventRepository {
        boolean firstUpdate = true;
        private ArrayList<Event> dataSet = new ArrayList<>();

        public FakeModel() {
            dataSet.add(new Event("1", "event1", "", new GPSCoordinates(0, 1), "a"));
            dataSet.add(new Event("2", "event2", "", new GPSCoordinates(2, 3), "b"));
            dataSet.add(new Event("3", "event3", "", new GPSCoordinates(46.521, 6.5678), "E3"));
            dataSet.add(new Event("4", "event4", "", new GPSCoordinates(46.5215, 6.56785), "E4"));
            dataSet.add(new Event("5", "event5", "", new GPSCoordinates(46.5218, 6.5679), "E5"));
        }

        @Override
        public boolean saveEvent(Event event) {
            return false;
        }

        @Override
        public CompletableFuture<Event> getEvent(String eventId) {

            return CompletableFuture.completedFuture(findEvent(eventId));
        }

        @Override
        public CompletableFuture<Boolean> joinEvent(String eventId, String participantId) {
            return CompletableFuture.completedFuture(findEvent(eventId).addParticipant(participantId));
        }

        @Override
        public CompletableFuture<Boolean> leaveEvent(String eventId, String participantId) {
            return CompletableFuture.completedFuture(findEvent(eventId).removeParticipant(participantId));
        }

        @Override
        public CompletableFuture<Event> getEvent(String userId, String title) {
            return null;
        }

        @Override
        public String getUniqueId() {
            return null;
        }

        public CompletableFuture<List<Event>> getEventsByFilter(EventFilter eventFilter, ProfilesFilter profilesFilter) {
            return null;
        }

        private Event findEvent(String eventId) {
            if (eventId == null) {
                return null;
            }
            int idx = Integer.parseInt(eventId) - 1;
            return dataSet.get(idx);
        }
    }
}
