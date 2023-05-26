package com.sdpteam.connectout.profile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sdpteam.connectout.profileList.filter.ProfileFilter;

public interface ProfileDataSource {

    /**
     * saves the given Profile in the repository
     *
     * @return a boolean indicating if the operation is successful
     */
    CompletableFuture<Boolean> saveProfile(Profile profile);

    /**
     * Fetches one's profile from the repository
     */
    CompletableFuture<Profile> fetchProfile(String uid);

    /**
     * @param filter (ProfileFilter) : Custom filter to apply upon the profile's attribute
     * @return (LiveData < List < Profile > >): List of all profiles found that matches the given filters.
     */
    CompletableFuture<List<Profile>> getProfilesByFilter(ProfileFilter filter);
}
