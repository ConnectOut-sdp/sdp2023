package com.sdpteam.connectout.mapList;

import static com.sdpteam.connectout.utils.LiveDataTestUtil.toCompletableFuture;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.mapList.map.GPSCoordinates;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MapListViewModelTest {

    @Test
    public void getEventListReturnsTheCorrectEvents() {
        MapListViewModel viewModel = new MapListViewModel(new FakeMapModelManager());
        LiveData<List<Event>> eventListLiveData = viewModel.getEventList();

        CompletableFuture<List<Event>> eventListFuture = toCompletableFuture(eventListLiveData);

        List<Event> events = eventListFuture.join();
        assertThat(events.size(), is(2));
        assertThat(events.get(0).getTitle(), is("event1"));
        assertThat(events.get(1).getTitle(), is("event2"));
    }

    @Test
    public void testMapViewModelWithRefresh() {
        MapListViewModel mvm = new MapListViewModel(new FakeMapModelManager());
        LiveData<List<Event>> events = mvm.getEventList();
        CompletableFuture<List<Event>> future1 = toCompletableFuture(events);
        future1.join();

        LiveData<List<Event>> liveData = mvm.refreshEventList();
        CompletableFuture<List<Event>> future2 = toCompletableFuture(liveData);
        List<Event> eventList = future2.join();

        assertThat(eventList.size(), is(3));
        assertThat(eventList.get(0).getTitle(), is("event3"));
        assertThat(eventList.get(1).getTitle(), is("event4"));
        assertThat(eventList.get(2).getTitle(), is("event5"));
    }

    public static class FakeMapModelManager implements MapListModelManager {
        boolean firstUpdate = true;
        private ArrayList<Event> dataSet = new ArrayList<>();

        public FakeMapModelManager() {
            dataSet.add(new Event("1", "event1", "", new GPSCoordinates(0, 1), "a"));
            dataSet.add(new Event("2", "event2", "", new GPSCoordinates(2, 3), "b"));
        }

        public MutableLiveData<List<Event>> getEventLiveList(String filteredAttribute, String expectedValue) {
            updateData();
            return new MutableLiveData<>(dataSet);
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