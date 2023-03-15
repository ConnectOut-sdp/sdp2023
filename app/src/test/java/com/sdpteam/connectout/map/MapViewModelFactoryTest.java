package com.sdpteam.connectout.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.event.Event;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MapViewModelFactoryTest {

    @Test
    public void test_ViewModel_uses_the_custom_model_through_factory_instantiation() {
        final MapViewModelFactory factory = new MapViewModelFactory(new FakeMapModel());
        final MapViewModel mapViewModel = factory.create(MapViewModel.class);
        final List<Event> events = mapViewModel.getEventList().getValue();

        assertEquals(2, events.size());
        assertEquals("Event 1", events.get(0).getTitle());
        assertEquals("Event 2", events.get(1).getTitle());
    }

    @Test
    public void test_factory_cannot_instantiate_ViewModel_when_model_class_is_invalid() {
        final MapViewModelFactory factory = new MapViewModelFactory(new FakeMapModel());
        assertThrows(IllegalArgumentException.class, () -> factory.create(InvalidViewModel.class));
    }

    private static class FakeMapModel implements MapModel {

        @Override
        public MutableLiveData<List<Event>> getEventLiveList() {
            return new MutableLiveData<>(Arrays.asList(
                    new Event("Event 1", new GPSCoordinates(0, 0), "Description 1"),
                    new Event("Event 2", new GPSCoordinates(1, 1), "Description 2")
            ));
        }
    }

    private static class InvalidViewModel extends ViewModel {
    }
}
