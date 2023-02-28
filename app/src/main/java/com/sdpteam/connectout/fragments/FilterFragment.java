package com.sdpteam.connectout.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.databinding.FragmentFilterBinding;

public class FilterFragment extends Fragment {

    private FragmentFilterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FilterViewModel filterViewModel =
                new ViewModelProvider(this).get(FilterViewModel.class);

        //Add to this fragment, the home fragment to filter
        this.getChildFragmentManager().beginTransaction().add(R.id.filter_container, new HomeFragment()).commit();
        binding = FragmentFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class FilterViewModel extends ViewModel {

        private final MutableLiveData<String> mText;

        public FilterViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is filter fragment");
        }
    }
}