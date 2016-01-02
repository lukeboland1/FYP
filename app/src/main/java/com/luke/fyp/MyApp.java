package com.luke.fyp;

import android.app.Application;

/**
 * Created by Luke on 01/10/2015.
 */
public class MyApp extends Application {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}