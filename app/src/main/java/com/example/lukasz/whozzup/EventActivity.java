package com.example.lukasz.whozzup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    String userID = accessToken.getUserId();
    String id;
    ProgressDialog dialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        //System.out.println(id);
        new getEvent().execute("https://protected-ocean-61024.herokuapp.com/event/", id);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Event");
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Event Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lukasz.whozzup/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Event Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lukasz.whozzup/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private class getEvent extends AsyncTask<String, Void, List<Event>> {
        protected List<Event> doInBackground(String... str) {
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


                printout = new DataOutputStream(con.getOutputStream());
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

        protected void onPostExecute(List<Event> eventList) {
            dialog.hide();
            setContentView(R.layout.activity_event);

            final Button b = (Button) findViewById(R.id.join);
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new attendEvent().execute("https://protected-ocean-61024.herokuapp.com/event/join/", id);
                }
            });


            for (Event event : eventList) {
                if (event.attendees.contains(userID)) {
                    b.setText("Leave event");
                }
            }

            final Event e = eventList.get(0);

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

    private class attendEvent extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... str) {
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


                userID = accessToken.getUserId();
                JSONObject info = new JSONObject();
                info.put("id", id);
                info.put("userID", userID);


                printout = new DataOutputStream(con.getOutputStream());
                String data = info.toString();
                byte[] send = data.getBytes("UTF-8");
                printout.write(send);
                printout.flush();
                printout.close();
                Util util = new Util();
                in = con.getInputStream();

                String res = util.readIt(in, 500);
                System.out.println(res);
                return res;


            } catch (Exception e) {
                return "kkkk";

            }
        }

        protected void onPostExecute(String res) {
            Toast.makeText(getApplicationContext(), "Joined event", Toast.LENGTH_SHORT).show();
        }
    }

    private int getImage(String category) {
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
