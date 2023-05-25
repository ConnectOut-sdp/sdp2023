package com.sdpteam.connectout.event.nearbyEvents.filter;

import android.icu.util.Calendar;

import com.sdpteam.connectout.event.Event;

import java.util.GregorianCalendar;

public class EventDateFilter implements EventFilter {

    private final Calendar calendar;

    public EventDateFilter(Calendar calendar){

        this.calendar = calendar;
    }
    @Override
    public boolean test(Event event) {
        if(calendar == null){
            return true;
        }
        Calendar eventCalendar = Calendar.getInstance();
        eventCalendar.setTimeInMillis(event.getDate());

        return eventCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                eventCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
    }
}
