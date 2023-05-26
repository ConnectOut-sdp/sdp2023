package com.sdpteam.connectout.profileList;

import static com.sdpteam.connectout.utils.FutureUtils.waitABit;

import org.junit.Rule;
import org.junit.Test;

import com.sdpteam.connectout.chat.ChatActivity;

import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

public class EventParticipantsListActivityTest {
    @Rule
    public ActivityTestRule<EventParticipantsListActivity> testRule = new ActivityTestRule<EventParticipantsListActivity>(EventParticipantsListActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), EventParticipantsListActivity.class);
            intent.putExtra("eventId", "yourEventId");
            return intent;
        }
    };
    @Test
    public void doesNotCrash() {
        waitABit();
    }

    @Test
    public void launchParticipantsIntent() {
         EventParticipantsListActivity.showParticipants(testRule.getActivity(), "eventId");
    }
}
