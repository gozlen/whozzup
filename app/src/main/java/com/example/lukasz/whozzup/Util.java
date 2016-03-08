package com.example.lukasz.whozzup;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.example.lukasz.whozzup.Like;
import com.example.lukasz.whozzup.Friend;

/**
 * Created by Lukasz on 2/9/2016.
 */
public class Util {

    private static final String TAG = Util.class.getSimpleName();
    public static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public List<Events_Obj> readEventsJsonStream(InputStream in) throws IOException {

        List<Events_Obj> eventsList = new ArrayList<>();
        Events_Obj event = new Events_Obj();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

       reader.beginArray();
        //System.out.println("Marker for beginArray");
        while(reader.hasNext()) {
            event = readEventInfo(reader);
            eventsList.add(event);
        }
        reader.endArray();


        return eventsList;

    }


    public Events_Obj readEventInfo(JsonReader reader) throws IOException{


        Events_Obj event = new Events_Obj();
        //System.out.println("BEGIN OBJECT");


        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();

            if (name.equals("description") && reader.peek() != JsonToken.NULL) {
                //System.out.println("got description");
                event.event_description = reader.nextString();

            } else if (name.equals("date")) {
                //System.out.println("got date");
                event.event_date = reader.nextString();
            } else if (name.equals("time")) {
                //System.out.println("got time");
                event.event_time = reader.nextString();
            } else if (name.equals("location")) {
               // System.out.println("got location");
                event.event_location = reader.nextString();
            } else if (name.equals("title")) {
               // System.out.println("got title");
                event.event_title = reader.nextString();
            } else if (name.equals("restrictions")) {
                //System.out.println("got restrictions");
                event.event_restrictions = reader.nextString();

            } else if (name.equals("category")) {
                //System.out.println("got category");
                event.event_category = reader.nextString();

            } else if (name.equals("attendees")) {
                //System.out.println("got attendess");
                List<String> att= new ArrayList<String>();
                    reader.beginArray();
                        while (reader.hasNext()) {
                        //System.out.println("Att Sting: " + reader.nextString());
                            att.add(reader.nextString());
                        }
                    reader.endArray();
                    event.event_attendees = att;

            } else {
                reader.skipValue();
            }



        }//end of while(reader.hasNext())
        reader.endObject();

        return event;
    }




    public User readJsonStream(InputStream in) throws IOException {

        User user;

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        reader.beginArray();
        user = readUserInfo(reader);
        reader.endArray();

        //System.out.println("successfully parsed user data");
        return user;

    }

    public User readUserInfo(JsonReader reader) throws IOException{
        User user = new User();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("tags") && reader.peek() != JsonToken.NULL) {
                user.likes = readTagsArray(reader);
            } else if (name.equals("description")) {
                user.description = reader.nextString();
            } else if (name.equals("userID")) {
                user.id = reader.nextString();
            } else if (name.equals("friends")) {
                user.friends = readFriendsArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();


        return user;
    }

    public List<Like> readTagsArray(JsonReader reader) throws IOException{
        List<Like> likes = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            reader.beginObject();

            String name = "";
            String id = "";
            while (reader.hasNext()){
                String title = reader.nextName();
                if (title.equals("name")){
                    name = reader.nextString();
                } else if (title.equals("id")){
                    id = reader.nextString();
                } else{
                    reader.skipValue();
                }
            }

            likes.add(new Like(name,id));
            reader.endObject();

        }
        reader.endArray();
        return likes;
    }

    public List<Event> readEventArray(InputStream in) throws IOException{
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Event> events = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            reader.beginObject();

            String creator = "";
            String category = "";
            String title = "";
            String description = "";
            String location= "";
            String date= "";
            String time= "";
            String id = "";
            List<String> attendees = new ArrayList<>();


            while (reader.hasNext()){
                String tag = reader.nextName();
                if (tag.equals("title")){
                    title = reader.nextString();
                    //System.out.println(title);
                } else if (tag.equals("creator")){
                    creator = reader.nextString();
                } else if (tag.equals("category")){
                    category = reader.nextString();
                } else if (tag.equals("title")){
                    title = reader.nextString();
                } else if (tag.equals("description")){
                    description = reader.nextString();
                } else if (tag.equals("location")){
                    location = reader.nextString();
                } else if (tag.equals("date")){
                    date = reader.nextString();
                } else if (tag.equals("time")){
                    time = reader.nextString();
                } else if (tag.equals("attendees")){
                    attendees = readAttendeesArray(reader);
                } else if (tag.equals("_id")){
                    reader.beginObject();
                    if (reader.nextName().equals("$oid")){
                        id = reader.nextString();
                    }
                    reader.endObject();
                } else {
                    reader.skipValue();
                }
            }
            //System.out.println(id);
            events.add(new Event(creator,category,title,description,location,date,time,attendees, id));
            reader.endObject();

        }
        reader.endArray();
        return events;
    }

    public List<String> readAttendeesArray(JsonReader reader) throws  IOException{
        List<String> attendees = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            attendees.add(reader.nextString());
        }
        reader.endArray();
        return attendees;
    }



    public List<Friend> readFriendsArray(JsonReader reader) throws IOException{
       // System.out.println("reading friends array!!!!!!");
        List<Friend> likes = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            reader.beginObject();

            String name = "";
            String id = "";
            while (reader.hasNext()){
                String title = reader.nextName();
                if (title.equals("name")){
                    name = reader.nextString();

                } else if (title.equals("id")){
                    id = reader.nextString();

                } else{
                    reader.skipValue();
                }
            }
            likes.add(new Friend(name, id));
            reader.endObject();

        }
        reader.endArray();
        return likes;
    }

}