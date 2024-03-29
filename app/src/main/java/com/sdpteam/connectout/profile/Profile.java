package com.sdpteam.connectout.profile;

public class Profile {

    public final static Profile NULL_PROFILE = new Profile();
    public static final String NULL_USER = "null_user";
    private final String name;
    private final String email;
    private final String bio;
    private final Gender gender;
    private final double rating;
    private final int numRatings;
    private final String id;
    private final String profileImageUrl;
    private final String nameLowercase; // for index in firebase

    private Profile() {
        this(NULL_USER, null, null, null, null, 0, 0, null);
    }

    public Profile(String id, String name, String email, String bio, Gender gender, double rating, int numRatings, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.gender = gender;
        this.rating = rating;
        this.numRatings = numRatings;
        this.profileImageUrl = profileImageUrl;
        if (name == null) {
            this.nameLowercase = null;
        } else {
            this.nameLowercase = name.toLowerCase();
        }
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
        return id;
    }

    public double getRating() {
        return rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getNameLowercase() {
        return nameLowercase;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
