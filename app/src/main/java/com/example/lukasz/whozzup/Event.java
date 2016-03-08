package com.example.lukasz.whozzup;

import java.util.List;

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
    String id;
    List<String> attendees;

    public Event (String creator, String category, String title, String description, String location, String date, String time, List<String> attendees, String id){
        this.creator = creator;
        this.category = category;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.attendees = attendees;
        this.id = id;
    }

    public String getTitle(){
        return title;
    }
    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }
    public String getFullDate(){
        return date + " " + time;
    }
    public String getCategory() {return category;}
    public String getId() {return id;}
    public String getLocation(){return location;}
    public String getDescription() {return description;}
}
