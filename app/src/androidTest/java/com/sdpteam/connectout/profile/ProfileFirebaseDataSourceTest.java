package com.sdpteam.connectout.profile;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileFirebaseDataSourceTest {

    private final String id1 = generateRandomPath();
    private final String id2 = generateRandomPath();

    @After
    public void cleanup() {
        ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
        model.deleteProfile(id1);
        model.deleteProfile(id2);
    }

    @Test
    public void fetchesAllGivenProfiles() {
        ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
        Profile p1 = new Profile(id1, "okok", "okok@gmail.com", "okok okok", Profile.Gender.FEMALE, 3.3, 6, "");
        Profile p2 = new Profile(id2, "okok2", "okok@gmail.com2", "okok okok", Profile.Gender.FEMALE, 3.3, 6, "");
        fJoin(model.saveProfile(p1));
        fJoin(model.saveProfile(p2));

        List<String> ids = new ArrayList<>();
        ids.add(p1.getId());
        ids.add(p2.getId());
        List<Profile> obtained = fJoin(model.fetchProfiles(ids));
        assertThat(obtained.size(), is(2));
        assertThat(p1.getBio(), anyOf(is(obtained.get(0).getBio()), is(obtained.get(1).getBio())));
        assertThat(p1.getId(), anyOf(is(obtained.get(0).getId()), is(obtained.get(1).getId())));
        assertThat(p2.getName(), anyOf(is(obtained.get(0).getName()), is(obtained.get(1).getName())));
    }

    @Test
    public void fetchCorrectProfileWithExistingId() {
        ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
        Profile p = new Profile(id1, "okok", "okok@gmail.com", "okok okok", Profile.Gender.FEMALE, 3.3, 6, "");
        fJoin(model.saveProfile(p));

        Profile foundProfile = fJoin(model.fetchProfile(id1));

        assertThat(p.getBio(), is(foundProfile.getBio()));
        assertThat(p.getName(), is(foundProfile.getName()));
        assertThat(p.getEmail(), is(foundProfile.getEmail()));
        assertThat(p.getGender(), is(foundProfile.getGender()));
        assertThat(p.getId(), is(foundProfile.getId()));
    }
}
