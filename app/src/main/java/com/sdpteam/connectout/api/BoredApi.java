package com.sdpteam.connectout.api;

import com.sdpteam.connectout.models.BoredActivity;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *  interface that represents the data fetched from the api
 */
public interface BoredApi {
    @GET("activity")
    public Call<BoredActivity> getActivity();
}
