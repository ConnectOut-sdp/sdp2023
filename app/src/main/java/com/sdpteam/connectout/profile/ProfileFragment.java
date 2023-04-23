package com.sdpteam.connectout.profile;

import static android.view.View.INVISIBLE;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.utils.DrawerFragment;

public class ProfileFragment extends DrawerFragment {
    public final static String PASSED_ID_KEY = "uid";

    private final ProfileViewModel pvm = new ProfileViewModel(new ProfileFirebaseDataSource());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button ratingEditButton = view.findViewById(R.id.buttonRatingEditProfile);
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);

        Bundle arguments = getArguments();
        String userIdToDisplay = arguments != null ? arguments.getString(PASSED_ID_KEY) : null;
        AuthenticatedUser au = new GoogleAuth().loggedUser();
        String uid = (au == null) ? NULL_USER : au.uid;

        String buttonText;
        View.OnClickListener listener;

        //If current user is selected one:
        if(userIdToDisplay == null || uid.equals(userIdToDisplay)){
            userIdToDisplay = uid;
            buttonText = "Edit Profile";
            listener = v->goToEditProfile();
        }else{
            buttonText = "Rate Profile";
            String finalUserIdToDisplay = userIdToDisplay;
            listener = v -> goToProfileRate(finalUserIdToDisplay);
        }

        setupToolBar(ratingEditButton, toolbar, buttonText, listener);

        pvm.fetchProfile(userIdToDisplay);
        pvm.getProfileLiveData().observe(getViewLifecycleOwner(), profile -> setTextViewsTo(view, profile));
    }

    private void setTextViewsTo(View view, Profile user) {
        TextView name = view.findViewById(R.id.profileName);
        TextView email = view.findViewById(R.id.profileEmail);
        TextView bio = view.findViewById(R.id.profileBio);
        TextView gender = view.findViewById(R.id.profileGender);

        name.setText(user.getName());
        email.setText(user.getEmail());
        bio.setText(user.getBio());
        gender.setText(user.getGender().name());
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void goToProfileRate(String id) {
        Intent intent = new Intent(getActivity(), ProfileRateActivity.class);
        intent.putExtra(PASSED_ID_KEY, id);

        TextView viewById = getActivity().findViewById(R.id.profileName);
        String userName = (String) viewById.getText();
        intent.putExtra("name", userName);
        startActivity(intent);
    }

    /**
     * Method used to launch a Profile Fragment with a given Id.
     *
     * @param profileId (String): Id of the profile to display
     * @return (ProfileFragment): fragment instanced with the given Id
     */
    public static ProfileFragment setupFragment(String profileId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(PASSED_ID_KEY, profileId);
        fragment.setArguments(args);
        return fragment;
    }
}

