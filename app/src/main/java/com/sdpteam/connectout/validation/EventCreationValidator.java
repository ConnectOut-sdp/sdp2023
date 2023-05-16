package com.sdpteam.connectout.validation;

import static com.sdpteam.connectout.validation.ValidationUtils.handleValidationFailure;

import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EventCreationValidator {

    public static final double MAX_RATING = 5;
    public static final int MIN_NAX_NUMBER_PARTICIPANTS = 2;
    private static final int MIN_EVENT_TITLE_LENGTH = 5;
    public static final String EVENT_TITLE_ERROR = "Title too short, should be at least " + MIN_EVENT_TITLE_LENGTH + " characters";
    private static final int MIN_EVENT_DESCRIPTION_LENGTH = 20;
    public static final String EVENT_DESCRIPTION_ERROR = "Description too short, should be at least " + MIN_EVENT_DESCRIPTION_LENGTH + " characters";

    public static final String DATE_FORMAT_ERROR = "Date format is invalid !";

    public static final String TIME_FORMAT_ERROR = "Time format is invalid !";
    public static final String TIME_ERROR = "Time selected is invalid !";

    public static boolean isValidEventTitle(String title) {
        return title.length() >= MIN_EVENT_TITLE_LENGTH;
    }

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static boolean isValidFormat(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidDate(long date) {
        long currentTime = System.currentTimeMillis();
        return date >= currentTime;
    }

    public static boolean isValidEventDescription(String description) {
        return description.length() >= MIN_EVENT_DESCRIPTION_LENGTH;
    }

    public static boolean eventCreationValidation(EditText eventTitleInput, EditText eventDescriptionInput, EditText txtDateInput, EditText txtTimeInput, long date) {
        return handleValidationFailure(isValidEventTitle(eventTitleInput.getText().toString()), eventTitleInput, EVENT_TITLE_ERROR)
                && handleValidationFailure(isValidEventDescription(eventDescriptionInput.getText().toString()), eventDescriptionInput, EVENT_DESCRIPTION_ERROR)
                && handleValidationFailure(isValidFormat(txtDateInput.getText().toString(), DATE_FORMAT), txtDateInput, DATE_FORMAT_ERROR)
                && handleValidationFailure(isValidFormat(txtTimeInput.getText().toString(), TIME_FORMAT), txtTimeInput, TIME_FORMAT_ERROR)
                && handleValidationFailure(isValidDate(date), txtTimeInput, TIME_ERROR);
    }

    public static boolean eventRestrictionsValidation(double minRating, int maxNumParticipants, long joiningDeadline) {
        long currentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00")).getTimeInMillis();
        return minRating <= MAX_RATING & maxNumParticipants >= MIN_NAX_NUMBER_PARTICIPANTS & joiningDeadline > currentTime;
    }
}
