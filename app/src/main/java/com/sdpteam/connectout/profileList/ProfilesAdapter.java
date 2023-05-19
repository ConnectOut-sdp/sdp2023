package com.sdpteam.connectout.profileList;

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
import com.sdpteam.connectout.profile.ProfileActivity;

import java.util.List;

/**
 * It is responsible for creating and managing the views for a list of profiles.
 * The class holds the context, resource, and a list of profiles, which are passed in through the constructor.
 */
public class ProfilesAdapter extends ArrayAdapter<Profile> {

    private final Context context;
    private final int resource;

    /**
     * @param context  The context in which the adapter is being used.
     * @param resource The resource ID for the layout file of each list item.
     * @param profiles The list of profiles to display in the ListView.
     */
    public ProfilesAdapter(@NonNull Context context, int resource, @NonNull List<Profile> profiles) {
        super(context, resource, profiles);
        this.context = context;
        this.resource = resource;
    }

    /**
     * Creating and returning the view for each list item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        }

        Profile profile = getItem(position);
        if (profile == null) {
            return view; // TODO Understand why it can happen and fix it :)
        }


        TextView nameTextView = view.findViewById(R.id.nameAdapterTextView);
        TextView emailTextView = view.findViewById(R.id.emailAdapterTextView);
        TextView bioTextView = view.findViewById(R.id.bioAdapterTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingAdapterTextView);

        nameTextView.setText(profile.getName());
        emailTextView.setText(profile.getEmail());
        bioTextView.setText(profile.getBio());
        ratingTextView.setText(String.valueOf(profile.getRating()));
        view.setOnClickListener(v -> ProfileActivity.openProfile(getContext(), profile.getId()));

        return view;
    }
}

