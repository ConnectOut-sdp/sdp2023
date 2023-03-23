package com.sdpteam.connectout.profile;

import static org.junit.Assert.assertEquals;

import com.google.firebase.database.Query;
import com.sdpteam.connectout.Path;
import com.sdpteam.connectout.userList.OrderingOption;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class ProfileModelTest {
    private final static ProfileModel MODEL = new ProfileModel();

    @Test
    public void filterProfilesReturnRootQueryOnNone() {
        Query root = Mockito.mock(Query.class);
        Query query = MODEL.filterProfiles(root, OrderingOption.NONE, null);
        assertEquals(root, query);
    }

    @Test
    public void filterProfilesReturnByNameWithNullValues() {
        Query root = Mockito.mock(Query.class);
        Query query = MODEL.filterProfiles(root, OrderingOption.NAME, null);
        assertEquals(root.orderByChild(Path.Profile.toString() + Path.Slash + OrderingOption.NAME), query);
    }
    @Test
    public void filterProfilesReturnByNameWithEmptyValues() {
        Query root = Mockito.mock(Query.class);
        Query query = MODEL.filterProfiles(root, OrderingOption.NAME, new ArrayList<>());
        assertEquals(root.orderByChild(Path.Profile.toString() + Path.Slash + OrderingOption.NAME), query);
    }

    @Test
    public void filterProfilesReturnByRatingWithNullValues() {
        Query root = Mockito.mock(Query.class);
        Query query =MODEL.filterProfiles(root, OrderingOption.RATING, null);
        assertEquals(root.orderByChild(Path.Profile.toString() + Path.Slash + OrderingOption.RATING), query);
    }
    @Test
    public void filterProfilesReturnByRatingWithEmptyValues() {
        Query root = Mockito.mock(Query.class);
        Query query =MODEL.filterProfiles(root, OrderingOption.RATING, new ArrayList<>());
        assertEquals(root.orderByChild(Path.Profile.toString() + Path.Slash + OrderingOption.RATING), query);
    }


}
