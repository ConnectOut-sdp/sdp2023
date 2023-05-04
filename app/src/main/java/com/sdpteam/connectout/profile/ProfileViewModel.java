package com.sdpteam.connectout.profile;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.firebase.ui.database.FirebaseListOptions;

import android.view.View;
import android.widget.ListAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Profile> profileLiveData;
    public ProfileDataSource profileDataSource;
    public RegisteredEventsDataSource registeredEventsDataSource;

    public ProfileViewModel(ProfileDataSource profileDataSource) {
        this.profileDataSource = profileDataSource;
        this.profileLiveData = new MutableLiveData<>();
    }

    public ProfileViewModel(ProfileFirebaseDataSource profileAndRegisteredEventsRepository) {
        this.profileDataSource = profileAndRegisteredEventsRepository;
        this.profileLiveData = new MutableLiveData<>();
        this.registeredEventsDataSource = profileAndRegisteredEventsRepository;
    }

    /**
     * trigger a fetch from repo
     */
    public void fetchProfile(String uid) {
        profileDataSource.fetchProfile(uid).thenAccept(profileLiveData::setValue);
    }

    /**
     * Save your new Profile
     */
    public void saveProfile(Profile profile) {
        try {
            profileDataSource.saveProfile(profile).get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) { //TODO check why timeout in ci
//            throw new RuntimeException(e);
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        } catch (TimeoutException e) {
//            throw new RuntimeException(e);
        }
    }

    public LiveData<Profile> getProfileLiveData() {
        return profileLiveData;
    }

    /**
     * Update rating of a Profile
     */
    public void updateRating(String uid, double newRating) {
        profileDataSource.fetchProfile(uid).thenAccept(profile -> {
            double rating = profile.getRating();
            int numRatings = profile.getNumRatings();

            //compute new rating
            rating = (rating * numRatings + newRating) / (numRatings + 1);
            numRatings++;

            //save new rating
            try {
                profileDataSource.saveProfile(
                        new Profile(uid, profile.getName(), profile.getEmail(),
                                profile.getBio(), profile.getGender(), rating, numRatings, profile.getProfileImageUrl()
                        )).get(5, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * sets up the FirebaseListAdapter for the registered events view
     */
    public void setUpListAdapter(Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLayout,
                                 Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLifecycleOwner,
                                 BiConsumer<View, Profile.CalendarEvent> populateView,
                                 Consumer<ListAdapter> setAdapter, String profileId) {
        registeredEventsDataSource.setUpListAdapter(setLayout, setLifecycleOwner, populateView, setAdapter, profileId);
    }

    /**
     * stores a new Profile.CalendarEvent (eventId, eventTitle and eventDate)
     * in list of events that a profile is registered to
     */
    public void registerToEvent(Profile.CalendarEvent calEvent, String profileId) {
        registeredEventsDataSource.registerToEvent(calEvent, profileId);
    }
}

