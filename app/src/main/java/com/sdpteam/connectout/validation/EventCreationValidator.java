package com.sdpteam.connectout.validation;

import static com.sdpteam.connectout.validation.ValidationUtils.handleValidationFailure;

import android.widget.EditText;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EventCreationValidator {

    private static final int MIN_EVENT_TITLE_LENGTH = 5;
    private static final int MIN_EVENT_DESCRIPTION_LENGTH = 20;

    public static final String EVENT_TITLE_ERROR = "Title too short, should be at least " + MIN_EVENT_TITLE_LENGTH + " characters";
    public static final String EVENT_DESCRIPTION_ERROR = "Description too short, should be at least " + MIN_EVENT_DESCRIPTION_LENGTH + " characters";
    public static final double MAX_RATING = 5;
    public static final int MIN_NAX_NUMBER_PARTICIPANTS = 2;
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

    public static boolean eventRestrictionsValidation(double minRating, int maxNumParticipants, long joiningDeadline){
        long currentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00")).getTimeInMillis();
        return minRating <= MAX_RATING & maxNumParticipants >= MIN_NAX_NUMBER_PARTICIPANTS & joiningDeadline > currentTime;
    }

}
