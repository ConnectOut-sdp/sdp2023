package com.sdpteam.connectout.userList;

import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.profile.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class UserListViewModel extends ViewModel {

    private final UserListDataManager model;

    public UserListViewModel(UserListDataManager model) {
        this.model = model;
    }

    List<Profile> getProfileSortedByRating() {
        List<Profile> toSort = new ArrayList<>(Objects.requireNonNull(model.getListOfUsers().getValue()));
        //sorting Profile by rating
        toSort.sort(Comparator.comparingDouble(Profile::getRating));
        Collections.reverse(toSort);
        return toSort;
    }
}