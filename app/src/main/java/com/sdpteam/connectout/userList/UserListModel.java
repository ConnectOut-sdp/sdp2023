package com.sdpteam.connectout.userList;

import static com.sdpteam.connectout.profile.Profile.Gender.FEMALE;
import static com.sdpteam.connectout.profile.Profile.Gender.MALE;
import static com.sdpteam.connectout.profile.Profile.Gender.OTHER;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.profile.Profile;

import java.util.ArrayList;
import java.util.List;

public class UserListModel implements UserListDataManager {

    private final DatabaseReference database;

    public UserListModel() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public LiveData<List<Profile>> getListOfUsers() {
        //TODO: get data from firebase

        List<Profile> userlist = new ArrayList<>();

        userlist.add(new Profile("100", "Alice", "Alice@gmail.com", "Hello, I'm Alice", FEMALE, 4.5, 10));
        userlist.add(new Profile("101", "Bob", "Bob@gmail.com", "Hello, I'm Bob", MALE, 3.5, 12));
        userlist.add(new Profile("102", "Charlie", "Charlie@gmail.com", "Hello, I'm Charlie", OTHER, 5, 3));

        return new MutableLiveData<>(userlist);
    }
}
