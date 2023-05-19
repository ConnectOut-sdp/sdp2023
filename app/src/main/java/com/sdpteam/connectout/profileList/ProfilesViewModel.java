package com.sdpteam.connectout.profileList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDataSource;
import com.sdpteam.connectout.profileList.filter.ProfileFilter;

import java.util.ArrayList;
import java.util.List;

public class ProfilesViewModel extends ViewModel {

    private final ProfileDataSource model;
    private final MutableLiveData<List<Profile>> profiles = new MutableLiveData<>(new ArrayList<>());
    private ProfileFilter profileFilter = ProfileFilter.NONE;

    public ProfilesViewModel(ProfileDataSource model) {
        this.model = model;
    }

    public MutableLiveData<List<Profile>> getProfilesLiveData() {
        return profiles;
    }

    public void setFilter(ProfileFilter profileFilter) {
        this.profileFilter = profileFilter;
    }

    public void refreshProfiles() {
        model.getProfilesByFilter(profileFilter).thenAccept(profiles::setValue);
    }
}