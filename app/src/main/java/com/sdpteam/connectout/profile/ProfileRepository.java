package com.sdpteam.connectout.profile;

import android.view.View;
import android.widget.ListAdapter;

import com.firebase.ui.database.FirebaseListOptions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ProfileRepository {

    /**
     * saves the given Profile in the repository
     */
    void saveProfile(Profile profile);

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
