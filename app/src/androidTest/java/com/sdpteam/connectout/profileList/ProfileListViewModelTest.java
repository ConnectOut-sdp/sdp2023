package com.sdpteam.connectout.profileList;

import static com.sdpteam.connectout.profile.ProfileFirebaseDataSource.ProfileOrderingOption.NAME;
import static com.sdpteam.connectout.profile.ProfileFirebaseDataSource.ProfileOrderingOption.NONE;
import static com.sdpteam.connectout.profile.ProfileFirebaseDataSource.ProfileOrderingOption.RATING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.ProfileDataSource;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

public class ProfileListViewModelTest {

    private static final Profile PROFILE_TEST1 = new Profile("1", "Eric", "eric@gmail.com", "Hi ! Nice to meet you all :)", Profile.Gender.MALE, 5, 5, "");
    private static final Profile PROFILE_TEST2 = new Profile("2", "Alberta", "A@gmail.com", "AAAAAA", Profile.Gender.FEMALE, 3, 73, "");
    private static final Profile PROFILE_TEST3 = new Profile("3", "Alain", "B@gmail.com", "BBBBBBB", Profile.Gender.OTHER, 4.55, 102, "");
    private static final Profile PROFILE_TEST4 = new Profile("3", "C", "C@gmail.com", "CCCCC", Profile.Gender.OTHER, 4.75, 1000, "");
    private final static List<Profile> PROFILES = createProfiles();
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static List<Profile> createProfiles() {
        List<Profile> pl = new ArrayList<>();
        pl.add(PROFILE_TEST1);
        pl.add(PROFILE_TEST2);
        pl.add(PROFILE_TEST3);
        pl.add(PROFILE_TEST4);
        return pl;
    }

    @Test
    public void orderNoneCorrectlyWithNullArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST1);
        expected.add(PROFILE_TEST2);
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST4);

        ProfileListViewModel ulvm = new ProfileListViewModel(new TestProfileModel());
        ulvm.getListOfProfile(NONE, null);
        List<Profile> obtained = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained, is(expected));
    }

    @Test
    public void orderRatingCorrectlyWithNullArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST2);
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST4);
        expected.add(PROFILE_TEST1);

        ProfileListViewModel ulvm = new ProfileListViewModel(new TestProfileModel());
        ulvm.getListOfProfile(RATING, null);
        List<Profile> obtained = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained, is(expected));
    }

    @Test
    public void orderNameCorrectlyWithNullArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST2);
        expected.add(PROFILE_TEST4);
        expected.add(PROFILE_TEST1);

        ProfileListViewModel ulvm = new ProfileListViewModel(new TestProfileModel());
        ulvm.getListOfProfile(NAME, null);
        List<Profile> obtained = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained, is(expected));
    }

    @Test
    public void orderNameCorrectlyWithCorrectArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST2);

        ProfileListViewModel ulvm = new ProfileListViewModel(new TestProfileModel());
        ulvm.getListOfProfile(NAME, "Al");
        List<Profile> obtained = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained, is(expected));
    }

    @Test
    public void orderRatingCorrectlyWithRangeArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST4);
        expected.add(PROFILE_TEST1);

        ProfileListViewModel ulvm = new ProfileListViewModel(new TestProfileModel());
        ulvm.getListOfProfile(RATING, "4;5");
        List<Profile> obtained = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained, is(expected));
    }

    @Test
    public void orderRatingCorrectlyWitValueArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST2);

        ProfileListViewModel ulvm = new ProfileListViewModel(new TestProfileModel());
        ulvm.getListOfProfile(RATING, "3");
        List<Profile> obtained = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained, is(expected));
    }

    @Test
    public void orderRatingCorrectlyWithWrongArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST2);
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST4);
        expected.add(PROFILE_TEST1);

        ProfileListViewModel ulvm = new ProfileListViewModel(new TestProfileModel());
        ulvm.getListOfProfile(RATING, "4;5WrongInput Wrong Input :((((");
        List<Profile> obtained = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained, is(expected));

        ulvm.getListOfProfile(RATING, "4;5WrongInput Wrong Input :((((");
        List<Profile> obtained2 = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained2, is(expected));
    }

    @Test
    public void orderNameGivesNullListWithWrongArguments() {
        List<Profile> expected = new ArrayList<>();

        ProfileListViewModel ulvm = new ProfileListViewModel(new TestProfileModel());
        ulvm.getListOfProfile(NAME, "4;5WrongInput Wrong Input :((((");
        List<Profile> obtained = LiveDataTestUtil.getOrAwaitValue(ulvm.getUserListLiveData());
        assertThat(obtained, is(expected));
    }

    public static class TestProfileModel implements ProfileDataSource {

        @Override
        public CompletableFuture<Boolean> saveProfile(Profile profile) {
            return CompletableFuture.completedFuture(true);
        }

        @Override
        public CompletableFuture<Profile> fetchProfile(String uid) {
            return null;
        }

        @Override
        public CompletableFuture<List<Profile>> getListOfProfile(ProfileFirebaseDataSource.ProfileOrderingOption option, List<String> values) {
            if (option == NONE) {
                return CompletableFuture.completedFuture(PROFILES);
            }
            List<Profile> pl = new ArrayList<>(PROFILES);

            switch (option) {
                case NAME:
                    pl.sort(Comparator.comparing(Profile::getName));
                    break;
                case RATING:
                    pl.sort(Comparator.comparing(Profile::getRating));
            }

            if (values == null) {
                return CompletableFuture.completedFuture(pl);
            } else if (values.size() == 1 && option == NAME) {
                return CompletableFuture.completedFuture(pl.stream()
                        .filter(profile ->
                                profile.getName()
                                        .startsWith(values.get(0)))
                        .collect(Collectors.toList()));
            }
            if (values.size() == 2 && option == RATING) {
                pl = pl
                        .stream()
                        .filter(p -> p.getRating() >= Double.parseDouble(values.get(0)) && p.getRating() <= Double.parseDouble(values.get(1)))
                        .collect(Collectors.toList());
                return CompletableFuture.completedFuture(pl);
            }
            if (values.size() == 1 && option == RATING) {
                pl = pl
                        .stream()
                        .filter(p -> p.getRating() == Double.parseDouble(values.get(0)))
                        .collect(Collectors.toList());
                return CompletableFuture.completedFuture(pl);
            }

            return CompletableFuture.completedFuture(pl);
        }
    }
}
