package com.sdpteam.connectout.userList;

import androidx.lifecycle.LiveData;

import com.sdpteam.connectout.profile.Profile;

import java.util.List;

public interface ProfileListDataManager {

    /**
     * @param option (ProfileListOption): option of filtering adopted, random, by name or by rating.
     * @param values (List<String>): list of parsed users inputs which corresponds to the filters.
     * @return (LiveData < List < Profile > >): List of all profiles found that matches the given filters.
     */
    LiveData<List<Profile>> getListOfProfile(ProfileListOption option, List<String> values);
}
