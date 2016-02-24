package com.example.lukasz.whozzup;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.Dialog;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = Profile.class.getSimpleName();
   private OnFragmentInteractionListener mListener;
    private Util util;
    ListView list,list2;
    TextView descriptionText;
    String descriptionContent;
    Context context;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        new fillData().execute("https://protected-ocean-61024.herokuapp.com/user/", AccessToken.getCurrentAccessToken().getUserId());

        //events that the user has created
        new eventsCreated().execute("https://protected-ocean-61024.herokuapp.com/user/events/", AccessToken.getCurrentAccessToken().getUserId());
        //events that the user is attending - userID
        new eventsInterested().execute("https://protected-ocean-61024.herokuapp.com/user/attending/", AccessToken.getCurrentAccessToken().getUserId());

        return rootView;




    }

    private class fillData extends AsyncTask<String, Void, User> {
        protected User doInBackground(String... str){
            InputStream in = null;
            User usr = null;
            Util util = new Util();
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
                printout.close ();

                in = con.getInputStream();
                usr = util.readJsonStream(in);

                return usr;

            } catch (Exception e) {
                Log.d(TAG, e.toString());
                return usr;
            }
        }

        protected void onPostExecute(User result) {
            System.out.println("RESULT HERE\n");

            try{

                descriptionText.setText(result.description.toString());
                //Fill up other data here

            } catch (Exception e){
                e.printStackTrace();
            }


        }
    }


    private class eventsCreated extends AsyncTask<String, Void, List<Events_Obj>> {
        //private class eventsCreated extends AsyncTask<String, Void, String> {
        protected List<Events_Obj> doInBackground(String... str){
            InputStream in = null;
            List<Events_Obj> eventsList = new ArrayList<Events_Obj>();
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

                eventsList = util.readEventsJsonStream(in);
                System.out.println(eventsList.size());
                return eventsList;


            } catch (Exception e) {
                Log.d(TAG, e.toString());
               return eventsList;
            }
        }

        protected void onPostExecute(List<Events_Obj> result) {
           // System.out.println("EVENTS_RESULT HERE\n");


            JSONArray jsonArray = new JSONArray();
            try {

                for (int i=0; i < result.size(); i++) {
                jsonArray.put(result.get(i).getJSONObject());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println(jsonArray.toString());
            ListView listView1 = (ListView) getView().findViewById(R.id.listView1);
           try{
               MatrixCursor matrixCursor = new MatrixCursor(new String[] {"_id", "eventTitle","eventCategory","eventDescription","eventLocation","eventDate","eventTime","eventAttendees"});

               // Assuming the JSONtArray from Members has these 3 columns
                for (int i = 0; i < jsonArray.length(); i++) {
                    //make a JSON object for each object in the JSON returned
                    JSONObject todoItem = jsonArray.getJSONObject(i);
                    String title = todoItem.getString("Title");
                    String cat = todoItem.getString("Category");
                    String des = todoItem.getString("Description");
                    String loc = todoItem.getString("Location");
                    String date = todoItem.getString("Date");
                    String time = todoItem.getString("Time");
                    String atten = "Attendees "+i;
                    //String atten = todoItem.getString("Attendees");
                    matrixCursor.addRow(new Object[]{i, title,cat,des,loc,date,time,atten});
                }
               fillListView(1,matrixCursor);
            } catch (Exception e)
            {  e.printStackTrace();
            }

        }
    }//end of class eventsCreated extends AsyncTask


    private class eventsInterested extends AsyncTask<String, Void, List<Events_Obj>> {
       protected List<Events_Obj> doInBackground(String... str){
            InputStream in = null;
            List<Events_Obj> eventsList = new ArrayList<Events_Obj>();
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

                eventsList = util.readEventsJsonStream(in);
                System.out.println(eventsList.size());
                return eventsList;


            } catch (Exception e) {
                Log.d(TAG, e.toString());
                return eventsList;
            }
        }

        protected void onPostExecute(List<Events_Obj> result) {
            System.out.println("EVENTS_RESULT HERE\n");


            JSONArray jsonArray = new JSONArray();
            try {

                for (int i=0; i < result.size(); i++) {
                    jsonArray.put(result.get(i).getJSONObject());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println(jsonArray.toString());
            ListView listView1 = (ListView) getView().findViewById(R.id.listView1);
            try{
                MatrixCursor matrixCursor = new MatrixCursor(new String[] {"_id", "eventTitle","eventCategory","eventDescription","eventLocation","eventDate","eventTime","eventAttendees"});

                // Assuming the JSONtArray from Members has these 3 columns
                for (int i = 0; i < jsonArray.length(); i++) {
                    //make a JSON object for each object in the JSON returned
                    JSONObject todoItem = jsonArray.getJSONObject(i);
                    String title = todoItem.getString("Title");
                    String cat = todoItem.getString("Category");
                    String des = todoItem.getString("Description");
                    String loc = todoItem.getString("Location");
                    String date = todoItem.getString("Date");
                    String time = todoItem.getString("Time");
                    String atten = "Attendees "+i;
                    //String atten = todoItem.getString("Attendees");
                    matrixCursor.addRow(new Object[]{i, title,cat,des,loc,date,time,atten});
                }
                fillListView(2,matrixCursor);
            } catch (Exception e)
            {  e.printStackTrace();
            }

        }
    }//end of class eventsInterested extends AsyncTask 2

    public void fillListView(int listViewNumber,MatrixCursor matrixCursor){
        if(listViewNumber == 1){
        list = (ListView) getView().findViewById(R.id.listView1);
        }
        else{
            list = (ListView) getView().findViewById(R.id.listView2);
        }

        String[] columns = new String[]{
                "_id",
                "eventTitle",
                "eventCategory",
                "eventDescription",
                "eventLocation",
                "eventDate",
                "eventTime",
                "eventAttendees"

        };

        int[] to = new int[]{
                R.id.eventID,
                R.id.eventTitle,
                R.id.eventCategory,
                R.id.eventDescription,
                R.id.eventLocation,
                R.id.eventDate,
                R.id.eventTime,
                R.id.eventAttendees,

        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                context,
                R.layout.events_list_layout,
                matrixCursor,
                columns,
                to,
                0
        );
        list.setAdapter(adapter);
    }//end of fillListView

    private class updateDescription extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... str){
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
                info.put("description", str[1]);



                printout = new DataOutputStream(con.getOutputStream ());
                String data = info.toString();
                byte[] send = data.getBytes("UTF-8");
                printout.write(send);
                printout.flush ();
                printout.close ();

                in = con.getInputStream();
                String res = util.readIt(in, 500);
                return res;

            } catch (Exception e) {
//                Log.d(TAG, e.toString());
                return e.toString();
            }
        }

        protected void onPostExecute(String result) {

            if(result.substring(0,1).equals(new String("1"))){
                Toast.makeText(context , "Description Updated.",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(context , "Update Failed. Try again.",Toast.LENGTH_SHORT).show();
                descriptionText.setText(descriptionContent);
                }




        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();

        String id = AccessToken.getCurrentAccessToken().getUserId();

        ProfilePictureView profPict;
        profPict = (ProfilePictureView) getView().findViewById(R.id.profile_picture);
        profPict.setProfileId(id);


        descriptionText =(TextView)getView().findViewById(R.id.descriptionTextBox);
        descriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(v.getContext());
                input.setHeight(200);
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Description")
                        .setMessage("Write something about yourself!")
                        .setView(input)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                descriptionContent = descriptionText.getText().toString();
                                descriptionText.setText(input.getText().toString());
                                //function to connect to DB and send data
                                new updateDescription().execute("https://protected-ocean-61024.herokuapp.com/user/update/description/", input.getText().toString());

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }
        });


          //listView
        list = (ListView) getView().findViewById(R.id.listView1);
        MatrixCursor matrixCursor = new MatrixCursor(new String[] {"_id", "eventName","eventDate","eventDescription"});
        try{

            // Assuming the JSONtArray from Members has these 3 columns
            for (int i = 0; i < 10; i++) {
                //make a JSON object for each object in the JSON returned
                String eventName = "Event Name";
                String eventDate = "Date";
                String eventDescription = "Event Description";
                matrixCursor.addRow(new Object[]{i, eventName, eventDate,eventDescription});
            }
        } catch (Exception e)
        {  e.printStackTrace();
        }
        list = (ListView) getView().findViewById(R.id.listView1);
        String[] columns = new String[]{
                "_id",
                "eventName",
                "eventDate",
                "eventDescription"
        };

        int[] to = new int[]{
                R.id.eventID,
                R.id.eventTitle,
                R.id.eventDate,
                R.id.eventDescription

        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                context,
                R.layout.events_list_layout,
                matrixCursor,
                columns,
                to,
                0
        );
        list.setAdapter(adapter);



        //
        //listView2
        list2 = (ListView) getView().findViewById(R.id.listView2);
         matrixCursor = new MatrixCursor(new String[] {"_id", "eventName","reason","eventDescription"});
        try{

            // Assuming the JSONtArray from Members has these 3 columns
            for (int i = 0; i < 10; i++) {
                //make a JSON object for each object in the JSON returned
                String eventName = "Event Name - Date";
                String reason = "Reason Suggested ";
                String eventDescription = "Event Description";
                matrixCursor.addRow(new Object[]{i, eventName, reason,eventDescription});
            }
        } catch (Exception e)
        {  e.printStackTrace();
        }
        list = (ListView) getView().findViewById(R.id.listView2);
        columns = new String[]{
                "_id",
                "eventName",
                "reason",
                "eventDescription"
        };

        to = new int[]{
                R.id.eventID,
                R.id.eventTitle,
                R.id.eventDate,
                R.id.eventDescription

        };
        adapter = new SimpleCursorAdapter(
                context,
                R.layout.events_list_layout,
                matrixCursor,
                columns,
                to,
                0
        );
        list2.setAdapter(adapter);



        //
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
