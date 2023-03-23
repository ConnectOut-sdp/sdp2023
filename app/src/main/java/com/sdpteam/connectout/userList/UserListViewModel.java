package com.sdpteam.connectout.userList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.profile.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserListViewModel extends ViewModel {

    public final static String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";

    private final ProfileListDataManager model;

    public UserListViewModel(ProfileListDataManager model) {
        this.model = model;
    }

    /**
     * @param option    (OrderingOption): ordering in which profiles are sorted
     * @param userInput (String): non parsed filter demanded by the user
     * @return (LiveData < List < Profile > >): all the found profiles matching the inputs filter and ordering
     */
    public LiveData<List<Profile>> getListOfProfile(OrderingOption option, String userInput) {
        List<String> inputList = null;

        if (option == OrderingOption.RATING && userInput != null) {
            // Parse rating range input
            String[] rangeText = userInput.split(";");
            if (rangeText.length == 2 && Arrays.stream(rangeText).allMatch(str -> str.matches(NUMBER_REGEX))) {
                inputList = Arrays.asList(rangeText);
            }
        } else if (option == OrderingOption.NAME && userInput != null) {
            // Use user input as list of names
            inputList = Collections.singletonList(userInput);
        }

        return model.getListOfProfile(option, inputList);
    }
}