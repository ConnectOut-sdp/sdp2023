package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender.MALE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDataManager;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CompleteRegistrationTest {

    @Test
    public void testCompleteRegistrationActuallySetsCorrectValues() {
        MutableLiveData<Profile> databaseContent = new MutableLiveData<>();
        ProfileDataManager fakeProfileDatabase = new ProfileDataManager() {
            @Override
            public void saveValue(Profile profile) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    databaseContent.setValue(profile);
                });
            }

            @Override
            public LiveData<Profile> getValue(String uid) {
                return databaseContent;
            }
        };
        CompleteRegistration completeRegistration = new CompleteRegistration(fakeProfileDatabase);
        completeRegistration.completeRegistration("007", new CompleteRegistration.MandatoryFields("James", "james.bond@gmail.com", "No bio lol", MALE));

        Profile profile = LiveDataTestUtil.toCompletableFuture(fakeProfileDatabase.getValue("007")).join();
        assertThat(profile.getBio(), is("No bio lol"));
        assertThat(profile.getGender(), is(MALE));
        assertThat(profile.getName(), is("James"));
        assertThat(profile.getEmail(), is("james.bond@gmail.com"));
        assertThat(profile.getRating(), is(0.0));
        assertThat(profile.getNumRatings(), is(0));
    }
}