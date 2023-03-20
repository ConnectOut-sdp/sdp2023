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
        //Parse the user inputs to adapt to different cases.
        switch (option) {
            //If the user want to order profiles by rating:
            case RATING:
                // Check if a specific rating range is demanded.
                if (userInput != null) {
                    String[] rangeText = userInput.split(";");
                    if (rangeText.length == 2 && Arrays.stream(rangeText).allMatch(str -> str.matches(NUMBER_REGEX))) {
                        //If it respect the nomenclature, ask for the range.
                        return model.getListOfProfile(option, Arrays.asList(rangeText));
                    }
                }
            //If the user want to order profiles by name:
            case NAME:
                if (userInput != null) {
                    return model.getListOfProfile(option, new ArrayList<>(Collections.singleton(userInput)));
                }

            return model.getListOfProfile(option, null);

        }
        return model.getListOfProfile(OrderingOption.NONE, null);

    }
}