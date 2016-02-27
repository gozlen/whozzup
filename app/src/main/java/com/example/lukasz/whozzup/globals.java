package com.example.lukasz.whozzup;

import android.app.Application;

import java.util.List;

/**
 * Created by Lukasz on 2/24/2016.
 */
public class globals extends Application {

    private User user;

    public void setEventList(List<Event> events){
        user.events = events;
    }

    public User getGlobalVarValue() {
        return user;
    }

    public void setGlobalVarValue(User user) {
        this.user = user;
    }
}