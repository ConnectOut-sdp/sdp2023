package com.sdpteam.connectout.event.nearbyEvents.list;

import java.util.List;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.ProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * It is responsible for creating and managing the views for a list of events.
 * The class holds the context, resource, and a list of events, which are passed in through the constructor.
 */
public class EventsAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final int resource;

    /**
     * @param context  The context in which the adapter is being used.
     * @param resource The resource ID for the layout file of each list item.
     * @param events   The list of events to display in the ListView.
     */
    public EventsAdapter(@NonNull Context context, int resource, @NonNull List<Event> events) {
        super(context, resource, events);
        this.context = context;
        this.resource = resource;
    }

    /**
     * Creating and returning the view for each list item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        }

        Event event = getItem(position);

        TextView eventTitle = view.findViewById(R.id.event_list_event_title);
        eventTitle.setText(event.getTitle());

        TextView eventDescription = view.findViewById(R.id.event_list_event_description);
        eventDescription.setText(event.getDescription());

        ImageButton profileImageButton = view.findViewById(R.id.event_list_profile_button);
        profileImageButton.setOnClickListener(v -> {
            openProfile(event.getOrganizer());
        });

        ImageView eventImage = view.findViewById(R.id.event_list_event_image);
        //TODO set images

        return view;
    }

    private void openProfile(String profileId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("uid", profileId);
        context.startActivity(intent);
    }
}

