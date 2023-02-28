package com.sdpteam.connectout;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MapViewModelTest {
    /*@Rule
    public TestRule rule = new InstantTaskExecutorRule();
*/
    @Test
    public void testMapViewModel() {
        MapViewModel mvm = new MapViewModel();
        mvm.init(new FakeMapModel());
        LiveData<List<Event>> events = mvm.getEventList();
        //LiveData is asychronous so calling getValue may return null at this stage
        final LiveData<List<Event>> events1 = events;
        final CountDownLatch latch1 = new CountDownLatch(1);
        Observer<List<Event>> observer1 = listLiveData -> {
            latch1.countDown(); // this releases all the threads
            assertThat(listLiveData.get(0).getTitle(), is("event1"));
            assertThat(listLiveData.get(1).getTitle(), is("event2"));
        };
        try {
            latch1.await(1, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
        Handler handler = new Handler(Looper.getMainLooper()); //main thread queue
        // task to run on the main thread queue
        handler.post(() -> events1.observeForever(observer1));
        mvm.setEventList(null);
        events = mvm.getEventList();
        assertThat(events, CoreMatchers.nullValue(null));
    }

    @Test
    public void testMapViewModelWithRefresh() {
        MapViewModel mvm = new MapViewModel();
        mvm.init(new FakeMapModel());
        LiveData<List<Event>> events = mvm.getEventList();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        final LiveData<List<Event>> events1 = events = mvm.refreshEventList();
        final CountDownLatch latch1 = new CountDownLatch(1);
        Observer<List<Event>> observer1 = listLiveData -> {
            latch1.countDown(); // this releases all the threads
            assertThat(listLiveData.get(0).getTitle(), is("event3"));
            assertThat(listLiveData.get(1).getTitle(), is("event4"));
            assertThat(listLiveData.get(2).getTitle(), is("event5"));
        };
        try {
            latch1.await(1, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
        Handler handler = new Handler(Looper.getMainLooper()); //main thread queue
        // task to run on the main thread queue
        handler.post(() -> events1.observeForever(observer1));
    }

    public class FakeMapModel implements InterfaceMapModel {
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
