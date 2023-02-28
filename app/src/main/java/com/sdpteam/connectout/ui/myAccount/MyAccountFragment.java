package com.sdpteam.connectout.ui.myAccount;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.databinding.FragmentMyAccountBinding;


public class MyAccountFragment extends Fragment {

    private FragmentMyAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyAccountViewModel MyAccountsViewModel =
                new ViewModelProvider(this).get(MyAccountViewModel.class);

        binding = FragmentMyAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyAccount;
        MyAccountsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}