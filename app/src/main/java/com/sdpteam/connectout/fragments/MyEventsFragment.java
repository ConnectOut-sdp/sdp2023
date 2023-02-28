package com.sdpteam.connectout.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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

    public static class MyEventsViewModel extends ViewModel {

        private final MutableLiveData<String> mText;

        public MyEventsViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is my event fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }
}