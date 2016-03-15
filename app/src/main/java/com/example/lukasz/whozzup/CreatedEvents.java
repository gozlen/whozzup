package com.example.lukasz.whozzup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Iterator;
import java.util.List;


public class CreatedEvents extends ListFragment {

    AsyncTask mTask;
    List<Event> eventlist;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mTask = new allEvents().execute("https://protected-ocean-61024.herokuapp.com/event/all/");
        String id = id = AccessToken.getCurrentAccessToken().getUserId();
        System.out.println("THE USER ID RIGHT NOW IS " + id + "\n");
        mTask = new eventsCreated().execute("https://protected-ocean-61024.herokuapp.com/user/events/", id);

    }


    @Override
    public void onPause(){
        super.onPause();
        mTask.cancel(true);
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
                //System.out.println("WTF");

                List<Event> eventList = util.readEventArray(in);

                return eventList;


            } catch (Exception e) {
                return new ArrayList<>();

            }
        }

        protected void onPostExecute(List<Event> eventList){
            String id = AccessToken.getCurrentAccessToken().getUserId();

            EventListAdapter customAdapter = new EventListAdapter(getActivity(), R.layout.event_list_item, eventList);
            setListAdapter(customAdapter);
        }
    }
    private class eventsCreated extends AsyncTask<String, Void, List<Event>> {

        //private class eventsCreated extends AsyncTask<String, Void, String> {
        protected List<Event> doInBackground(String... str){
            InputStream in = null;
            List<Event> eventsList = new ArrayList<Event>();
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

                eventsList = util.readEventArray(in);

                return eventsList;


            } catch (Exception e) {
                e.printStackTrace();
                return eventsList;
            }
        }

        protected void onPostExecute(List<Event> result) {
            EventListAdapter customAdapter = new EventListAdapter(getActivity(), R.layout.event_list_item, result);
           setListAdapter(customAdapter);



        }
    }//end of class eventsCreated extends AsyncTask
}
