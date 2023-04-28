package com.sdpteam.connectout.validation;

import static com.sdpteam.connectout.validation.ValidationUtils.handleValidationFailure;

import android.widget.EditText;

public class EventCreationValidator {

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
        return handleValidationFailure(isValidEventTitle(eventTitleInput.getText().toString()), eventTitleInput, EVENT_TITLE_ERROR)
                && handleValidationFailure(isValidEventDescription(eventDescriptionInput.getText().toString()), eventDescriptionInput, EVENT_DESCRIPTION_ERROR);
    }

}
