package com.example.lukasz.whozzup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Create extends Fragment {
    private Util util;
    private TextView response;
    ProgressDialog dialog;

    private OnFragmentInteractionListener mListener;
    private static final String TAG = Create.class.getSimpleName();

    public Create() {
        // Required empty public constructor
    }

    public void onStart(){
        super.onStart();
        EditText editText6=(EditText)getView().findViewById(R.id.editText6);
        editText6.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });
        editText6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });

        EditText editText7=(EditText)getView().findViewById(R.id.editText7);
        editText7.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimeDialog dialog = new TimeDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "TimePicker");
                }
            }
        });
        editText7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialog dialog = new TimeDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "TimePicker");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                System.out.println("Activity finished, getting result");
                double [] result = data.getDoubleArrayExtra("result");
                EditText mEdit = (EditText) getView().findViewById(R.id.form_location);
                mEdit.setText(String.valueOf(result[0]) +"," + String.valueOf(result[1]));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  =  inflater.inflate(R.layout.fragment_create, container, false);


        final Spinner spinner = (Spinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Intent i = new Intent(getActivity(), MapsActivity.class);
        startActivityForResult(i, 1);


        final EditText maps = (EditText) v.findViewById(R.id.form_location);
        maps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent i = new Intent(getActivity(), MapsActivity.class);
                    startActivityForResult(i, 1);
                }

            }

        });

            final Button button = (Button) v.findViewById(R.id.CreateEventButton);
        response = (TextView) v.findViewById(R.id.ResponseText);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText mEdit;
                String category = (String) spinner.getSelectedItem();

                mEdit = (EditText) getView().findViewById(R.id.form_title);
                String title = mEdit.getText().toString();

                mEdit = (EditText) getView().findViewById(R.id.form_location);
                String location = mEdit.getText().toString();

                mEdit = (EditText) getView().findViewById(R.id.editText6);
                String date = mEdit.getText().toString();

                mEdit = (EditText) getView().findViewById(R.id.editText7);
                String time = mEdit.getText().toString();

                new CreateEvent().execute("https://protected-ocean-61024.herokuapp.com/event/create/", category, title, location, date, time);

                dialog = new ProgressDialog(getView().getContext());
                dialog.setCancelable(false);
                dialog.setMessage("Creating Event");
                dialog.setInverseBackgroundForced(false);
                dialog.show();

            }
        });
        return v;
    }

    private class CreateEvent extends AsyncTask<String, Void, String> {
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

                info.put("creator", id);
                info.put("category", str[1]);
                info.put("title", str[2]);
                info.put("location", str[3]);
                info.put("date", str[4]);
                info.put("time", str[5]);

                JSONArray attendees = new JSONArray();
                attendees.put(id);

                info.put("attendees", attendees);

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
                Log.d(TAG, e.toString());
                return e.toString();
            }
        }

        protected void onPostExecute(String result) {
            dialog.hide();
            response.setText(result);

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
        void onFragmentInteraction(Uri uri);
    }
}
