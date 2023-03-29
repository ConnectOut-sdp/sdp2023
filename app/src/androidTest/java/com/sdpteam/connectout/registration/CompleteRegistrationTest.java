package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender.MALE;
import static com.sdpteam.connectout.registration.CompleteRegistration.MandatoryFields;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileRepository;

import android.os.Handler;
import android.os.Looper;

public class CompleteRegistrationTest {

    CompletableFuture<Boolean> saved = new CompletableFuture<>();

    @Test
    public void testCompleteRegistrationActuallySetsCorrectValues() {
        final Profile[] databaseContent = new Profile[1];
        ProfileRepository fakeProfileDatabase = new ProfileRepository() {
            @Override
            public void saveProfile(Profile profile) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    databaseContent[0] = profile;
                    saved.complete(true);
                });
            }

            @Override
            public CompletableFuture<Profile> fetchProfile(String uid) {
                return CompletableFuture.completedFuture(databaseContent[0]);
            }

            @Override
            public CompletableFuture<List<Profile>> getListOfUsers() {
                return CompletableFuture.completedFuture(new ArrayList<>());
            }
        };
        CompleteRegistration completeRegistration = new CompleteRegistration(fakeProfileDatabase);
        completeRegistration.completeRegistration("007",
                new MandatoryFields("James", "james.bond@gmail.com", "No bio lol", MALE));

        // add latency so the background tasks gets finished before calling fetchProfile too soon and obviously getting a null value
        saved.join(); // instead of thread sleep

        Profile profile = fakeProfileDatabase.fetchProfile("007").join();
        assertThat(profile.getName(), is("James"));
        assertThat(profile.getBio(), is("No bio lol"));
        assertThat(profile.getGender(), is(MALE));
        assertThat(profile.getEmail(), is("james.bond@gmail.com"));
        assertThat(profile.getRating(), is(0.0));
        assertThat(profile.getNumRatings(), is(0));
    }
}