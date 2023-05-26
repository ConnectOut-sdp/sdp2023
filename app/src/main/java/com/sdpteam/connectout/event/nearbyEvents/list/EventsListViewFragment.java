package com.sdpteam.connectout.event.nearbyEvents.list;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EventsListViewFragment extends Fragment {

    private final EventsViewModel eventsViewModel;
    private ArrayAdapter<Event> adapter;

    /**
     * @param eventsViewModel The view model for the events.
     */
    public EventsListViewFragment(EventsViewModel eventsViewModel) {
        this.eventsViewModel = eventsViewModel;
    }

    /**
     * @param eventsViewModel The view model for the events.
     * @param adapter         The adapter for the list view (which specifies how to create the views for each list item).
     */
    public EventsListViewFragment(EventsViewModel eventsViewModel, ArrayAdapter<Event> adapter) {
        this.eventsViewModel = eventsViewModel;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_event_list, container, false);

        if (adapter == null) {
            this.adapter = new EventsAdapter(container.getContext(), R.layout.event_list_item_view);
        }

        ListView listView = contentView.findViewById(R.id.events_list_view);
        listView.setAdapter(adapter);

        eventsViewModel.refreshEvents();
        eventsViewModel.getEventListLiveData().observeForever(events -> {
            adapter.clear();
            adapter.addAll(events);
            adapter.notifyDataSetChanged();
        });

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        eventsViewModel.refreshEvents();
    }
}