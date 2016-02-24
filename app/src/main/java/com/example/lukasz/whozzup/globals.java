package com.example.lukasz.whozzup;

import android.app.Application;

/**
 * Created by Lukasz on 2/24/2016.
 */
public class globals extends Application {

    private User user;

    public User getGlobalVarValue() {
        return user;
    }

    public void setGlobalVarValue(User user) {
        this.user = user;
    }
}