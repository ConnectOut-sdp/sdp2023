package com.sdpteam.connectout.event.nearbyEvents;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.event.EventRepository;

/**
 * Used by ViewModelProvider to instantiate the EventsViewModel with custom arguments (here the model)
 */
public class EventsViewModelFactory implements ViewModelProvider.Factory {

    private final EventRepository model;

    public EventsViewModelFactory(EventRepository model) {
        this.model = model;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EventsViewModel.class)) {
            return (T) new EventsViewModel(model);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
