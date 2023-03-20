package com.sdpteam.connectout.userList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;

public class FilterFragment extends Fragment {

    private final UserListFragment userListFragment;

    public FilterFragment() {
        userListFragment = new UserListFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Find the filtering fragment.
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        //write into the filtering fragment, the list.
        this.getChildFragmentManager().beginTransaction().replace(R.id.filter_container, userListFragment).commit();

        //Retrieve Filter tools.
        ToggleButton categoryButton = rootView.findViewById(R.id.filter_category_button);
        ToggleButton applicationButton = rootView.findViewById(R.id.filter_apply_button);
        EditText text = rootView.findViewById(R.id.text_filter);

        //Upon click, apply changes on the list.
        applicationButton.setOnClickListener(v -> swapTypeOnClick(categoryButton.isChecked(), text.getText().toString()));
        categoryButton.setOnClickListener(v -> swapTypeOnClick(categoryButton.isChecked(), text.getText().toString()));

        return rootView;
    }

    /**
     * Stop observing changes with the given filters on the list view
     */
    public void stopObservation() {
        userListFragment.stopObservation();
    }

    /**
     * Swaps the list's observer with one applying the given filters.
     *
     * @param isChecked (boolean): true if ordering users by name, false by rating.
     * @param userInput (String): filters of name or rating given by the user.
     */
    private void swapTypeOnClick(boolean isChecked, String userInput) {
        if (isChecked) {
            userListFragment.changeObserved(OrderingOption.NAME, userInput);
        } else {
            userListFragment.changeObserved(OrderingOption.RATING, userInput);
        }
    }
}