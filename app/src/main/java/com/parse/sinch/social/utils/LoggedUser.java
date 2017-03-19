package com.parse.sinch.social.utils;

/**
 * Singleton to Store the logged user and have it accessible in different classes
 */

public class LoggedUser {
    private String mUserLogged;
    private static LoggedUser sLoggedUser;

    public static LoggedUser getInstance() {
        if (sLoggedUser == null) {
            sLoggedUser = new LoggedUser();
        }
        return sLoggedUser;
    }

    private LoggedUser() {

    }
    public String getUserLogged() {
        return mUserLogged;
    }

    public void setUserLogged(String userLogged) {
        this.mUserLogged = userLogged;
    }
}
