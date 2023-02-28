package com.sdpteam.connectout.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.databinding.FragmentFilterBinding;

public class FilterFragment extends Fragment {

    private FragmentFilterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FilterViewModel galleryViewModel =
                new ViewModelProvider(this).get(FilterViewModel.class);

        binding = FragmentFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textFilter;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}