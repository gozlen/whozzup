package com.example.lukasz.whozzup;

import java.util.List;

/**
 * Created by Lukasz on 2/23/2016.
 */
public class User {
    List<Like> likes;
    List<Friend> friends;
    String id;
    String name;
    String description;


    public String toStringCustom() {

        return "ID "+id
                +"Name: "+name
                +" Description "+ description
                +" Likes "+likes.toString()
                +" Friends "+friends.toString();
    }
}
