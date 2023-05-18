package com.sdpteam.connectout.profileList.filter;

import static com.sdpteam.connectout.profile.ProfileFirebaseDataSource.PROFILE;

import com.google.firebase.database.Query;

/**
 * Filters participants by their name.
 */
public class ProfileNameFilter implements ProfileFilter {

    private final static String QUERY_END = "\uf8ff";
    private final String text;

    public ProfileNameFilter(String text) {
        this.text = text.trim().toLowerCase();
    }

    @Override
    public Query buildQuery(Query root) {
        // The regex is used to ensure that we retrieve all names starting with the given string
        return root.orderByChild(PROFILE + "/nameLowercase")
                .startAt(text).endAt(text + QUERY_END);
    }
}
