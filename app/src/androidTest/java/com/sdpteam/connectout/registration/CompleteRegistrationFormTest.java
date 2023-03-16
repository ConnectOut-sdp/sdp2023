package com.sdpteam.connectout.registration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.profile.Profile.Gender.FEMALE;
import static com.sdpteam.connectout.profile.Profile.Gender.MALE;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;

import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDirectory;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class CompleteRegistrationFormTest {

    private RegistrationViewModel viewModel;
    private final MutableLiveData<Profile> databaseContent = new MutableLiveData<>();
    private final ProfileDirectory fakeProfilesDatabase = new ProfileDirectory() {
        @Override
        public void saveProfile(Profile profile) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                databaseContent.setValue(profile);
            });
        }

        @Override
        public LiveData<Profile> fetchProfile(String uid) {
            return databaseContent;
        }
    };

    @Before
    public void setUp() {
        Intents.init();

        Authentication fakeAuth = new Authentication() {
            @Override
            public boolean isLoggedIn() {
                return true;
            }

            @Override
            public AuthenticatedUser loggedUser() {
                return new AuthenticatedUser("007", "Donald", "donald@gmail.com");
            }

            @Override
            public void logout() {

            }

            @Override
            public Intent buildIntent() {
                return null;
            }
        };

        viewModel = new RegistrationViewModel(new CompleteRegistration(fakeProfilesDatabase), fakeAuth);
        fakeProfilesDatabase.saveProfile(new Profile("007", "Donald", "donald@gmail.com", "bioooo", FEMALE, 0, 0));

        CompleteRegistrationForm myFragment = new CompleteRegistrationForm();
        myFragment.viewModelFactory = new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                return (T) viewModel; //mock a fake view model (had to do that because otherwise the fragment will use the real one in onCreate() before I can change it to the mocked one)
            }
        };
        // run myFragment :
        FragmentScenario<CompleteRegistrationForm> scenario = FragmentScenario.launchInContainer(CompleteRegistrationForm.class, null, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                if (className.equals(CompleteRegistrationForm.class.getName())) {
                    return myFragment;
                }
                return super.instantiate(classLoader, className);
            }
        });
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void whenOpeningItShouldShowInitiallyTheLoggedUserNameAndEmail() {
        withId(R.id.nameEditText).matches(withText("Donald"));
        withId(R.id.emailEditText).matches(withText("donald@gmail.com"));
    }

    @Test
    public void fieldsInformationAreSentWhenCompletingTheRegistration() {
        onView(withId(R.id.nameEditText)).perform(typeText(" Trump"));
        onView(withId(R.id.bioEditText)).perform(typeText("My awesome bio"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.radioMale)).perform(click());
        onView(withId(R.id.checkBox)).perform(click());
        onView(withId(R.id.finishButton)).perform(click());
        Profile updatedProfile = LiveDataTestUtil.toCompletableFuture(fakeProfilesDatabase.fetchProfile("007")).join();

        MatcherAssert.assertThat(updatedProfile.getId(), is("007"));
        MatcherAssert.assertThat(updatedProfile.getName(), is("Donald Trump"));
        MatcherAssert.assertThat(updatedProfile.getEmail(), is("donald@gmail.com"));
        MatcherAssert.assertThat(updatedProfile.getBio(), is("My awesome bio"));
        MatcherAssert.assertThat(updatedProfile.getGender(), is(MALE));
    }

    @Test
    public void notLoggedShouldThrowException() {

        Authentication notLoggedInAuth = new Authentication() {
            @Override
            public boolean isLoggedIn() {
                return false;
            }

            @Override
            public AuthenticatedUser loggedUser() {
                return new AuthenticatedUser("0", "o", "o");
            }

            @Override
            public void logout() {

            }

            @Override
            public Intent buildIntent() {
                return null;
            }
        };
        viewModel = new RegistrationViewModel(new CompleteRegistration(fakeProfilesDatabase), notLoggedInAuth);
        fakeProfilesDatabase.saveProfile(new Profile("007", "Donald", "donald@gmail.com", "bioooo", FEMALE, 0, 0));

        CompleteRegistrationForm myFragment = new CompleteRegistrationForm();
        myFragment.viewModelFactory = new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                return (T) viewModel; //mock a fake view model (had to do that because otherwise the fragment will use the real one in onCreate() before I can change it to the mocked one)
            }
        };
        // run myFragment :
        FragmentScenario<CompleteRegistrationForm> scenario = FragmentScenario.launchInContainer(CompleteRegistrationForm.class, null, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                if (className.equals(CompleteRegistrationForm.class.getName())) {
                    return myFragment;
                }
                return super.instantiate(classLoader, className);
            }
        });
        assertThrows(IllegalStateException.class, () -> {
                    onView(withId(R.id.checkBox)).perform(click());
                    try {
                        onView(withId(R.id.finishButton)).perform(click());
                    } catch (PerformException e) {
                        throw e.getCause();
                    }
                }
        );
    }

    @Test
    public void clickConditionsButtonShouldTriggerIntent() {
        Uri uri = Uri.parse("https://firebase.google.com/terms");
        onView(withId(R.id.generalConditions)).perform(click());
        allOf(hasAction(Intent.ACTION_VIEW), hasData(uri));
    }
}