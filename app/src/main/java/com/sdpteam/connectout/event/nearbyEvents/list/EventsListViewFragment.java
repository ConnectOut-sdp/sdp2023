package com.sdpteam.connectout.event.nearbyEvents.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;

public class EventsListViewFragment extends Fragment {

    private final EventsViewModel eventsViewModel;

    public EventsListViewFragment(EventsViewModel eventsViewModel) {
        this.eventsViewModel = eventsViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_event_list, container, false);

        eventsViewModel.refreshEvents();

        eventsViewModel.getEventListLiveData().observeForever(events -> {
            EventsAdapter adapter = new EventsAdapter(container.getContext(), R.layout.event_list_item_view, events);
            ListView listView = contentView.findViewById(R.id.events_list_view);
            listView.setAdapter(adapter);
        });

        return contentView;
    }
}