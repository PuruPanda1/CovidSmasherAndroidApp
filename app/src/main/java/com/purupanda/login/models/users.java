package com.purupanda.login.models;

public class users {
    String name;
    String email;
    String password;
    String profilePicture;
    String userId;

    public users(String name, String email, String password, String profilePicture, String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.userId = userId;
    }

    public users(){}

    // sign up constructor
    public users(String name, String email, String password)
    {
        this.name = name;
        this.email = email;
        this.password = password;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
