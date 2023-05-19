package com.sdpteam.connectout.profileList.filter;

import com.google.firebase.database.Query;

/**
 * Filters participants by their ratings within a custom range
 */
public class ProfileRatingFilter implements ProfileFilter {

    private final double minRating;
    private final double maxRating;

    /**
     * @param minRating min rating value (inclusive)
     * @param maxRating max rating value (inclusive)
     */
    public ProfileRatingFilter(double minRating, double maxRating) {
        this.minRating = Math.max(0, minRating);
        this.maxRating = maxRating;
    }

    @Override
    public Query buildQuery(Query root) {
        return root.startAt(minRating).endAt(maxRating);
    }
}
