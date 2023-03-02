package com.sdpteam.connectout.models;


import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import android.os.Bundle;

import com.sdpteam.connectout.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Schema that represents a bored activity (entity)
 */

@Entity(tableName = "activity_table")
public class BoredActivity {

    // fields, constructor, getters and setters
    @PrimaryKey
    @NonNull
    private String id;
    private String activity;
    private double accessibility;
    private double price;
    private String type;

    @NonNull
    public String getId() {
        return id;
    }

    public String getActivity() {
        return activity;
    }

    public double getAccessibility() {
        return accessibility;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setAccessibility(double accessibility) {
        this.accessibility = accessibility;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }
}










