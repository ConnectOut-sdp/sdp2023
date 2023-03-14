package com.sdpteam.connectout.profile;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.utils.LiveDataTestUtil;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;


public class ProfileViewModelTest {
    @Test
    public void testSaveValue() {
        Profile value = new Profile("test", "aymeric", "yo@gmail.com", "empty", Profile.Gender.MALE, 1, 1);
        FakeModel model = new FakeModel();
        ProfileViewModel viewModel = new ProfileViewModel(model);

        viewModel.saveValue(value, "test");

        assertEquals(value, model.mValue);
    }

    @Test
    public void testGetValue() {
        FakeModel model = new FakeModel();
        ProfileViewModel viewModel = new ProfileViewModel(model);

        CompletableFuture<Profile> future = LiveDataTestUtil.toCompletableFuture(viewModel.getProfile("test"));

        Profile p = future.join();

        assertThat(p.getId(), is("fakeProfileModel"));
        assertThat(p.getName(), is("aymeric"));
        assertThat(p.getBio(), is("empty"));
        assertThat(p.getEmail(), is("yo@gmail.com"));


    }

    public static class FakeModel implements ProfileDataManager {
        public Profile mValue;
        private MutableLiveData<Profile> mLiveData = new MutableLiveData<>();

        @Override
        public void saveValue(Profile value, String uid) {
            mValue = value;
        }

        @Override
        public LiveData<Profile> getProfile(String uid) {
            mLiveData= new MutableLiveData<>(new Profile("fakeProfileModel", "aymeric", "yo@gmail.com", "empty", Profile.Gender.MALE, 1, 1));
            return mLiveData;
        }
    }
}
