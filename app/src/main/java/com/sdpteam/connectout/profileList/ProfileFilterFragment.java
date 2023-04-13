package com.sdpteam.connectout.profileList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

/**
 * Adds a filtering context and features to the list of profile displayed.
 */
public class ProfileFilterFragment extends Fragment {

    private final ProfileListFragment profileListFragment;

    public ProfileFilterFragment() {
        profileListFragment = new ProfileListFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Find the filtering fragment.
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        //write into the filtering fragment, the list.
        this.getChildFragmentManager().beginTransaction().replace(R.id.filter_container, profileListFragment).commit();

        //Retrieve Filter tools.
        RadioGroup categoryButton = rootView.findViewById(R.id.filter_category_button);
        RadioButton nameButton = rootView.findViewById(R.id.name_switch_button);
        RadioButton ratingButton = rootView.findViewById(R.id.rating_switch_button);

        ToggleButton applicationButton = rootView.findViewById(R.id.filter_apply_button);
        EditText text = rootView.findViewById(R.id.text_filter);

        //Upon click, apply changes on the list.
        categoryButton.setOnCheckedChangeListener((group, checkedId) -> {
            if(R.id.name_switch_button == checkedId){
                ratingButton.setChecked(false);
            }else{
                nameButton.setChecked(false);
            }
            swapTypeOnClick(nameButton.isChecked(), text.getText().toString());
        });
        applicationButton.setOnClickListener(v -> swapTypeOnClick(nameButton.isChecked(), text.getText().toString()));

        return rootView;
    }

    /**
     * Stop observing changes with the given filters on the list view
     */
    public void stopObservation() {
        profileListFragment.stopObservation();
    }

    /**
     * Swaps the list's observer with one applying the given filters.
     *
     * @param isChecked (boolean): true if ordering users by name, false by rating.
     * @param userInput (String): filters of name or rating given by the user.
     */
    private void swapTypeOnClick(boolean isChecked, String userInput) {
        if (isChecked) {
            profileListFragment.changeObserved(ProfileFirebaseDataSource.ProfileOrderingOption.NAME, userInput);
        } else {
            profileListFragment.changeObserved(ProfileFirebaseDataSource.ProfileOrderingOption.RATING, userInput);
        }
    }
}
