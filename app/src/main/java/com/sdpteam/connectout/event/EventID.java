package com.sdpteam.connectout.event;

import com.sdpteam.connectout.common.EntityID;

public class EventID extends EntityID {

    public static final EventID UNSET = new EventID("");

    public EventID(String value) {
        super(value);
    }
}
