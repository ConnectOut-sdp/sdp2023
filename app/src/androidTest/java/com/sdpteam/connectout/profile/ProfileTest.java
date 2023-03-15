package com.sdpteam.connectout.profile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.sdpteam.connectout.profile.Profile;

import org.junit.Test;

public class ProfileTest {

    @Test
    public void gettersAndSettersTest() {
        Profile p = new Profile("12342", "Donald Trump", "donaldtrump@gmail.com", "I'm so cool", Profile.Gender.OTHER, 1, 1);

        assertThat(p.getBio(), is("I'm so cool"));
        assertThat(p.getName(), is("Donald Trump"));
        assertThat(p.getEmail(), is("donaldtrump@gmail.com"));
        assertThat(p.getGender(), is(Profile.Gender.OTHER));
        assertThat(p.getId(), is("12342"));

        p = new Profile("12342", "ExPresident", "expresident@gmail.com", "I'm not cool", Profile.Gender.MALE, 1, 1);

        assertThat(p.getBio(), is("I'm not cool"));
        assertThat(p.getName(), is("ExPresident"));
        assertThat(p.getEmail(), is("expresident@gmail.com"));
        assertThat(p.getGender(), is(Profile.Gender.MALE));
        assertThat(p.getId(), is("12342"));
    }
}
