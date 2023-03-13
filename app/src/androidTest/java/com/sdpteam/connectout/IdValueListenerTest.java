package com.sdpteam.connectout;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.sdpteam.connectout.event.Event;

import org.junit.Test;

public class IdValueListenerTest {

    @Test
    public void logsCorrectValueOnFail(){
        MutableLiveData<Event> value = new MutableLiveData<>();
        IdValueListener<Event> eventIdValueListener = new IdValueListener<>(Event.class, value);
        eventIdValueListener.onCancelled(DatabaseError.fromException(new DatabaseException("Test exception")));
    }
}
