package com.sdpteam.connectout.drawer;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.userList.ProfileListOption;
import com.sdpteam.connectout.userList.UserListFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class FilterFragment extends Fragment {

    private final UserListFragment userListFragment;

    public FilterFragment(){
        userListFragment = new UserListFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        this.getChildFragmentManager().beginTransaction().replace(R.id.filter_container, userListFragment).commit();

        ToggleButton categoryButton = rootView.findViewById(R.id.filter_category_button);
        ToggleButton applicationButton = rootView.findViewById(R.id.filter_apply_button);
        EditText text = rootView.findViewById(R.id.text_filter);

        applicationButton.setOnClickListener(v-> {
                    if (categoryButton.isChecked()) {
                        userListFragment.changeObserved(ProfileListOption.NAME, Collections.singletonList(text.getText().toString()));
                    } else {
                        String[] rangeText = text.getText().toString().split(",");
                        userListFragment.changeObserved(ProfileListOption.RATING, Arrays.stream(rangeText).collect(Collectors.toList()));
                    }
                }
        );
        return rootView;
    }

    public void stopObservation(){
        userListFragment.stopObservation();
    }
}