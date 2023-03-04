package com.sdpteam.connectout.profile;

public class Profile {
    public enum Gender {
        FEMALE, MALE, OTHER
    }

    private String name;
    private String email;
    private String bio;
    private Gender gender;

    private final int id; //value of -1 corresponds to an error
    public Profile(int id, String name, String email, String bio, Gender gender){
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.gender = gender;
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

    public int getId() {
        return id;
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
}
