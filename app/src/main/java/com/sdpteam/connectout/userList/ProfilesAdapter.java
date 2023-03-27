package com.sdpteam.connectout.userList;

import java.util.List;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Android List view stuff
 */
public class ProfilesAdapter extends ArrayAdapter<Profile> {

    private final Context context;
    private final int resource;

    public ProfilesAdapter(@NonNull Context context, int resource, @NonNull List<Profile> profiles) {
        super(context, resource, profiles);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        }

        Profile profile = getItem(position);

        TextView nameTextView = view.findViewById(R.id.nameAdapterTextView);
        TextView emailTextView = view.findViewById(R.id.emailAdapterTextView);
        TextView bioTextView = view.findViewById(R.id.bioAdapterTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingAdapterTextView);

        nameTextView.setText(profile.getName());
        emailTextView.setText(profile.getEmail());
        bioTextView.setText(profile.getBio());
        ratingTextView.setText(String.valueOf(profile.getRating()));

        return view;
    }
}

