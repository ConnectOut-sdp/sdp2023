package com.sdpteam.connectout.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;

import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class EventCreatorViewModelTest {
    public static final Event TEST_EVENT1 = new Event("1", "Tenis", "Searching for a tenis partner", new GPSCoordinates(10, 10), "Eric");
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void testSavesValueAddsToExistingStore() {

        EventCreatorViewModelTest.TestEventCreatorModel model = new TestEventCreatorModel();
        EventCreatorViewModel viewModel = new EventCreatorViewModel(model);
        Event event = new Event(viewModel.getUniqueId(), "Climbing", "Searching for a climbing group", new GPSCoordinates(10, 10), "Eric");

        viewModel.saveEvent(event);
        assertTrue(TestEventCreatorModel.EVENT_LIST.contains(event));
    }

    @Test
    public void testGetValueWithEIdFindsCorrectEvent() {
        EventCreatorViewModelTest.TestEventCreatorModel model = new TestEventCreatorModel();
        EventCreatorViewModel viewModel = new EventCreatorViewModel(model);

        LiveData<Event> mutableLiveData = viewModel.getEventLiveData();
        viewModel.getEvent("1");

        Event e = LiveDataTestUtil.getOrAwaitValue(mutableLiveData);

        assertThat(e, is(TEST_EVENT1));
    }

    @Test
    public void testGetValueWithUIAndTitleFindsCorrectEvent() {
        EventCreatorViewModelTest.TestEventCreatorModel model = new TestEventCreatorModel();
        EventCreatorViewModel viewModel = new EventCreatorViewModel(model);

        MutableLiveData<Event> liveData = viewModel.getEventLiveData();
        viewModel.getEvent("Eric", "Tenis");
        Event e = LiveDataTestUtil.getOrAwaitValue(liveData);

        assertThat(e, is(TEST_EVENT1));
    }

    public static class TestEventCreatorModel implements EventRepository {

        public static final List<Event> EVENT_LIST = createTestList();

        private static List<Event> createTestList() {
            List<Event> eventList = new ArrayList<>();
            eventList.add(TEST_EVENT1);
            return eventList;
        }

        @Override
        public boolean saveEvent(Event event) {
            EVENT_LIST.add(event);
            return true;
        }

        @Override
        public CompletableFuture<Event> getEvent(String eid) {
            List<Event> filtered = EVENT_LIST.stream().filter(e -> e.getId().equals(eid)).collect(Collectors.toList());
            return filtered.isEmpty() ? null : CompletableFuture.completedFuture(filtered.get(0));
        }

        @Override
        public CompletableFuture<Event> getEvent(String uid, String title) {
            List<Event> filtered = EVENT_LIST.stream().filter(e -> e.getOrganizer().equals(uid) && e.getTitle().equals(title)).collect(Collectors.toList());
            return filtered.isEmpty() ? null : CompletableFuture.completedFuture(filtered.get(0));
        }

        @Override
        public String getUniqueId() {
            return UUID.randomUUID().toString();
        }

        @Override
        public CompletableFuture<List<Event>> getEventsByFilter(String filteredAttribute, String expectedValue) {
            return CompletableFuture.completedFuture(EVENT_LIST);
        }
    }
}
