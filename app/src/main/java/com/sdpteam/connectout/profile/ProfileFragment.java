package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.Profile.NULL_USER;

import com.sdpteam.connectout.QrCode.QRcodeModalActivity;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.drawer.DrawerFragment;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventParticipantIdFilter;
import com.sdpteam.connectout.event.nearbyEvents.list.EventsListViewFragment;
import com.sdpteam.connectout.profile.editProfile.EditProfileActivity;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class ProfileFragment extends DrawerFragment {
    public final static String PASSED_ID_KEY = "uid";

    private final ProfileViewModel pvm = new ProfileViewModel(new ProfileFirebaseDataSource());
    private final ActivityResultLauncher<Intent> qrCodeLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
    });

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
        String displayedUser = arguments != null ? arguments.getString(PASSED_ID_KEY) : null;
        AuthenticatedUser au = new GoogleAuth().loggedUser();
        String uid = (au == null) ? NULL_USER : au.uid;

        String buttonText;
        View.OnClickListener listener;

        Button sharePersonalQrCodeButton = initializeSharePersonalQrCodeButton(view, uid);

        //If current user is selected one:
        if (displayedUser == null || uid.equals(displayedUser)) {
            displayedUser = uid;
            buttonText = "Edit Profile";
            listener = v -> goToEditProfile();
        } else {
            buttonText = "Rate Profile";
            String finalUserIdToDisplay = displayedUser;
            listener = v -> goToProfileRate(finalUserIdToDisplay);

            sharePersonalQrCodeButton.setVisibility(View.GONE);
        }

        setupToolBar(ratingEditButton, toolbar, buttonText, listener);

        pvm.fetchProfile(displayedUser);
        pvm.getProfileLiveData().observe(getViewLifecycleOwner(), profile -> displayProfile(view, profile));

        insertEventsListFragment(displayedUser);
    }

    private Button initializeSharePersonalQrCodeButton(@NonNull View view, String uid) {
        Button sharePersonalQrCodeButton = view.findViewById(R.id.buttonSharePersonalQrCode);
        sharePersonalQrCodeButton.setOnClickListener(v -> {
            String qrCodeData = "profile/" + uid;
            Intent intent = new Intent(getActivity(), QRcodeModalActivity.class);
            intent.putExtra("title", "Profile QR code");
            intent.putExtra("qrCodeData", qrCodeData);

            qrCodeLauncher.launch(intent);
        });

        return sharePersonalQrCodeButton;
    }

    private void insertEventsListFragment(String userId) {
        EventsViewModel viewModel = new EventsViewModel(new EventFirebaseDataSource(), new EventParticipantIdFilter(userId));
        EventsListViewFragment eventsListViewFragment = new EventsListViewFragment(viewModel);
        getChildFragmentManager().beginTransaction().replace(R.id.profile_events_container, eventsListViewFragment).commit();
    }

    private void displayProfile(View view, Profile profile) {
        TextView name = view.findViewById(R.id.profileName);
        TextView email = view.findViewById(R.id.profileEmail);
        TextView bio = view.findViewById(R.id.profileBio);
        TextView gender = view.findViewById(R.id.profileGender);

        name.setText(profile.getName());
        email.setText(profile.getEmail());
        bio.setText(profile.getBio());
        gender.setText(profile.getGender().name());

        ImageView profilePicture = view.findViewById(R.id.profilePicture);
        String imageUrl = profile.getProfileImageUrl();
        Picasso.get().load(imageUrl).into(profilePicture);
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        EditProfileActivity.profileToEdit = pvm.getProfileLiveData().getValue();
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

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = pvm.getProfileLiveData().getValue();
        if (profile != null) pvm.fetchProfile(profile.getId()); // refresh when coming back from edit

    }
}

