package com.sdpteam.connectout.userList;

import androidx.lifecycle.LiveData;

import com.sdpteam.connectout.profile.Profile;

import java.util.List;

public interface ProfileListDataManager {

    /**
     * @param option (String): Attribute by which elements should be ordered
     * @param values (List<Pair<String, String>>): List of all (Value, Attributes) pairs equalities that should be applied
     * @return (LiveData < List < Profile > >): List of all profiles found that matches the given values.
     */
    LiveData<List<Profile>> getListOfProfile(ProfileListOption option, List<String> values);
}
