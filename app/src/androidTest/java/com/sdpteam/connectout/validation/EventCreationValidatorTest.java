package com.sdpteam.connectout.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import android.content.Context;
import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;

public class EventCreationValidatorTest {

    @Test
    public void testIsValidEventTitle() {
        assertTrue(EventCreationValidator.isValidEventTitle("Valid title asdfasdf"));
        assertFalse(EventCreationValidator.isValidEventTitle("a"));
        assertFalse(EventCreationValidator.isValidEventTitle(""));
    }

    @Test
    public void testIsValidEventDescription() {
        assertTrue(EventCreationValidator.isValidEventDescription("Valid description with more than 20 characters adsfasdf"));
        assertFalse(EventCreationValidator.isValidEventDescription("a"));
        assertFalse(EventCreationValidator.isValidEventDescription(""));
    }

    @Test
    public void testEventCreationValidation() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        EditText eventTitleInput = new EditText(appContext);
        EditText eventDescriptionInput = new EditText(appContext);

        // Test case 1: valid input
        eventTitleInput.setText("Valid title adfasdf");
        eventDescriptionInput.setText("Valid description with more than 20 characters adsfasdfasdf");
        assertTrue(EventCreationValidator.eventCreationValidation(eventTitleInput, eventDescriptionInput));

        // Test case 2: invalid event title
        eventTitleInput.setText("a");
        eventDescriptionInput.setText("Valid description with more than 20 characters adfasdfasdf");
        assertFalse(EventCreationValidator.eventCreationValidation(eventTitleInput, eventDescriptionInput));
        assertEquals(EventCreationValidator.EVENT_TITLE_ERROR, eventTitleInput.getError().toString());

        // Test case 3: invalid event description
        eventTitleInput.setText("Valid title adfasdf");
        eventDescriptionInput.setText("a");
        assertFalse(EventCreationValidator.eventCreationValidation(eventTitleInput, eventDescriptionInput));
        assertEquals(EventCreationValidator.EVENT_DESCRIPTION_ERROR, eventDescriptionInput.getError().toString());

        // Test case 4: both inputs invalid
        eventTitleInput.setText("");
        eventDescriptionInput.setText("");
        assertFalse(EventCreationValidator.eventCreationValidation(eventTitleInput, eventDescriptionInput));
        assertEquals(EventCreationValidator.EVENT_TITLE_ERROR, eventTitleInput.getError().toString());
        assertEquals(EventCreationValidator.EVENT_DESCRIPTION_ERROR, eventDescriptionInput.getError().toString());
    }
}


