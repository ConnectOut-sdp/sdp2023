package com.sdpteam.connectout.userList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;

import java.util.List;

public class UserListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_user_list, container, false);
        UserListViewModel mViewModel = new UserListViewModel(new UserListModel());
        List<Profile> sortedProfileList = mViewModel.getProfileSorted();

        ProfilesAdapter adapter = new ProfilesAdapter(container.getContext(),  R.layout.adapter_text_view, sortedProfileList);
        ListView listView = contentView.findViewById(R.id.user_list_view);
        listView.setAdapter(adapter);
        return contentView;
    }


}