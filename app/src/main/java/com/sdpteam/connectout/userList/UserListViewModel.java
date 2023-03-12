package com.sdpteam.connectout.userList;

import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.profile.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class UserListViewModel extends ViewModel {

    private final UserListData mModel;

    public UserListViewModel(UserListData mModel) {
        this.mModel = mModel;
    }

    List<Profile> getProfileSorted() {
        List<Profile> toSort = new ArrayList<>(Objects.requireNonNull(mModel.getValue().getValue()));
        //sorting Profile by rating
        toSort.sort(Comparator.comparingDouble(Profile::getRating));
        Collections.reverse(toSort);
        return toSort;
    }
}