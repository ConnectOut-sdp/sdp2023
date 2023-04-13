package com.sdpteam.connectout.profile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ProfileFirebaseDataSourceTest {


    @Test
    public void fetchesAllGivenEvents(){
        ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
        Profile p1 = new Profile("2", "okok","okok@gmail.com","okok okok", Profile.Gender.FEMALE,3.3,6);
        Profile p2 = new Profile("1", "okok2","okok@gmail.com2","okok okok", Profile.Gender.FEMALE,3.3,6);
        model.saveProfile(p1);
        model.saveProfile(p2);

        List<String> ids = new ArrayList<>();
        ids.add(p1.getId());
        ids.add(p2.getId());
        List<Profile> obtained = model.fetchProfiles(ids).join();
        assertThat(p1.getBio(), anyOf(is(obtained.get(0).getBio()), is(obtained.get(1).getBio())));
        assertThat(p1.getId(), anyOf(is(obtained.get(0).getId()), is(obtained.get(1).getId())));
        assertThat(p2.getName(), anyOf(is(obtained.get(0).getName()), is(obtained.get(1).getName())));
    }
}
