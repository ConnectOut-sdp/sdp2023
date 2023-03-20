package com.sdpteam.connectout.userList;

import static com.sdpteam.connectout.userList.OrderingOption.NAME;
import static com.sdpteam.connectout.userList.OrderingOption.NONE;
import static com.sdpteam.connectout.userList.OrderingOption.RATING;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserListViewModelTest {
    private static final Profile PROFILE_TEST1 = new Profile("1", "Eric", "eric@gmail.com", "Hi ! Nice to meet you all :)", Profile.Gender.MALE, 5, 5);
    private static final Profile PROFILE_TEST2 = new Profile("2", "Alberta", "A@gmail.com", "AAAAAA", Profile.Gender.FEMALE, 3, 73);
    private static final Profile PROFILE_TEST3 = new Profile("3", "Alain", "B@gmail.com", "BBBBBBB", Profile.Gender.OTHER, 4.55, 102);
    private static final Profile PROFILE_TEST4 = new Profile("3", "C", "C@gmail.com", "CCCCC", Profile.Gender.OTHER, 4.75, 1000);

    private final static List<Profile> PROFILES = createProfiles();

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

        UserListViewModel ulvm = new UserListViewModel(new TestProfileModel());
        List<Profile> obtained = LiveDataTestUtil.toCompletableFuture(ulvm.getListOfProfile(NONE, null)).join();
        assertThat(obtained, is(expected));
    }

    @Test
    public void orderRatingCorrectlyWithNullArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST2);
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST4);
        expected.add(PROFILE_TEST1);

        UserListViewModel ulvm = new UserListViewModel(new TestProfileModel());
        List<Profile> obtained = LiveDataTestUtil.toCompletableFuture(ulvm.getListOfProfile(RATING, null)).join();
        assertThat(obtained, is(expected));
    }
    @Test
    public void orderNameCorrectlyWithNullArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST2);
        expected.add(PROFILE_TEST4);
        expected.add(PROFILE_TEST1);

        UserListViewModel ulvm = new UserListViewModel(new TestProfileModel());
        List<Profile> obtained = LiveDataTestUtil.toCompletableFuture(ulvm.getListOfProfile(NAME, null)).join();
        assertThat(obtained, is(expected));
    }
    @Test
    public void orderNameCorrectlyWithCorrectArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST2);

        UserListViewModel ulvm = new UserListViewModel(new TestProfileModel());
        List<Profile> obtained = LiveDataTestUtil.toCompletableFuture(ulvm.getListOfProfile(NAME, "Al")).join();
        assertThat(obtained, is(expected));
    }
    @Test
    public void orderRatingCorrectlyWithCorrectArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST4);
        expected.add(PROFILE_TEST1);

        UserListViewModel ulvm = new UserListViewModel(new TestProfileModel());
        List<Profile> obtained = LiveDataTestUtil.toCompletableFuture(ulvm.getListOfProfile(RATING, "4;5")).join();
        assertThat(obtained, is(expected));
    }
    @Test
    public void orderRatingCorrectlyWithWrongArguments() {
        List<Profile> expected = new ArrayList<>();
        expected.add(PROFILE_TEST2);
        expected.add(PROFILE_TEST3);
        expected.add(PROFILE_TEST4);
        expected.add(PROFILE_TEST1);

        UserListViewModel ulvm = new UserListViewModel(new TestProfileModel());
        List<Profile> obtained = LiveDataTestUtil.toCompletableFuture(ulvm.getListOfProfile(RATING, "4;5WrongInput Wrong Input :((((")).join();
        assertThat(obtained, is(expected));

        List<Profile> obtained2 = LiveDataTestUtil.toCompletableFuture(ulvm.getListOfProfile(RATING, "WrongInput Wrong Input :((((")).join();
        assertThat(obtained2, is(expected));
    }
    @Test
    public void orderNameGivesNullListWithWrongArguments() {
        List<Profile> expected = new ArrayList<>();

        UserListViewModel ulvm = new UserListViewModel(new TestProfileModel());
        List<Profile> obtained = LiveDataTestUtil.toCompletableFuture(ulvm.getListOfProfile(NAME, "4;5WrongInput Wrong Input :((((")).join();
        assertThat(obtained, is(expected));
    }

    public static class TestProfileModel implements ProfileListDataManager {

        @Override
        public LiveData<List<Profile>> getListOfProfile(OrderingOption option, List<String> values) {
            if (option == NONE) {
                return new MutableLiveData<>(PROFILES);
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
                return new MutableLiveData<>(pl);
            } else if (values.size() == 1 && option == NAME) {
                return new MutableLiveData<>(pl.stream()
                        .filter(profile ->
                                profile.getName()
                                        .startsWith(values.get(0)))
                        .collect(Collectors.toList()));
            }
            if (values.size() == 2 && option == RATING) {
                pl = pl
                        .stream()
                        .filter(p -> p.getRating() >= Double.parseDouble(values.get(0)) && p.getRating() <=Double.parseDouble(values.get(1)))
                        .collect(Collectors.toList());
                return new MutableLiveData<>(pl);
            }

            return new MutableLiveData<>(pl);
        }
    }
}
