package com.example.myapplication.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private final String userToken;
    private final String displayName;

    public LoggedInUser(String userToken, String displayName) {
        this.userToken = userToken;
        this.displayName = displayName;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getDisplayName() {
        return displayName;
    }
}