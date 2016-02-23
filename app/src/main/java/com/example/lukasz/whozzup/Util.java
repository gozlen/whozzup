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


    public User readJsonStream(InputStream in) throws IOException {
        System.out.println("READING USER STREAM");
        User user;

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        reader.beginArray();
        user = readUserInfo(reader);
        reader.endArray();

        System.out.println("successfully parsed user data");
        return user;

    }

    public User readUserInfo(JsonReader reader) throws IOException{
        User user = new User();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("tags") && reader.peek() != JsonToken.NULL) {
                System.out.println("got tags");
                user.likes = readTagsArray(reader);
            } else if (name.equals("description")) {
                System.out.println("got description");
                user.description = reader.nextString();
            } else if (name.equals("userID")) {
                System.out.println("got user id");
                user.id = reader.nextString();
            } else if (name.equals("friends")) {
                System.out.println("got friends");
                user.friends = readFriendsArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return user;
    }

    public List<Like> readTagsArray(JsonReader reader) throws IOException{
        System.out.println("READING TAGS");
        List<Like> likes = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            reader.beginObject();
            while (reader.hasNext()){
                String title = reader.nextName();
                String name = "";
                String id = "";
                if (title.equals("name")){
                    name = reader.nextString();
//                    System.out.println(name);
                } else if (title.equals("id")){
                    id = reader.nextString();
//                    System.out.println(id);
                } else{
                    reader.skipValue();
                }
                likes.add(new Like(name,id));
            }
            reader.endObject();

        }
        reader.endArray();
        return likes;
    }

    public List<Friend> readFriendsArray(JsonReader reader) throws IOException{
        System.out.println("READING TAGS");
        List<Friend> likes = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            reader.beginObject();
            while (reader.hasNext()){
                String title = reader.nextName();
                String name = "";
                String id = "";
                if (title.equals("name")){
                    name = reader.nextString();
//                    System.out.println(name);
                } else if (title.equals("id")){
                    id = reader.nextString();
//                    System.out.println(id);
                } else{
                    reader.skipValue();
                }
                likes.add(new Friend(name,id));
            }
            reader.endObject();

        }
        reader.endArray();
        return likes;
    }

}