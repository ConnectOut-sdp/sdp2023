package com.sdpteam.connectout.profile;

public class Profile {
    public enum Gender {
        FEMALE, MALE, OTHER
    }

    private String name;
    private String email;
    private String bio;
    private Gender gender;

    private double rating;

    private int numRatings;

    private final String uid;

    public Profile(){
        this(EditProfileActivity.NULL_USER, null, null, null, null, 0, 0);
    }
    public Profile(String uid, String name, String email, String bio, Gender gender, double rating, int numRatings){
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

    public String getName(){
        return name;
    }

    public String getId() {
        return uid;
    }

    public double getRating() {
        return rating;
    }

    public int getNumRatings(){
        return numRatings;
    }

    public void setGender(Gender g){
        gender = g;
    }

    public void setBio(String b){
        bio = b;
    }
    public void setEmail(String e){
        email = e;
    }
    public void setName(String n){
        name = n;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
