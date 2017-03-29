package com.social.backendless.utils;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

/**
 * Singleton to Store the logged user and have it accessible in different classes
 */

public class LoggedUser {
    private BackendlessUser mUserLogged;
    private String mUserLoggedId;
    private static LoggedUser sLoggedUser;

    public static LoggedUser getInstance() {
        if (sLoggedUser == null) {
            sLoggedUser = new LoggedUser();
        }
        return sLoggedUser;
    }

    private LoggedUser() {
        setUserLoggedId(Backendless.UserService.loggedInUser());
    }
    public String getUserIdLogged() {
        return mUserLoggedId;
    }

    public BackendlessUser getUserLogged() { return mUserLogged; }
    public void setUserLogged(BackendlessUser userLogged) {
        this.mUserLogged = userLogged;
    }

    public void setUserLoggedId(String userLoggedId) {
        this.mUserLoggedId = userLoggedId;
    }
}
