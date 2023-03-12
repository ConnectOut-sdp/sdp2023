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

    private final Context mContext;
    private final int mResource;

    public ProfilesAdapter(@NonNull Context context, int resource, @NonNull List<Profile> profiles) {
        super(context, resource, profiles);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }

        Profile profile = getItem(position);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        TextView bioTextView = view.findViewById(R.id.bioTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);

        nameTextView.setText(profile.getName());
        emailTextView.setText(profile.getEmail());
        bioTextView.setText(profile.getBio());
        ratingTextView.setText(String.valueOf(profile.getRating()));

        return view;
    }
}

