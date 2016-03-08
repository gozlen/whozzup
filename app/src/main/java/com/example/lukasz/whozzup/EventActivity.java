package com.example.lukasz.whozzup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {
    TextView title;
    TextView date;
    TextView location;
    TextView description;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");
        //System.out.println(id);
        new getEvent().execute("https://protected-ocean-61024.herokuapp.com/event/", id);
    }


    private class getEvent extends AsyncTask<String, Void, List<Event>> {
        protected List<Event> doInBackground(String... str){
            InputStream in = null;
            try {
                DataOutputStream printout;
                URL url = new URL(str[0]);
                String id = str[1];
                System.out.println(id);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");


                JSONObject info = new JSONObject();
                info.put("id", id);



                printout = new DataOutputStream(con.getOutputStream ());
                String data = info.toString();
                byte[] send = data.getBytes("UTF-8");
                printout.write(send);
                printout.flush();
                printout.close();
                Util util = new Util();
                in = con.getInputStream();

                List<Event> eventList = util.readEventArray(in);

                return eventList;


            } catch (Exception e) {
                return new ArrayList<>();

            }
        }

        protected void onPostExecute(List<Event> eventList){
            setContentView(R.layout.activity_event);

            Event e = eventList.get(0);

            title = (TextView) findViewById(R.id.event_info_title);
            date = (TextView) findViewById(R.id.event_info_date);
            location = (TextView) findViewById(R.id.event_info_location);
            description = (TextView) findViewById(R.id.event_info_description);
            img = (ImageView) findViewById(R.id.event_info_image);

            title.setText(e.getTitle());
            date.setText(e.getFullDate());
            location.setText(e.getLocation());
            description.setText(e.getDescription());
            img.setImageResource(getImage(e.getCategory()));

        }
    }

    private class attendEvent extends AsyncTask<String, Void, List<Event>> {
        protected List<Event> doInBackground(String... str){
            InputStream in = null;
            try {
                DataOutputStream printout;
                URL url = new URL(str[0]);
                String id = str[1];
                System.out.println(id);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                String userID = accessToken.getUserId();

                JSONObject info = new JSONObject();
                info.put("id", id);
                info.put("userID", userID);


                printout = new DataOutputStream(con.getOutputStream ());
                String data = info.toString();
                byte[] send = data.getBytes("UTF-8");
                printout.write(send);
                printout.flush();
                printout.close();
                Util util = new Util();
                in = con.getInputStream();

                List<Event> eventList = util.readEventArray(in);

                return eventList;


            } catch (Exception e) {
                return new ArrayList<>();

            }
        }

        protected void onPostExecute(List<Event> eventList){
        }
    }

    private int getImage(String category){
        if (category.equals("Food"))
            return R.drawable.food;
        else if (category.equals("Hiking"))
            return R.drawable.hiking;
        else if (category.equals("Sports"))
            return R.drawable.sports;
        else if (category.equals("Movies"))
            return R.drawable.cinema;
        else if (category.equals("Drinks"))
            return R.drawable.drinks;
        return R.drawable.food;
    }
}
