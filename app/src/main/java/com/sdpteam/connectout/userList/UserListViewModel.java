package com.sdpteam.connectout.userList;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserListViewModel extends ViewModel {

    private final ProfileRepository model;
    private final MutableLiveData<List<Profile>> userListLiveData;

    public UserListViewModel(ProfileRepository model) {
        this.model = model;
        this.userListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Profile>> getUserListLiveData() {
        return userListLiveData;
    }

    void triggerFetchProfileSortedByRating() {
        model.getListOfUsers().thenAccept(profiles -> {
            //sorting Profile by rating
            List<Profile> sorted = profiles.stream()
                    .sorted(comparingDouble(Profile::getRating).reversed())
                    .collect(toList());
            userListLiveData.setValue(sorted);
        });
    }
}