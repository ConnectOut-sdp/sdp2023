package com.sdpteam.connectout.registration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.sdpteam.connectout.profile.Profile.Gender.FEMALE;
import static com.sdpteam.connectout.profile.Profile.Gender.MALE;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.Is.is;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDataSource;
import com.sdpteam.connectout.profileList.filter.ProfileFilter;

import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(AndroidJUnit4.class)
public class CompleteRegistrationFormTest {

    private static Profile databaseContent;
    public static final ProfileDataSource fakeProfilesDatabase = new ProfileDataSource() {
        @Override
        public CompletableFuture<Boolean> saveProfile(Profile profile) {
            CompletableFuture<Boolean> done = new CompletableFuture<>();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                databaseContent = profile;
                done.complete(true);
            });
            return done;
        }

        @Override
        public CompletableFuture<Profile> fetchProfile(String uid) {
            return CompletableFuture.completedFuture(databaseContent);
        }

        @Override
        public CompletableFuture<List<Profile>> getProfilesByFilter(ProfileFilter filter) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    };
    @Rule
    public ActivityTestRule<CompleteRegistrationActivity> activityTestRule = new ActivityTestRule<>(CompleteRegistrationActivity.class, true, true);
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private RegistrationViewModel viewModel;

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
        fJoin(fakeProfilesDatabase.saveProfile(new Profile("007", "Donald", "donald@gmail.com", "bioooo", FEMALE, 0, 0, "")));

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
        Profile updatedProfile = fJoin(fakeProfilesDatabase.fetchProfile("007"));

        MatcherAssert.assertThat(updatedProfile.getId(), is("007"));
        MatcherAssert.assertThat(updatedProfile.getName(), is("Donald Trump"));
        MatcherAssert.assertThat(updatedProfile.getEmail(), is("donald@gmail.com"));
        MatcherAssert.assertThat(updatedProfile.getBio(), is("My awesome bio"));
        MatcherAssert.assertThat(updatedProfile.getGender(), is(MALE));
    }

    @Test
    public void notLoggedShouldThrowErrorMessage() {

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
        fJoin(fakeProfilesDatabase.saveProfile(new Profile("007", "Donald", "donald@gmail.com", "bioooo", FEMALE, 0, 0, "")));

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
        onView(withId(R.id.checkBox)).perform(click());
        onView(withId(R.id.finishButton)).perform(click());
        onView((withId(R.id.complete_registration_error_msg))).check(matches(withText("Cannot complete the registration you're not even logged in.")));
    }

    @Test
    public void clickConditionsButtonShouldTriggerIntent() {
        Uri uri = Uri.parse("https://firebase.google.com/terms");
        onView(withId(R.id.generalConditions)).perform(click());
        allOf(hasAction(Intent.ACTION_VIEW), hasData(uri));
    }

    /**
     * it uses a mocked firebase users, but the real upload images firebase.
     */
    @Test
    public void completeRegistrationUploadImage() {

        Authentication notLoggedInAuth = new Authentication() {
            @Override
            public boolean isLoggedIn() {
                return true;
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
        fJoin(fakeProfilesDatabase.saveProfile(new Profile("007", "Donald", "donald@gmail.com", "bioooo", FEMALE, 0, 0, "")));

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
        onView(withId(R.id.checkBox)).perform(click());

        // Mock the Uri result for the image picker intent using the account_image drawable resource
        Context context = getInstrumentation().getTargetContext();
        Uri mockedUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(R.drawable.account_image) + '/' +
                context.getResources().getResourceTypeName(R.drawable.account_image) + '/' +
                context.getResources().getResourceEntryName(R.drawable.account_image));

        getInstrumentation().runOnMainSync(() -> {
            viewModel.completeRegistration("Donald", "email@test.com", "bio2", MALE, mockedUri);
        });
        waitABit();
        waitABit();
        waitABit();

        onView((withId(R.id.complete_registration_error_msg))).check(matches(withText("Operation successful")));
        waitABit();
        waitABit();
        waitABit();
        Profile updatedProfile = fJoin(fakeProfilesDatabase.fetchProfile("007"));
        MatcherAssert.assertThat(updatedProfile.getName(), is("Donald"));
        MatcherAssert.assertThat(updatedProfile.getEmail(), is("email@test.com"));
        MatcherAssert.assertThat(updatedProfile.getBio(), is("bio2"));
        MatcherAssert.assertThat(updatedProfile.getProfileImageUrl().contains("https://"), is(true));
    }
}