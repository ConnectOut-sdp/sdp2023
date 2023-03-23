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

        if(userInput != null) {
            if (option == OrderingOption.RATING) {
                inputList = parseRangeInput(userInput);
            } else if (option == OrderingOption.NAME) {
                inputList = parseNameInput(userInput);
            }
        }

        return model.getListOfProfile(option, inputList);
    }


    /**
     *
     * @param userInput (String): input given by the user
     * @return (List<String>): list containing the name wanted by the user.
     */
    private List<String> parseNameInput(String userInput){
         return Collections.singletonList(userInput);
    }

    /**
     *
     * @param userInput (String): input given by the user
     * @return (List<String>): list containing the range of ratings wanted by the user.
     */
    private List<String> parseRangeInput(String userInput){
        List<String> inputList = new ArrayList<>();

        String[] rangeText = userInput.split(";");
        if (rangeText.length == 2 && Arrays.stream(rangeText).allMatch(str -> str.matches(NUMBER_REGEX))) {
            inputList = Arrays.asList(rangeText);
        }
        return inputList;
    }

}