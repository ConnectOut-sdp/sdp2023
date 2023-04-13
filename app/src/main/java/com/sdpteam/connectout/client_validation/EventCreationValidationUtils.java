package com.sdpteam.connectout.client_validation;

import android.widget.EditText;
import android.widget.RadioButton;

public class EventCreationValidationUtils {

    private static final int MIN_EVENT_TITLE_LENGTH = 5;
    private static final int MIN_EVENT_DESCRIPTION_LENGTH = 20;

    public static final String EVENT_TITLE_ERROR = "Title too short, should be at least " + MIN_EVENT_TITLE_LENGTH + " characters";
    public static final String EVENT_DESCRIPTION_ERROR = "Description too short, should be at least " + MIN_EVENT_DESCRIPTION_LENGTH + " characters";

    public static boolean isValidEventTitle(String title) {
        return title.length() >= MIN_EVENT_TITLE_LENGTH;
    }
    public static boolean isValidEventDescription(String description) {
        return description.length() >= MIN_EVENT_DESCRIPTION_LENGTH;
    }

    public static boolean eventCreationValidation(EditText eventTitleInput, EditText eventDescriptionInput) {
        if(!isValidEventTitle(eventTitleInput.getText().toString())) {
            eventTitleInput.setError(EVENT_TITLE_ERROR);
            eventTitleInput.requestFocus();
            return false;
        }
        if(!isValidEventDescription(eventDescriptionInput.getText().toString())) {
            eventDescriptionInput.setError(EVENT_DESCRIPTION_ERROR);
            eventDescriptionInput.requestFocus();
            return false;
        }
        return true;
    }


}
