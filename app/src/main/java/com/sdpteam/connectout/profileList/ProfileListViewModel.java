package com.sdpteam.connectout.profileList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDataSource;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProfileListViewModel extends ViewModel {

    private final static String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private final ProfileDataSource model;
    private final MutableLiveData<List<Profile>> userListLiveData;

    public ProfileListViewModel(ProfileDataSource model) {
        this.model = model;
        this.userListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Profile>> getUserListLiveData() {
        return userListLiveData;
    }

    /**
     * @param option    (ProfileOrderingOption): ordering in which profiles are sorted
     * @param userInput (String): non parsed filter demanded by the user
     * @return (LiveData < List < Profile > >): all the found profiles matching the inputs filter and ordering
     */
    public void getListOfProfile(ProfileFirebaseDataSource.ProfileOrderingOption option, String userInput) {
        List<String> inputList = null;

        if (userInput != null) {
            if (option == ProfileFirebaseDataSource.ProfileOrderingOption.RATING) {
                inputList = parseRangeInput(userInput);
                Collections.sort(inputList);
            } else if (option == ProfileFirebaseDataSource.ProfileOrderingOption.NAME) {
                inputList = parseNameInput(userInput);
            } else if (option == ProfileFirebaseDataSource.ProfileOrderingOption.EVENT_PARTICIPANTS) {
                inputList = Arrays.asList(userInput);
            }
        }
        model.getListOfProfile(option, inputList).thenAccept(userListLiveData::setValue);
    }

    /**
     * @param userInput (String): input given by the user
     * @return (List < String >): list containing the name wanted by the user.
     */
    private List<String> parseNameInput(String userInput) {
        return Collections.singletonList(userInput);
    }

    /**
     * @param userInput (String): input given by the user
     * @return (List < String >): list containing the range of ratings wanted by the user.
     */
    private List<String> parseRangeInput(String userInput) {
        List<String> inputList = new ArrayList<>();

        String[] rangeText = userInput.split(";");
        if (Arrays.stream(rangeText).allMatch(str -> str.matches(NUMBER_REGEX))) {
            if (rangeText.length == 2) {
                inputList = Arrays.asList(rangeText);
            } else {
                inputList = Collections.singletonList(userInput);
            }
        }
        return inputList;
    }
}