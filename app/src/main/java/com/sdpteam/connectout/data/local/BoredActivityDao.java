package com.sdpteam.connectout.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sdpteam.connectout.models.BoredActivity;

import java.util.List;

/**
 *  DAO : data access object interface - which allows to communicate with local database
 */

@Dao
public interface BoredActivityDao {
    @Query("SELECT * FROM activity_table")
    List<BoredActivity> getAllActivities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertActivity(BoredActivity activity);
}
