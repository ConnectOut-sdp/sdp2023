package com.sdpteam.connectout.utils;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.drawer.DrawerActivity;

public abstract class DrawerFragment extends Fragment {


    public void setupToolBar(Button fragmentButton, Toolbar fragmentToolbar,String text, View.OnClickListener listener){
        if(getActivity() instanceof DrawerActivity){
            ((DrawerActivity) getActivity()).setupButton(text, listener);
            fragmentButton.setVisibility(View.GONE);
            fragmentToolbar.setVisibility(View.GONE);
        }else {
            fragmentButton.setOnClickListener(listener);
            ((WithFragmentActivity)getActivity()).setSupportActionBar(fragmentToolbar);
            fragmentToolbar.setNavigationOnClickListener(v-> getActivity().finish());
        }
    }
}
