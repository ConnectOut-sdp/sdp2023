package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender.MALE;
import static com.sdpteam.connectout.registration.CompleteRegistration.MandatoryFields;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import android.os.Handler;
import android.os.Looper;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDataSource;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompleteRegistrationTest {

    @Test
    public void testCompleteRegistrationActuallySetsCorrectValues() {
        final Profile[] databaseContent = new Profile[1];
        ProfileDataSource fakeProfileDatabase = new ProfileDataSource() {
            @Override
            public CompletableFuture<Boolean> saveProfile(Profile profile) {
                CompletableFuture<Boolean> res = new CompletableFuture<>();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    databaseContent[0] = profile;
                    res.complete(true);
                });
                return res;
            }

            @Override
            public CompletableFuture<Profile> fetchProfile(String uid) {
                return CompletableFuture.completedFuture(databaseContent[0]);
            }

            @Override
            public CompletableFuture<List<Profile>> getListOfProfile(ProfileFirebaseDataSource.ProfileOrderingOption option, List<String> values) {
                return CompletableFuture.completedFuture(new ArrayList<>());
            }

            public CompletableFuture<List<Profile>> getListOfUsers() {
                return CompletableFuture.completedFuture(new ArrayList<>());
            }
        };
        CompleteRegistration completeRegistration = new CompleteRegistration(fakeProfileDatabase);
        fJoin(completeRegistration.completeRegistration("007",
                new MandatoryFields("James", "james.bond@gmail.com", "No bio lol", MALE), null));

        // add latency so the background tasks gets finished before calling fetchProfile too soon and obviously getting a null value
        //saved.join(); // instead of thread sleep

        Profile profile = fJoin(fakeProfileDatabase.fetchProfile("007"));
        assertThat(profile.getName(), is("James"));
        assertThat(profile.getBio(), is("No bio lol"));
        assertThat(profile.getGender(), is(MALE));
        assertThat(profile.getEmail(), is("james.bond@gmail.com"));
        assertThat(profile.getRating(), is(0.0));
        assertThat(profile.getNumRatings(), is(0));
    }
}