package com.example.lukasz.whozzup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    Button back;
    String userID,userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // if(savedInstanceState.isEmpty() || savedInstanceState.equals(null)){System.out.println("BUndle is null");}
        Bundle bundle = this.getArguments();
        userID = bundle.getString("userID");

        userName = bundle.getString("userName");
        System.out.println("The user name and password fro craeted events is "+userID+"-"+userName);
        View v =   super.onCreateView(inflater, container, savedInstanceState);


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set a linearLayout to add buttons
        RelativeLayout rl = new RelativeLayout(getActivity());
        // Set the layout full width, full height
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        rl.setLayoutParams(params);


        back = new Button(getActivity());
        back.setText("Back");
                //For buttons visibility, you must set the layout params in order to give some width and height:
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
       params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        back.setLayoutParams(params);

        ViewGroup viewGroup = (ViewGroup) view;
        rl.addView(back);
        viewGroup.addView(rl);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        mTask = new eventsCreated().execute("https://protected-ocean-61024.herokuapp.com/user/events/", userID);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Event f = (Event) l.getItemAtPosition(position);
        Intent i = new Intent(getActivity(), EventActivity.class);
        i.putExtra("id", f.getId());
        startActivity(i);
    }


    @Override
    public void onPause(){
        super.onPause();
        mTask.cancel(true);
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


                JSONObject info = new JSONObject();
                info.put("userID", userID);



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
