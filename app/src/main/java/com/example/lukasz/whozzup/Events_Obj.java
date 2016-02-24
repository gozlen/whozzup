package com.example.lukasz.whozzup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Kashal Silva on 2/24/2016.
 */
public class Events_Obj {
    String event_category;
    String event_title;
    String event_description;
    String event_location;
    String event_date;
    String event_time;
    String event_restrictions;
   List<String> event_attendees;



    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Category", event_category);
            obj.put("Description", event_description);
            obj.put("Location", event_location);
            obj.put("Date", event_date);
            obj.put("Time", event_time);
            obj.put("Title", event_title);
            obj.put("Attendees", event_attendees);
            //obj.put("Restrictions", event_restrictions);
        } catch (JSONException e) {
            System.out.println("DefaultListItem.toString JSONException: "+e.getMessage());
        }
        return obj;
    }
}