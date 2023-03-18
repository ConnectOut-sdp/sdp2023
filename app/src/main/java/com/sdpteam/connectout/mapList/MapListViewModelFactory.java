package com.sdpteam.connectout.mapList;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.mapList.MapListModelManager;
import com.sdpteam.connectout.mapList.MapListViewModel;

/**
 * Used by ViewModelProvider to instantiate the MapListViewModel with custom arguments (here the model)
 */
public class MapListViewModelFactory implements ViewModelProvider.Factory {

    private final MapListModelManager model;

    public MapListViewModelFactory(MapListModelManager model) {
        this.model = model;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapListViewModel.class)) {
            return (T) new MapListViewModel(model);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
