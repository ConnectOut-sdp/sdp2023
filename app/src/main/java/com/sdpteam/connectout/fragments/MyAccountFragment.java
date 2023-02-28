package com.sdpteam.connectout.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public static class MyAccountViewModel extends ViewModel {
        private final MutableLiveData<String> mText;

        public MyAccountViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is my account fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }
}