package com.example.lukasz.whozzup;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SettingsFragment extends Fragment {
    JSONArray interests;
    int slider;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }


    @Override
    public void onStart(){
        super.onStart();




        globals glob = ((globals) getActivity().getApplicationContext());
        final User user = glob.getGlobalVarValue();
        if (user != null) {
            if (user.interests.contains("movies"))
                ((CheckBox) getView().findViewById(R.id.movies_box)).setChecked(true);
            if (user.interests.contains("drinks"))
                ((CheckBox) getView().findViewById(R.id.drink_box)).setChecked(true);
            if (user.interests.contains("hiking"))
                ((CheckBox) getView().findViewById(R.id.hiking_box)).setChecked(true);
            if (user.interests.contains("food"))
                ((CheckBox) getView().findViewById(R.id.food_box)).setChecked(true);
            if (user.interests.contains("sports"))
                ((CheckBox) getView().findViewById(R.id.sports_box)).setChecked(true);
            if(user.slider !=0)
                ((SeekBar) getView().findViewById(R.id.seeker)).setProgress(user.slider);
        }

        Button b = (Button) getView().findViewById(R.id.update_settings);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                slider = ((SeekBar) getView().findViewById(R.id.seeker)).getProgress();
                interests = new JSONArray();

                if (((CheckBox) getView().findViewById(R.id.movies_box)).isChecked())
                    interests.put("movies");
                if (((CheckBox) getView().findViewById(R.id.drink_box)).isChecked())
                    interests.put("drinks");
                if (((CheckBox) getView().findViewById(R.id.hiking_box)).isChecked())
                    interests.put("hiking");
                if (((CheckBox) getView().findViewById(R.id.food_box)).isChecked())
                    interests.put("food");
                if (((CheckBox) getView().findViewById(R.id.sports_box)).isChecked())
                    interests.put("sports");

                user.slider = ((SeekBar) getView().findViewById(R.id.seeker)).getProgress();
                user.interests = new ArrayList<String>();
                try{

                    for (int i = 0; i < interests.length(); i++)
                        user.interests.add((String) interests.get(i));
                } catch (Exception e){

                }
                new UpdateProfile().execute("https://protected-ocean-61024.herokuapp.com/user/update/interests/");
            }
        });


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class UpdateProfile extends AsyncTask<String, Void, String> {
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


                info.put("interests", interests);
                info.put("slider", slider );

                printout = new DataOutputStream(con.getOutputStream ());
                String data = info.toString();
                byte[] send = data.getBytes("UTF-8");
                printout.write(send);
                printout.flush();
                printout.close ();

                Util util = new Util();
                in = con.getInputStream();
                String res = util.readIt(in, 500);
                return res;

            } catch (Exception e) {
//                Log.d(TAG, e.toString());
                return e.toString();
            }
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getApplicationContext(), "Preferences updated", Toast.LENGTH_SHORT).show();

        }
    }
}
