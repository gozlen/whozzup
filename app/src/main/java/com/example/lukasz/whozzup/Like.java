package com.example.lukasz.whozzup;

/**
 * Created by Lukasz on 2/23/2016.
 */
public class Like {
    private String name;
    private String id;

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public Like(String name, String id){
        this.name = name;
        this.id = id;
    }
@Override
    public String toString(){
        return id+" "+name;
    }
}