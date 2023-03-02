package com.sdpteam.connectout.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;


public class FilterFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Add to this fragment, the home fragment to filter
        this.getChildFragmentManager().beginTransaction().replace(R.id.filter_container, new HomeFragment()).commit();
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

}