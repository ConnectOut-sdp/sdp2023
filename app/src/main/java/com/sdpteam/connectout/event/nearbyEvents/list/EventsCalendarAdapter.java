package com.sdpteam.connectout.event.nearbyEvents.list;

import java.util.List;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.viewer.EventActivity;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EventsCalendarAdapter extends ArrayAdapter<Event> {

    private final int eventListItemResource;

    /**
     * @param context               The context in which the adapter is being used.
     * @param eventListItemResource The resource ID for the layout file of each list item.
     * @param events                The list of events to display in the ListView.
     */
    public EventsCalendarAdapter(@NonNull Context context, int eventListItemResource, @NonNull List<Event> events) {
        super(context, eventListItemResource, events);
        this.eventListItemResource = eventListItemResource;
    }

    public EventsCalendarAdapter(@NonNull Context context, int eventListItemResource) {
        super(context, eventListItemResource);
        this.eventListItemResource = eventListItemResource;
    }

    /**
     * Creating and returning the view for each list item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(eventListItemResource, parent, false);
        }

        final Event event = getItem(position);
        final String orgProfileId = event.getOrganizer();

        TextView eventTitle = view.findViewById(R.id.registered_event_title);
        eventTitle.setText(event.getTitle());

        TextView registeredEventTime = view.findViewById(R.id.registered_event_time);
        // Format the date before showing it
        registeredEventTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                event.getDate()));

        registeredEventTime.setGravity(Gravity.CENTER);
        eventTitle.setGravity(Gravity.CENTER);

        view.setOnClickListener(v -> EventActivity.openEvent(getContext(), event.getId()));
        return view;
    }
}
