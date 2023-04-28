package com.sdpteam.connectout.event.nearbyEvents.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.profile.ProfileActivity;

import java.util.List;

/**
 * It is responsible for creating and managing the views for a list of events.
 * The class holds the context, resource, and a list of events, which are passed in through the constructor.
 */
public class EventsAdapter extends ArrayAdapter<Event> {

    private final int eventListItemResource;

    /**
     * @param context               The context in which the adapter is being used.
     * @param eventListItemResource The resource ID for the layout file of each list item.
     * @param events                The list of events to display in the ListView.
     */
    public EventsAdapter(@NonNull Context context, int eventListItemResource, @NonNull List<Event> events) {
        super(context, eventListItemResource, events);
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

        Event event = getItem(position);

        TextView eventTitle = view.findViewById(R.id.event_list_event_title);
        eventTitle.setText(event.getTitle());

        TextView eventDescription = view.findViewById(R.id.event_list_event_description);
        eventDescription.setText(event.getDescription());

        ImageButton profileImageButton = view.findViewById(R.id.event_list_profile_button);
        profileImageButton.setOnClickListener(v -> ProfileActivity.openProfile(getContext(), event.getOrganizer()));

        ImageView eventImage = view.findViewById(R.id.event_list_event_image);
        //TODO set images
        view.setOnClickListener(v -> EventActivity.openEvent(getContext(), event.getId()));
        return view;
    }
}

