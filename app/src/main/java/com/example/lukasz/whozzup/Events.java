package com.example.lukasz.whozzup;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class Events extends ListFragment{

    AsyncTask mTask;
    List<Event> eventlist;
    LocationManager locationManager;
    Location location;
    LocationListener locationListener;

    String TAG = "Here";
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                location.setLatitude(loc.getLatitude());
                location.setLongitude(loc.getLongitude());
            }

            @Override
            public void onProviderDisabled(String provider) {}

            @Override
            public void onProviderEnabled(String provider) {
                System.out.println(provider + " enabled");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

        };
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        try{

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } catch(SecurityException e){

        }


        mTask = new allEvents().execute("https://protected-ocean-61024.herokuapp.com/event/available/");
    }

    @Override
    public void onPause() {
        super.onPause();
        mTask.cancel(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Event f = (Event) l.getItemAtPosition(position);
        Intent i = new Intent(getActivity(), EventActivity.class);
        i.putExtra("id", f.getId());
        startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }
    @Override
    public void onResume() {
        super.onResume();

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        } catch (SecurityException e) {
            Log.d(TAG, "wtf it's not working");
        }
    }



    private class allEvents extends AsyncTask<String, Void, List<Event>> {
        protected List<Event> doInBackground(String... str){
            InputStream in = null;
            try {
                DataOutputStream printout;
                URL url = new URL(str[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                String id = AccessToken.getCurrentAccessToken().getUserId();
                JSONObject info = new JSONObject();
                info.put("userID", id);
                try{

                    String kkkk = Double.toString(location.getLongitude())+","+Double.toString(location.getLatitude());
                    info.put("location", kkkk);
                } catch (Exception e){
                    e.printStackTrace();
                }




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
            ((MainActivity) getActivity()).setAllEvents(eventList);
            String id = AccessToken.getCurrentAccessToken().getUserId();
            for (Iterator<Event> iter = eventList.listIterator(); iter.hasNext(); ) {
                Event e = iter.next();
                for (String a: e.attendees)
                    if(a.equals(id)){
                        iter.remove();
                        break;
                    }
            }
            EventListAdapter customAdapter = new EventListAdapter(getActivity(), R.layout.event_list_item, eventList);
            customAdapter.notifyDataSetChanged();
            setListAdapter(customAdapter);
        }
    }

    }
