package com.sdpteam.connectout;

class AuthenticatedUser {
    String uid;
    String name;
    String email;

    public AuthenticatedUser(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }
}
