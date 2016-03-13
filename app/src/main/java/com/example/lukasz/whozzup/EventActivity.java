package com.example.lukasz.whozzup;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {
    ImageHelper hlp = new ImageHelper();
    TextView title;
    TextView date;
    List<Double> coords = new ArrayList<>();
    GoogleMap mMap;
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng loc = new LatLng(coords.get(0), coords.get(1));
        System.out.println(coords.get(0) + "," +  coords.get(1));
        mMap.addMarker(new MarkerOptions().position(loc).title("Your Event"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

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
            setContentView(R.layout.activity_event);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.event_marker);
            mapFragment.getMapAsync(EventActivity.this);
            dialog.hide();

            final Button b = (Button) findViewById(R.id.join);

            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (b.getText().equals("I'm up!")){
                        System.out.println("Joining");
                        new attendEvent().execute("https://protected-ocean-61024.herokuapp.com/event/join/", id);

                    }else {
                        System.out.println("Leaving");

                        new attendEvent().execute("https://protected-ocean-61024.herokuapp.com/event/leave/", id);
                    }

                    dialog.setCancelable(false);
                    dialog.setMessage("Submitting request");
                    dialog.setInverseBackgroundForced(false);
                    dialog.show();

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
            img = (ImageView) findViewById(R.id.event_info_image);

            title.setText(e.getTitle());
            date.setText(e.getFullDate());

            String category = e.getCategory();
            Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), getImage(category));
            largeIcon = hlp.getRoundedCornerBitmap(largeIcon, 500);
            img.setImageBitmap(largeIcon);

            String str = e.getLocation();
            List<String> strList = Arrays.asList(str.split(","));
            coords.add(0, Double.parseDouble(strList.get(1)));
            coords.add(1, Double.parseDouble(strList.get(0)));



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
            dialog.hide();
            final Button b = (Button) findViewById(R.id.join);

            if (b.getText().equals("I'm up!")){
                b.setText("Leave event");
                Toast.makeText(getApplicationContext(), "Joined event", Toast.LENGTH_SHORT).show();
            } else {
                b.setText("I'm up!");
                Toast.makeText(getApplicationContext(), "Left event", Toast.LENGTH_SHORT).show();
            }
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