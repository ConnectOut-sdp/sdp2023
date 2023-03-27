package com.sdpteam.connectout.userList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sdpteam.connectout.profile.Profile;

public interface UserRepository {
    CompletableFuture<List<Profile>> getListOfUsers();
}
