package com.sdpteam.connectout.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Used by ViewModelProvider to instantiate the MapViewModel with custom arguments (here the model)
 */
public class MapViewModelFactory implements ViewModelProvider.Factory {

    private final MapModelManager model;

    public MapViewModelFactory(MapModelManager model) {
        this.model = model;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(model);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
