package com.sdpteam.connectout.authentication;

public class AuthenticatedUser {
    public final String uid;
    public final String name;
    public final String email;

    public AuthenticatedUser(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }
}
