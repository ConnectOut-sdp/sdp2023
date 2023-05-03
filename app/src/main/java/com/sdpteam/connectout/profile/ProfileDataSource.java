package com.sdpteam.connectout.profile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
     * @param option (ProfileOrderingOption): option of filtering adopted, random, by name or by rating.
     * @param values (List<String>): list of parsed users inputs which corresponds to the filters.
     * @return (LiveData < List < Profile > >): List of all profiles found that matches the given filters.
     */
    CompletableFuture<List<Profile>> getListOfProfile(ProfileFirebaseDataSource.ProfileOrderingOption option, List<String> values);
}
