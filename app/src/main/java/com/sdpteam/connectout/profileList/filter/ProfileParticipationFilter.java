package com.sdpteam.connectout.profileList.filter;

import com.google.firebase.database.Query;

public class ProfileParticipationFilter implements ProfileFilter {

    public final String eventId;

    public ProfileParticipationFilter(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public Query buildQuery(Query root) {
        if (eventId == null || eventId.isEmpty()) {
            return root;
        }
        return root.orderByChild("RegisteredEvents/eventId").equalTo(eventId);
    }
}
