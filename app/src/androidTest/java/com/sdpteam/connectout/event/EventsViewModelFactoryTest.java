package com.sdpteam.connectout.event;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Rule;
import org.junit.Test;

import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModelFactory;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.profile.ProfileViewModel;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.ViewModel;

public class EventsViewModelFactoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void testFailsToCreateViewOnWrongArgument() {
        EventsViewModelFactory mf = new EventsViewModelFactory(new MockModel());
        assertThrows(IllegalArgumentException.class, () -> mf.create(ProfileViewModel.class));
    }

    @Test
    public void createViewOnCorrectModelArgument() {
        EventRepository mockModel = new MockModel();
        // Implicitly instantiating EventsViewModel to use that instance back in MapViewFragment
        assertThat(new EventsViewModelFactory(mockModel).create(EventsViewModel.class), isA(EventsViewModel.class));
    }

    @Test
    public void testViewModelUsesCustomModelThroughFactoryInstantiation() {
        final EventsViewModelFactory factory = new EventsViewModelFactory(new MockModel());
        final EventsViewModel mapViewModel = factory.create(EventsViewModel.class);
        final List<Event> events = LiveDataTestUtil.getOrAwaitValue(mapViewModel.getEventListLiveData());

        assertEquals(2, events.size());
        assertEquals("Event 1", events.get(0).getTitle());
        assertEquals("Event 2", events.get(1).getTitle());
    }

    @Test
    public void testFactoryCannotInstantiateViewModelWhenModelClassIsInvalid() {
        final EventsViewModelFactory factory = new EventsViewModelFactory(new MockModel());
        assertThrows(IllegalArgumentException.class, () -> factory.create(InvalidViewModel.class));
    }

    private static class MockModel implements EventRepository {

        @Override
        public boolean saveEvent(Event event) {
            return false;
        }

        @Override
        public CompletableFuture<Event> getEvent(String eventId) {
            return null;
        }

        @Override
        public CompletableFuture<Boolean> joinEvent(String eventId, String participantId) {
            return null;
        }

        @Override
        public CompletableFuture<Boolean> leaveEvent(String eventId, String participantId) {
            return null;
        }

        @Override
        public CompletableFuture<Event> getEvent(String userId, String title) {
            return null;
        }

        @Override
        public String getUniqueId() {
            return null;
        }

        @Override
        public CompletableFuture<List<Event>> getEventsByFilter(EventFilter eventFilter, ProfilesFilter profilesFilter) {
            return CompletableFuture.completedFuture(Arrays.asList(
                    new Event("1", "Event 1", "Description 2", new GPSCoordinates(0, 0), "a"),
                    new Event("2", "Event 2", "Description 2", new GPSCoordinates(1, 1), "b")
            ));
        }
    }

    private static class InvalidViewModel extends ViewModel {
    }
}
