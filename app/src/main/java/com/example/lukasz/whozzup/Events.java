package com.example.lukasz.whozzup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Events extends ListFragment {

    List<Event> eventlist;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new allEvents().execute("https://protected-ocean-61024.herokuapp.com/event/all/");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

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



                printout = new DataOutputStream(con.getOutputStream ());
                String data = info.toString();
                byte[] send = data.getBytes("UTF-8");
                printout.write(send);
                printout.flush();
                printout.close();
                Util util = new Util();
                in = con.getInputStream();
                System.out.println("WTF");

                List<Event> eventList = util.readEventArray(in);

                return eventList;


            } catch (Exception e) {
                return new ArrayList<>();

            }
        }

        protected void onPostExecute(List<Event> eventList){
            EventListAdapter customAdapter = new EventListAdapter(getActivity(), R.layout.event_list_item, eventList);

            setListAdapter(customAdapter);
        }
    }

    }
