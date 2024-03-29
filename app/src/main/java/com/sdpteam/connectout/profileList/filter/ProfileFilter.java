package com.sdpteam.connectout.profileList.filter;

import java.util.function.Predicate;

import com.sdpteam.connectout.profile.ProfileEntry;

public interface ProfileFilter extends Predicate<ProfileEntry> {

    ProfileFilter NONE = entry -> true;

    @Override
    boolean test(ProfileEntry entry);

    /**
     * Combine two filters with the "AND" operator.
     *
     * @param otherFilter (ProfileFilter): the other filter to combine with.
     * @return new combined filter.
     */
    default ProfileFilter and(ProfileFilter otherFilter) {
        return entry -> test(entry) && otherFilter.test(entry);
    }
}
