package com.sdpteam.connectout;

import static com.sdpteam.connectout.LiveDataTestUtil.toCompletableFuture;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MapViewModelTest {

    @Test
    public void getEventListReturnsTheCorrectEvents() {
        MapViewModel viewModel = new MapViewModel();
        viewModel.init(new FakeMapModel());
        LiveData<List<Event>> eventListLiveData = viewModel.getEventList();

        CompletableFuture<List<Event>> eventListFuture = toCompletableFuture(eventListLiveData);

        List<Event> events = eventListFuture.join();
        assertThat(events.size(), is(2));
        assertThat(events.get(0).getTitle(), is("event1"));
        assertThat(events.get(1).getTitle(), is("event2"));
    }

    @Test
    public void setEventListToNull() {
        MapViewModel viewModel = new MapViewModel();
        viewModel.init(new FakeMapModel());
        viewModel.setEventList(null);
        assertThat(viewModel.getEventList(), notNullValue());
    }

    @Test
    public void setEventListToNullAfterMakingAgetEventList() {
        MapViewModel viewModel = new MapViewModel();
        viewModel.init(new FakeMapModel());
        LiveData<List<Event>> eventListLiveData = viewModel.getEventList();

        CompletableFuture<List<Event>> eventListFuture = toCompletableFuture(eventListLiveData);

        List<Event> events = eventListFuture.join();
        assertThat(events.size(), is(2));
        assertThat(events.get(0).getTitle(), is("event1"));
        assertThat(events.get(1).getTitle(), is("event2"));

        viewModel.setEventList(null);
        assertThat(events.size(), is(2));
        assertThat(events.get(0).getTitle(), is("event1"));
        assertThat(events.get(1).getTitle(), is("event2"));
    }

    @Test
    public void testMapViewModelWithRefresh() {
        MapViewModel mvm = new MapViewModel();
        mvm.init(new FakeMapModel());
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

    public static class FakeMapModel implements InterfaceMapModel {
        boolean firstUpdate = true;
        private ArrayList<Event> dataSet = new ArrayList<>();

        public FakeMapModel() {
            dataSet.add(new Event("event1", 0, 1));
            dataSet.add(new Event("event2", 2, 3));
        }

        public MutableLiveData<List<Event>> getEventLiveList() {
            updateData();
            MutableLiveData<List<Event>> data = new MutableLiveData<>();
            data.postValue(dataSet);
            return data;
        }

        private void updateData() {
            if (firstUpdate) {
                firstUpdate = false;
                return;
            }
            ArrayList<Event> testList = new ArrayList<>();
            testList.add(new Event("event3", 46.521, 6.5678));
            testList.add(new Event("event4", 46.5215, 6.56785));
            testList.add(new Event("event5", 46.5218, 6.5679));

            dataSet = testList;
        }
    }
}
