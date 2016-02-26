package com.example.lukasz.whozzup;

/**
 * Created by Lukasz on 2/24/2016.
 */
public class Event {
    String creator;
    String category;
    String title;
    String description;
    String location;
    String date;
    String time;

    public Event (String creator, String category, String title, String description, String location, String date, String time){
        this.creator = creator;
        this.category = category;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
    }
}
