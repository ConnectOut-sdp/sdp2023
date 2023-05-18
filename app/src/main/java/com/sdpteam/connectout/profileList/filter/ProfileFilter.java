package com.sdpteam.connectout.profileList.filter;

import static com.sdpteam.connectout.profile.ProfileFirebaseDataSource.PROFILE;

import com.google.firebase.database.Query;

public interface ProfileFilter {

    ProfileFilter NONE = root -> root.orderByChild(PROFILE + "/nameLowercase");

    /**
     * Build up a more constrained query based on top of some root query
     *
     * @param root (Query): query on which we add constraints
     * @return new query with additional filters
     */
    Query buildQuery(Query root);

    default ProfileFilter and(ProfileFilter other) {
        return root -> buildQuery(other.buildQuery(root));
    }
}
