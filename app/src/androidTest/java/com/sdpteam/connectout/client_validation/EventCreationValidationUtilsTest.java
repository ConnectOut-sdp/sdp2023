package com.sdpteam.connectout.client_validation;

import android.content.Context;
import android.widget.EditText;
import org.junit.Test;

import static org.junit.Assert.*;

import androidx.test.platform.app.InstrumentationRegistry;

public class EventCreationValidationUtilsTest {

    @Test
    public void testIsValidEventTitle() {
        assertTrue(EventCreationValidationUtils.isValidEventTitle("Valid title asdfasdf"));
        assertFalse(EventCreationValidationUtils.isValidEventTitle("a"));
        assertFalse(EventCreationValidationUtils.isValidEventTitle(""));
    }

    @Test
    public void testIsValidEventDescription() {
        assertTrue(EventCreationValidationUtils.isValidEventDescription("Valid description with more than 20 characters adsfasdf"));
        assertFalse(EventCreationValidationUtils.isValidEventDescription("a"));
        assertFalse(EventCreationValidationUtils.isValidEventDescription(""));
    }

    @Test
    public void testEventCreationValidation() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        EditText eventTitleInput = new EditText(appContext);
        EditText eventDescriptionInput = new EditText(appContext);

        // Test case 1: valid input
        eventTitleInput.setText("Valid title adfasdf");
        eventDescriptionInput.setText("Valid description with more than 20 characters adsfasdfasdf");
        assertTrue(EventCreationValidationUtils.eventCreationValidation(eventTitleInput, eventDescriptionInput));

        // Test case 2: invalid event title
        eventTitleInput.setText("a");
        eventDescriptionInput.setText("Valid description with more than 20 characters adfasdfasdf");
        assertFalse(EventCreationValidationUtils.eventCreationValidation(eventTitleInput, eventDescriptionInput));
        assertEquals(EventCreationValidationUtils.EVENT_TITLE_ERROR, eventTitleInput.getError().toString());

        // Test case 3: invalid event description
        eventTitleInput.setText("Valid title adfasdf");
        eventDescriptionInput.setText("a");
        assertFalse(EventCreationValidationUtils.eventCreationValidation(eventTitleInput, eventDescriptionInput));
        assertEquals(EventCreationValidationUtils.EVENT_DESCRIPTION_ERROR, eventDescriptionInput.getError().toString());

        // Test case 4: both inputs invalid
        eventTitleInput.setText("");
        eventDescriptionInput.setText("");
        assertFalse(EventCreationValidationUtils.eventCreationValidation(eventTitleInput, eventDescriptionInput));
        assertEquals(EventCreationValidationUtils.EVENT_TITLE_ERROR, eventTitleInput.getError().toString());
        assertEquals(EventCreationValidationUtils.EVENT_DESCRIPTION_ERROR, eventDescriptionInput.getError().toString());
    }
}


