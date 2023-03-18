package com.sdpteam.connectout.mapList;

import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;

import com.sdpteam.connectout.profile.ProfileViewModel;
import org.junit.Test;



public class MapListViewModelFactoryTest {

    @Test
    public void testFailsToCreateViewOnWrongArgument(){
        MapListViewModelFactory mf = new MapListViewModelFactory((filteredAttribute, expectedValue) -> null);
        assertThrows(IllegalArgumentException.class, () -> {
            mf.create(ProfileViewModel.class);
        });
    }
    @Test
    public void createViewOnCorrectModelArgument(){
         MapListModelManager mockModel = (String filteredAttribute, String expectedValue) -> null;
        // Implicitly instantiating MapListViewModel to use that instance back in MapViewFragment
        assertThat(new MapListViewModelFactory(mockModel).create(MapListViewModel.class), isA(MapListViewModel.class));
    }

}
