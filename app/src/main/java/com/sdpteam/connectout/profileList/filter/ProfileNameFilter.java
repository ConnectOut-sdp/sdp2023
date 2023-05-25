package com.sdpteam.connectout.profileList.filter;

import com.sdpteam.connectout.profile.ProfileEntry;

/**
 * Filters participants by their name.
 */
public class ProfileNameFilter implements ProfileFilter {

    private final String text;

    public ProfileNameFilter(String text) {
        this.text = text.trim().toLowerCase();
    }

    @Override
    public boolean test(ProfileEntry entry) {
        return entry.getProfile().getNameLowercase().contains(text);
    }
}
