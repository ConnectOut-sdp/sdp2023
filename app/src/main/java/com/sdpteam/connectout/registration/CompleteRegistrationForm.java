package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender;

import java.util.Arrays;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CompleteRegistrationForm extends Fragment {

    /**
     * ViewModelProvider.Factory is an interface which have create method.
     * The create method is responsible for creating our ViewModel's instance
     * It is especially useful to pass either the real ViewModel that uses the real Firebase and Google Auth
     * Or we can use a fake ViewModel for mocking in tests.
     */
    public ViewModelProvider.Factory viewModelFactory; // for testing (mocking)
    private RegistrationViewModel registrationViewModel;

    public static CompleteRegistrationForm newInstance() {
        CompleteRegistrationForm completeRegistrationForm = new CompleteRegistrationForm();
        completeRegistrationForm.viewModelFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                CompleteRegistration registrationToFirebase = new CompleteRegistration(new ProfileFirebaseDataSource());
                GoogleAuth googleAuth = new GoogleAuth();
                return (T) new RegistrationViewModel(registrationToFirebase, googleAuth); // use the real view model
            }
        };
        return completeRegistrationForm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationViewModel = new ViewModelProvider(this, viewModelFactory).get(RegistrationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_completion_registration_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText nameEditor = view.findViewById(R.id.nameEditText);
        nameEditor.setText(registrationViewModel.currentName());

        EditText emailEditor = view.findViewById(R.id.emailEditText);
        emailEditor.setText(registrationViewModel.currentEmail());

        EditText bioEditor = view.findViewById(R.id.bioEditText);
        bioEditor.setHint("Bio");

        RadioGroup radioGroup = view.findViewById(R.id.radio_group);

        Button conditionsInfoButton = view.findViewById(R.id.generalConditions);
        conditionsInfoButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://firebase.google.com/terms");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        Button finishButton = view.findViewById(R.id.finishButton);
        finishButton.setEnabled(false);
        finishButton.setOnClickListener(v -> {
            submitForm(nameEditor, emailEditor, bioEditor, radioGroup);
        });

        CheckBox checkBox = view.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(v -> {
            finishButton.setEnabled(checkBox.isChecked());
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void submitForm(EditText nameEditor, EditText emailEditor, EditText bioEditor, RadioGroup radioGroup) {
        int i = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
        final Gender gender = Arrays.asList(Gender.MALE, Gender.FEMALE, Gender.OTHER).get(i);
        final String name = nameEditor.getText().toString();
        final String email = emailEditor.getText().toString();
        final String bio = bioEditor.getText().toString();
        try {
            registrationViewModel.completeRegistration(name, email, bio, gender);
        } catch (IllegalStateException e) {
            // error unhandled for the moment
            throw e;
        }
    }
}