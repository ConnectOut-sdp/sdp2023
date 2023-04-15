package com.sdpteam.connectout.profile;

import android.view.View;
import android.widget.ListAdapter;

import com.firebase.ui.database.FirebaseListOptions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface RegisteredEventsRepository {

    void registerToEvent(Profile.CalendarEvent calEvent, String profileId);
    void setUpListAdapter(Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLayout,
                          Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLifecycleOwner,
                          BiConsumer<View, Profile.CalendarEvent> populateView,
                          Consumer<ListAdapter> setAdapter, String profileId);
}
