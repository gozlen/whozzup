package com.example.lukasz.whozzup;

import android.app.FragmentTransaction;
import android.content.Context;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Create.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Create#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Create extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Util util;
    private TextView response;

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



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Create.
     */
    // TODO: Rename and change types and number of parameters
    public static Create newInstance(String param1, String param2) {
        Create fragment = new Create();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        util = new Util();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  =  inflater.inflate(R.layout.fragment_create, container, false);


        final Button button = (Button) v.findViewById(R.id.CreateEventButton);
        response = (TextView) v.findViewById(R.id.ResponseText);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText mEdit;
                mEdit = (EditText) getView().findViewById(R.id.editText);
                String category = mEdit.getText().toString();

                mEdit = (EditText) getView().findViewById(R.id.editText2);
                String title = mEdit.getText().toString();

                mEdit = (EditText) getView().findViewById(R.id.editText3);
                String description = mEdit.getText().toString();

                mEdit = (EditText) getView().findViewById(R.id.editText4);
                String location = mEdit.getText().toString();

                mEdit = (EditText) getView().findViewById(R.id.editText6);
                String date = mEdit.getText().toString();

                mEdit = (EditText) getView().findViewById(R.id.editText7);
                String time = mEdit.getText().toString();

                new CreateEvent().execute("https://protected-ocean-61024.herokuapp.com/event/create/", category, title, description, location, date, time);


                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/likes",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Log.d(TAG, response.toString());
                            }
                        }
                ).executeAsync();
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
                info.put("description", str[3]);
                info.put("location", str[4]);
                info.put("date", str[5]);
                info.put("time", str[6]);

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
            response.setText(result);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
