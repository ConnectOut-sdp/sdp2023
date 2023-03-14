package com.sdpteam.connectout.profile;

public class Profile {
    public enum Gender {
        FEMALE, MALE, OTHER
    }

    private final String name;
    private final String email;
    private final String bio;
    private final Gender gender;

    private final double rating;

    private final int numRatings;

    private final String uid;

    public Profile() {
        this(EditProfileActivity.NULL_USER, null, null, null, null, 0, 0);
    }

    public Profile(String uid, String name, String email, String bio, Gender gender, double rating, int numRatings) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.gender = gender;
        this.rating = rating;
        this.numRatings = numRatings;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return uid;
    }

    public double getRating() {
        return rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

}
