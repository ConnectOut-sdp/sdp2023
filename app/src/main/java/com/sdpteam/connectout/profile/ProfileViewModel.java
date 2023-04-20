package com.sdpteam.connectout.profile;

import android.view.View;
import android.widget.ListAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.database.FirebaseListOptions;
import com.sdpteam.connectout.chat.ChatFirebaseDataSource;
import com.sdpteam.connectout.chat.ChatMessage;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Profile> profileLiveData;
    public ProfileRepository profileRepository;
    public RegisteredEventsRepository registeredEventsRepository;

    public ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        this.profileLiveData = new MutableLiveData<>();
    }

    public ProfileViewModel(ProfileFirebaseDataSource profileAndRegisteredEventsRepository){
        this.profileRepository = profileAndRegisteredEventsRepository;
        this.profileLiveData = new MutableLiveData<>();
        this.registeredEventsRepository = profileAndRegisteredEventsRepository;
    }

    /**
     * trigger a fetch from repo
     */
    public void fetchProfile(String uid) {
        profileRepository.fetchProfile(uid).thenAccept(profile -> {
            profileLiveData.setValue(profile);
        });
    }

    /**
     * Save your new Profile
     */
    public void saveProfile(Profile profile) {
        profileRepository.saveProfile(profile);
    }

    public LiveData<Profile> getProfileLiveData() {
        return profileLiveData;
    }

    /**
     * Update rating of a Profile
     */
    public void updateRating(String uid, double newRating) {
        profileRepository.fetchProfile(uid).thenAccept(profile -> {
            double rating = profile.getRating();
            int numRatings = profile.getNumRatings();

            //compute new rating
            rating = (rating * numRatings + newRating) / (numRatings + 1);
            numRatings++;

            //save new rating
            profileRepository.saveProfile(
                    new Profile(uid, profile.getName(), profile.getEmail(),
                            profile.getBio(), profile.getGender(), rating, numRatings, profile.getProfileImageUrl()
                    ));
        });
    }

    /**
     * sets up the FirebaseListAdapter for the registered events view
     */
    public void setUpListAdapter(Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLayout,
                                 Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLifecycleOwner,
                                 BiConsumer<View, Profile.CalendarEvent> populateView,
                                 Consumer<ListAdapter> setAdapter, String profileId) {
        registeredEventsRepository.setUpListAdapter(setLayout, setLifecycleOwner, populateView, setAdapter, profileId);
    }
}

