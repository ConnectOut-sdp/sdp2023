package com.sdpteam.connectout.userList;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.sdpteam.connectout.profile.Profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserListViewModel extends ViewModel {

    private final UserRepository model;
    private final MutableLiveData<List<Profile>> userListLiveData;

    public UserListViewModel(UserRepository model) {
        this.model = model;
        this.userListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Profile>> getUserListLiveData() {
        return userListLiveData;
    }

    void triggerFetchProfileSortedByRating() {
        List<Profile> profiles = new ArrayList<>(Objects.requireNonNull(model.getListOfUsers().join()));

        //sorting Profile by rating
        List<Profile> sorted = profiles.stream()
                .sorted(comparingDouble(Profile::getRating).reversed())
                .collect(toList());
        userListLiveData.setValue(sorted);
    }
}