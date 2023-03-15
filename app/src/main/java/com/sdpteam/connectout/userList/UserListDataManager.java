package com.sdpteam.connectout.userList;

import androidx.lifecycle.LiveData;

import com.sdpteam.connectout.profile.Profile;

import java.util.List;

public interface UserListDataManager {
    LiveData<List<Profile>> getListOfUsers();
}
