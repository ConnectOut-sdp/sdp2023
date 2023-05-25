package com.sdpteam.connectout.profile;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.profileList.filter.ProfileFilter;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class ProfileViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void testSaveValue() {
        Profile value = new Profile("test", "aymeric", "yo@gmail.com", "empty", Profile.Gender.MALE, 1, 1, "");
        FakeProfileDataSource model = new FakeProfileDataSource();
        ProfileViewModel viewModel = new ProfileViewModel(model);

        viewModel.saveProfile(value);

        assertEquals(value, model.value);
    }

    @Test
    public void testGetValue() {
        FakeProfileDataSource model = new FakeProfileDataSource();
        ProfileViewModel viewModel = new ProfileViewModel(model);

        viewModel.fetchProfile("test");

        Profile p = LiveDataTestUtil.getOrAwaitValue(viewModel.getProfileLiveData());
        assertThat(p.getId(), is("fakeProfileModel"));
        assertThat(p.getName(), is("aymeric"));
        assertThat(p.getBio(), is("empty"));
        assertThat(p.getEmail(), is("yo@gmail.com"));
    }

    private class FakeProfileDataSource implements ProfileDataSource {
        public Profile value;
        CompletableFuture<Boolean> done = new CompletableFuture<>();

        @Override
        public CompletableFuture<Boolean> saveProfile(Profile value) {
            this.value = value;
            return CompletableFuture.completedFuture(true);
        }

        @Override
        public CompletableFuture<Profile> fetchProfile(String uid) {
            done.complete(true);
            Profile mLiveData = new Profile("fakeProfileModel", "aymeric", "yo@gmail.com", "empty", Profile.Gender.MALE, 1, 1, "");
            return CompletableFuture.completedFuture(mLiveData);
        }

        @Override
        public CompletableFuture<List<Profile>> getProfilesByFilter(ProfileFilter filter) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }
}
