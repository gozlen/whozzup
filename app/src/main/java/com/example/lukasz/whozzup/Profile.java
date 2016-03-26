package com.example.lukasz.whozzup;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.BitmapFactory;c
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private String a ;
    private String mParam1 = null;
    private String mParam2 = null;
    private static final String TAG = Profile.class.getSimpleName();
   private OnFragmentInteractionListener mListener;
    private Util util;
    ListView list;
    TextView descriptionText;
    String descriptionContent;
    Context context;
    ProgressDialog dialog;
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
    /*public Profile(String userID){
        a = userID;
    }*/
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

            try{

                descriptionText.setText(result.description.toString());
                //Fill up other data here

            } catch (Exception e){
                e.printStackTrace();
            }


        }
    }





    private class eventsInterested extends AsyncTask<String, Void, List<Event>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(context == null){System.out.println("ITS TIME TO FREAK OUT \n\n\n\n");}
            dialog = new ProgressDialog(getView().getContext());
            dialog.setCancelable(false);
            dialog.setMessage("Loadinging Profile");
            dialog.setInverseBackgroundForced(false);
            dialog.show();

        }

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
                Log.d(TAG, e.toString());
                return eventsList;
            }
        }

        protected void onPostExecute(List<Event> result) {
            dialog.hide();
            System.out.println("Testing List 2");

            EventListAdapter customAdapter = new EventListAdapter(getActivity(), R.layout.event_list_item, result);
            list = (ListView) getView().findViewById(R.id.listView2);
            list.setAdapter(customAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Event f = (Event) parent.getItemAtPosition(position);
                    Intent i = new Intent(getActivity(), EventActivity.class);
                    i.putExtra("id", f.getId());
                    startActivity(i);
                    System.out.println("This is me doing shit");
                }

            });


        }
    }//end of class eventsInterested extends AsyncTask 2



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
        String id = null;
        if(mParam1 == null  ){
            id = AccessToken.getCurrentAccessToken().getUserId();
            System.out.println("getUserID is."+id);

        }else{
            id = mParam1;
            System.out.println("mParam1 is."+id+".");
 }

        Button btn = (Button) getView().findViewById(R.id.createdEvents);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             Fragment newFragment = new CreatedEvents();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_frame, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
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
        new fillData().execute("https://protected-ocean-61024.herokuapp.com/user/", id);

         new eventsInterested().execute("https://protected-ocean-61024.herokuapp.com/user/attending/", id);



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
