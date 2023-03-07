package com.sdpteam.connectout.registration;

import com.sdpteam.connectout.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CompleteRegistrationForm extends Fragment {

    private RegistrationViewModel mViewModel;

    public static CompleteRegistrationForm newInstance() {
        return new CompleteRegistrationForm();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_completion_registration_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText name = view.findViewById(R.id.nameEditText);
        name.setEnabled(false);

        EditText email = view.findViewById(R.id.emailEditText);
        email.setEnabled(false);

        EditText bio = view.findViewById(R.id.bioEditText);
        bio.setHint("Bio");

        Button conditionsInfoButton = view.findViewById(R.id.moreInfoText);
        conditionsInfoButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://google.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        super.onViewCreated(view, savedInstanceState);
    }
}