package com.sdpteam.connectout.userList;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

public class UserListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_user_list, container, false);

        UserListViewModel usersListViewModel = new UserListViewModel(new ProfileFirebaseDataSource());
        usersListViewModel.triggerFetchProfileSortedByRating();

        usersListViewModel.getUserListLiveData().observeForever(profiles -> {
            ProfilesAdapter adapter = new ProfilesAdapter(container.getContext(), R.layout.adapter_text_view, profiles);
            ListView listView = contentView.findViewById(R.id.user_list_view);
            listView.setAdapter(adapter);
        });

        return contentView;
    }
}