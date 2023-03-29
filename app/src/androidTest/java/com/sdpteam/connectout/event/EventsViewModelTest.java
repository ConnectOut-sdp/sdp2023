package com.sdpteam.connectout.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Rule;
import org.junit.Test;

import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

public class EventsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void getEventListReturnsTheCorrectEvents() {
        EventsViewModel viewModel = new EventsViewModel(new FakeMapModelManager());
        LiveData<List<Event>> eventListLiveData = viewModel.getEventListLiveData();

        List<Event> events = LiveDataTestUtil.getOrAwaitValue(eventListLiveData);
        assertThat(events.size(), is(2));
        assertThat(events.get(0).getTitle(), is("event1"));
        assertThat(events.get(1).getTitle(), is("event2"));
    }

    @Test
    public void testMapViewModelWithRefresh() {
        EventsViewModel mvm = new EventsViewModel(new FakeMapModelManager());
        LiveDataTestUtil.getOrAwaitValue(mvm.getEventListLiveData());

        LiveData<List<Event>> liveData = mvm.getEventListLiveData();
        mvm.refreshEventList();
        List<Event> eventList = LiveDataTestUtil.getOrAwaitValue(liveData);

        assertThat(eventList.size(), is(3));
        assertThat(eventList.get(0).getTitle(), is("event3"));
        assertThat(eventList.get(1).getTitle(), is("event4"));
        assertThat(eventList.get(2).getTitle(), is("event5"));
    }

    public static class FakeMapModelManager implements EventRepository {
        boolean firstUpdate = true;
        private ArrayList<Event> dataSet = new ArrayList<>();

        public FakeMapModelManager() {
            dataSet.add(new Event("1", "event1", "", new GPSCoordinates(0, 1), "a"));
            dataSet.add(new Event("2", "event2", "", new GPSCoordinates(2, 3), "b"));
        }

        @Override
        public boolean saveEvent(Event event) {
            return false;
        }

        @Override
        public CompletableFuture<Event> getEvent(String eventId) {
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

        public CompletableFuture<List<Event>> getEventsByFilter(String filteredAttribute, String expectedValue) {
            updateData();
            return CompletableFuture.completedFuture(dataSet);
        }

        private void updateData() {
            if (firstUpdate) {
                firstUpdate = false;
                return;
            }
            ArrayList<Event> testList = new ArrayList<>();
            testList.add(new Event("3", "event3", "", new GPSCoordinates(46.521, 6.5678), "E3"));
            testList.add(new Event("4", "event4", "", new GPSCoordinates(46.5215, 6.56785), "E4"));
            testList.add(new Event("5", "event5", "", new GPSCoordinates(46.5218, 6.5679), "E5"));

            dataSet = testList;
        }
    }
}
