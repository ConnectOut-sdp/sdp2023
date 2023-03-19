package com.sdpteam.connectout.userList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.profile.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserListViewModel extends ViewModel {

    private final ProfileListDataManager model;

    public UserListViewModel(ProfileListDataManager model) {
        this.model = model;
    }

    public LiveData<List<Profile>> getListOfProfile(ProfileListOption option, List<String> values) {
        return model.getListOfProfile(option, values);
    }
    /*
    public LiveData<List<Profile>> getProfileSortedByRating() {
        return model.getListOfProfile(ProfileListOption.RATING, null);
    }
    public LiveData<List<Profile>> getProfileSortedWithRatingRange(Double min, Double max) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(min));
        list.add(String.valueOf(max));
        return model.getListOfProfile(ProfileListOption.RATING,list);
    }
   public LiveData<List<Profile>> getProfileSortedByName() {
        return model.getListOfProfile(ProfileListOption.NAME, null);
    }
    public LiveData<List<Profile>> getProfileWithName(String name) {
        return model.getListOfProfile(ProfileListOption.NAME, new ArrayList<>(Collections.singleton(name)));
    }
    public LiveData<List<Profile>> getProfiles() {
        return model.getListOfProfile(ProfileListOption.NONE, null);
    }*/
}