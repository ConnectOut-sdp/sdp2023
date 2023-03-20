package com.sdpteam.connectout.userList;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.util.NumberUtils;
import com.sdpteam.connectout.profile.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class UserListViewModel extends ViewModel {

    private final ProfileListDataManager model;

    public UserListViewModel(ProfileListDataManager model) {
        this.model = model;
    }

    public LiveData<List<Profile>> getListOfProfile(ProfileListOption option, List<String> values) {
        return model.getListOfProfile(option, values);
    }
}