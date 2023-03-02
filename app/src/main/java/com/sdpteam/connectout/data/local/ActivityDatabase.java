package com.sdpteam.connectout.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Room;

import com.sdpteam.connectout.models.BoredActivity;

/**
 *  Room database that will hold the data
 */

@Database(entities = {BoredActivity.class}, version = 1)
public abstract class ActivityDatabase extends RoomDatabase {
    public abstract BoredActivityDao activityDao();

    private static ActivityDatabase instance;

    public static synchronized ActivityDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ActivityDatabase.class, "activity_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
