package com.sdpteam.connectout.userList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;

import java.util.List;

public class ProfilesAdapter extends ArrayAdapter<Profile> {

    private final Context context;
    private final int resource;

    /**
     * Makes the array of profiles, containable in a list.
     *
     * @param context (Context): context of the current view
     * @param resource (int): layout that is being inflated with the given list of profiles
     * @param profiles (List<Profile>): List of the given profile that is required to be displayed.
     */
    public ProfilesAdapter(@NonNull Context context, int resource, @NonNull List<Profile> profiles) {
        super(context, resource, profiles);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Find or create the current view of the profile in the list.
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        }
        //Retrieve the current profile according to its position.
        Profile profile = getItem(position);

        //Upon retrieval of the profile, find the corresponding text fields and assign them.
        TextView nameTextView = view.findViewById(R.id.nameAdapterTextView);
        TextView emailTextView = view.findViewById(R.id.emailAdapterTextView);
        TextView bioTextView = view.findViewById(R.id.bioAdapterTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingAdapterTextView);

        nameTextView.setText(profile.getName());
        emailTextView.setText(profile.getEmail());
        bioTextView.setText(profile.getBio());
        ratingTextView.setText(String.valueOf(profile.getRating()));

        //Return the view of the current list element.
        return view;
    }

}

