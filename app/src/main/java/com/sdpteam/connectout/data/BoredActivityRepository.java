package com.sdpteam.connectout.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.data.local.BoredActivityDao;
import com.sdpteam.connectout.api.BoredApi;
import com.sdpteam.connectout.data.local.ActivityDatabase;
import com.sdpteam.connectout.data.remote.RetrofitClientInstance;
import com.sdpteam.connectout.models.BoredActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class that handles interactions between the API and the local
 *     - interacts with BoredApi (when connected to the internet)
 *     - interacts with BoredActivityDao
 */

public class BoredActivityRepository {
    private BoredApi api;
    private BoredActivityDao dao;
    private MutableLiveData<List<BoredActivity>> allActivities;

    /**
     * Public Constructor for the BoredActivityRepository
     * @param application
     */
    public BoredActivityRepository(Application application) {
        ActivityDatabase database = ActivityDatabase.getInstance(application);
        dao = database.activityDao();
        api = RetrofitClientInstance.getRetrofitInstance().create(BoredApi.class);
        allActivities = new MutableLiveData<>();
        allActivities.setValue(dao.getAllActivities());
    }

    /**
     * Fetches a new activity from the API and stores it in the local database using the DAO
     * On failure, it logs the error,
     * todo must return cached data on failure
     */
    public void refreshActivities() {
        api.getActivity().enqueue(new Callback<BoredActivity>() {
            @Override
            public void onResponse(Call<BoredActivity> call, Response<BoredActivity> response) {
                if (response.isSuccessful()) {
                    BoredActivity activity = response.body();
                    dao.insertActivity(activity);
                }
            }

            @Override
            public void onFailure(Call<BoredActivity> call, Throwable t) {
                // logged locally to the console - in development
                Log.e("BoredActivityRepository", "Failed to get activity: " + t.getMessage());
            }
        });
    }

    /**
     * Method that returns all activities stored in the local database
     * Observable -> changes are observed
     * @return allActivities object
     */
    public LiveData<List<BoredActivity>> getAllActivities() {
        return allActivities;
    }
}
