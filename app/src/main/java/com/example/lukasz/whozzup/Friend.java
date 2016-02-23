package com.example.lukasz.whozzup;

/**
 * Created by Lukasz on 2/23/2016.
 */
public class Friend{
    private String name;
    private String id;

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public Friend (String name, String id){
        this.name = name;
        this.id = id;
    }
}
