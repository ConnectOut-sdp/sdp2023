package com.sdpteam.connectout.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

public class EventValidatorTest {

    @Test
    public void testIsValidEventTitle() {
        assertTrue(EventValidator.isValidEventTitle("Valid title asdfasdf"));
        assertFalse(EventValidator.isValidEventTitle("a"));
        assertFalse(EventValidator.isValidEventTitle(""));
    }

    @Test
    public void testIsValidEventDescription() {
        assertTrue(EventValidator.isValidEventDescription("Valid description with more than 20 characters adsfasdf"));
        assertFalse(EventValidator.isValidEventDescription("a"));
        assertFalse(EventValidator.isValidEventDescription(""));
    }

    @Test
    public void testIsValidDateFormat_ValidDate() {
        // Valid date format: "dd-MM-yyyy"
        String validDate = "16-05-2500";
        assertTrue(EventValidator.isValidFormat(validDate, EventValidator.DATE_FORMAT));
    }

    @Test
    public void testIsValidDateFormat_InvalidDate() {
        // Invalid date format: "dd-MM-yyyy"
        String invalidDate = "2023-05-16";
        assertFalse(EventValidator.isValidFormat(invalidDate, EventValidator.DATE_FORMAT));
    }

    @Test
    public void testIsValidTimeFormat_ValidTime() {
        // Valid time format: "HH:mm"
        String validTime = "12:30";
        assertTrue(EventValidator.isValidFormat(validTime, EventValidator.TIME_FORMAT));
    }

    @Test
    public void testIsValidTimeFormat_InvalidTime() {
        // Invalid time format: "HH:mm:ss"
        String invalidTime = "12-30-45";
        assertFalse(EventValidator.isValidFormat(invalidTime, EventValidator.TIME_FORMAT));
    }

    @Test
    public void testEventDate() {
        long eventTime = System.currentTimeMillis() + 100000000;
        long eventNotValidTime = System.currentTimeMillis() - 100000000;
        assertTrue(EventValidator.isValidTime(eventTime));
        assertFalse(EventValidator.isValidTime(eventNotValidTime));
    }

    @Test
    public void testEventCreationValidation() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        EditText eventTitleInput = new EditText(appContext);
        EditText eventDescriptionInput = new EditText(appContext);

        EditText txtDateInput = new EditText(appContext);
        EditText txtTimeInput = new EditText(appContext);
        txtDateInput.setText("16-05-2500");
        txtTimeInput.setText("12:30");

        // Test case 1: valid input
        eventTitleInput.setText("Valid title adfasdf");
        eventDescriptionInput.setText("Valid description with more than 20 characters adsfasdfasdf");
        assertTrue(EventValidator.eventCreationValidation(eventTitleInput, eventDescriptionInput, txtDateInput, txtTimeInput));

        // Test case 2: invalid event title
        eventTitleInput.setText("a");
        eventDescriptionInput.setText("Valid description with more than 20 characters adfasdfasdf");
        assertFalse(EventValidator.eventCreationValidation(eventTitleInput, eventDescriptionInput, txtDateInput, txtTimeInput));
        assertEquals(EventValidator.EVENT_TITLE_ERROR, eventTitleInput.getError().toString());

        // Test case 3: invalid event description
        eventTitleInput.setText("Valid title adfasdf");
        eventDescriptionInput.setText("a");
        assertFalse(EventValidator.eventCreationValidation(eventTitleInput, eventDescriptionInput, txtDateInput, txtTimeInput));
        assertEquals(EventValidator.EVENT_DESCRIPTION_ERROR, eventDescriptionInput.getError().toString());

        // Test case 4: both inputs invalid
        eventTitleInput.setText("");
        eventDescriptionInput.setText("");
        assertFalse(EventValidator.eventCreationValidation(eventTitleInput, eventDescriptionInput, txtDateInput, txtTimeInput));
        assertEquals(EventValidator.EVENT_TITLE_ERROR, eventTitleInput.getError().toString());
        assertEquals(EventValidator.EVENT_DESCRIPTION_ERROR, eventDescriptionInput.getError().toString());
    }
}


