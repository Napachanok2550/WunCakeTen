package com.project;

public class Session {
    private static String currentUser;

    public static void setUser(String username) {
        currentUser = username;
    }

    public static String getUser() {
        return currentUser;
    }
}
