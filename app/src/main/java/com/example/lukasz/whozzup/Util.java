package com.example.lukasz.whozzup;

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


    public List<Message> readJsonStream(InputStream in) throws IOException {
        System.out.println("READING STREAM");
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<String> tags = new ArrayList<>();
        reader.beginArray();
        while(reader.hasNext()){
            reader.beginObject();
            String name = reader.nextName();
            if (name.equals("tags") && reader.peek() != JsonToken.NULL) {
                tags = readTagsArray(reader);
            } else if (name.equals("description")) {
                System.out.println("got description");
            } else if (name.equals("userID")) {
                System.out.println("got id");
            } else if (name.equals("friends")) {
                System.out.println("got friends");
            } else {
                reader.skipValue();
            }
        }

        List<Message> msg = new ArrayList<Message>();
        return msg;

    }

    public List<String> readTagsArray(JsonReader reader) throws IOException{
        List<String> tags = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            reader.beginObject();
            while (reader.hasNext()){
                String name = reader.nextName();
                if (name.equals("name")){
                    String tag = reader.nextString();
                    System.out.println(tag);
                    tags.add(tag);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        }
        reader.endArray();
        return tags;
    }

}