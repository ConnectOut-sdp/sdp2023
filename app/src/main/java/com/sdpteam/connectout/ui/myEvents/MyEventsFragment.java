package com.sdpteam.connectout.ui.myEvents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.databinding.FragmentMyEventsBinding;

public class MyEventsFragment extends Fragment {

    private FragmentMyEventsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyEventsViewModel MyEventsViewModel =
                new ViewModelProvider(this).get(MyEventsViewModel.class);

        binding = FragmentMyEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyEvents;
        MyEventsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}