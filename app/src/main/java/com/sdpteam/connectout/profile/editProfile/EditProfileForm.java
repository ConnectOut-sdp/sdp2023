package com.sdpteam.connectout.profile.editProfile;

import static com.sdpteam.connectout.profile.Profile.Gender;

import java.util.Arrays;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.drawer.DrawerActivity;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.remoteStorage.ImageSelectionFragment;
import com.sdpteam.connectout.validation.EditProfileValidator;
import com.sdpteam.connectout.validation.ValidationUtils;

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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditProfileForm extends Fragment {

    /**
     * ViewModelProvider.Factory is an interface which have create method.
     * The create method is responsible for creating our ViewModel's instance
     * It is especially useful to pass either the real ViewModel that uses the real Firebase and Google Auth
     * Or we can use a fake ViewModel for mocking in tests.
     */
    public ViewModelProvider.Factory viewModelFactory; // for testing (mocking)
    private EditProfileViewModel editProfileViewModel;

    private Uri selectedImage = null;

    private boolean submitWasClicked = false; //thus waiting for a response (see getProgress() from the view model)

    public static EditProfileForm newInstance(@Nullable Profile profileToEdit) {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.viewModelFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                EditProfile registrationToFirebase = new EditProfile(new ProfileFirebaseDataSource());
                return (T) new EditProfileViewModel(registrationToFirebase, profileToEdit); // use the real view model
            }
        };
        return editProfileForm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editProfileViewModel = new ViewModelProvider(this, viewModelFactory).get(EditProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_completion_registration_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageSelectionFragment imageSelectionFragment = new ImageSelectionFragment(nonNullString(editProfileViewModel.profile().getProfileImageUrl()));
        imageSelectionFragment.setOnImageSelectedListener(imageUri -> {
            this.selectedImage = imageUri;
        });
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.image_selection_container, imageSelectionFragment);
        transaction.commit();

        EditText nameEditor = view.findViewById(R.id.nameEditText);
        nameEditor.setHint("Name");
        nameEditor.setText(nonNullString(editProfileViewModel.profile().getName()));

        EditText emailEditor = view.findViewById(R.id.emailEditText);
        emailEditor.setHint("Email");
        emailEditor.setText(nonNullString(editProfileViewModel.profile().getEmail()));

        EditText bioEditor = view.findViewById(R.id.bioEditText);
        bioEditor.setHint("Bio");
        bioEditor.setText(nonNullString(editProfileViewModel.profile().getBio()));

        RadioGroup radioGroup = view.findViewById(R.id.radio_group);
        Gender gender = editProfileViewModel.profile().getGender();
        int genderOrdinal = gender == null ? 0 : gender.ordinal();
        radioGroup.check(radioGroup.getChildAt(genderOrdinal).getId());

        Button conditionsInfoButton = view.findViewById(R.id.generalConditions);
        conditionsInfoButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://firebase.google.com/terms");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        CheckBox checkBox = view.findViewById(R.id.checkBox);
        Button finishButton = view.findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            if (EditProfileValidator.editProfileValidation(nameEditor, emailEditor, bioEditor, null, null, null)
                    & ValidationUtils.handleValidationFailure(checkBox.isChecked(), checkBox, "Must accept")) {
                submitForm(nameEditor, emailEditor, bioEditor, radioGroup);
            }
        });

        TextView errView = view.findViewById(R.id.complete_registration_error_msg);
        errView.setText("");

        editProfileViewModel.getErrorMessage().observeForever(s -> {
            if (s != null && s.length() > 0) {
                errView.setText(s);
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            } else {
                errView.setText("");
            }
        });

        editProfileViewModel.getProgress().observeForever(loading -> {
            if (!loading && submitWasClicked && errView.getText().equals("Operation successful")) {
                finishButton.setEnabled(false);
                formSubmittedSuccessfully();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private String nonNullString(String s) {
        return s == null ? "" : s;
    }

    private void submitForm(EditText nameEditor, EditText emailEditor, EditText bioEditor, RadioGroup radioGroup) {
        int i = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
        final Gender gender = Arrays.asList(Gender.MALE, Gender.FEMALE, Gender.OTHER).get(i);
        final String name = nameEditor.getText().toString();
        final String email = emailEditor.getText().toString();
        final String bio = bioEditor.getText().toString();
        submitWasClicked = true;
        editProfileViewModel.saveNewProfile(name, email, bio, gender, selectedImage);
    }

    private void formSubmittedSuccessfully() {
        getActivity().finish();
        if (editProfileViewModel.isFirstTimeFillingProfile()) {
            Intent intent = new Intent(getContext(), DrawerActivity.class);
            startActivity(intent);
        }
    }
}