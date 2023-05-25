package com.sdpteam.connectout.utils;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;

public class WithIndexMatcher {
    /**
     * Used for testing lists, can take a look at how it was used in EventsActivityTest for example.
     * Not sure if this is the best way to test listviews tho XD
     * If multiple views match 'withId(R.id.event_list_event_title)' you can get the nth by choosing a index n.
     */
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (matcher.matches(view)) {
                    if (currentIndex == index) {
                        currentIndex++;
                        return true;
                    }
                    currentIndex++;
                }
                return false;
            }
        };
    }
}
