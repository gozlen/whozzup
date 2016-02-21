package com.example.lukasz.whozzup;

import android.content.Context;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Events.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Events#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Events extends Fragment {

    public static String user_id;
    Context thiscontext;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    ListView list;
    ArrayAdapter<String> adapter;
    String[] events = {"Event_From_JSON_Object_1","Event_From_JSON_Object_2","Event_From_JSON_Object_3","Event_From_JSON_Object_4"};




    // TODO: Rename and change types of parameters
    private String mParam1;     
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Events() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Events.
     */
    // TODO: Rename and change types and number of parameters
    public static Events newInstance(String param1, String param2) {
        Events fragment = new Events();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //R.layout.fragment_events is the listView.
        View view = inflater.inflate(R.layout.fragment_events,container,false);
        list = (ListView) view.findViewById(R.id.listView);
        thiscontext = container.getContext();
        displayList(user_id);

        //adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_view_layout,R.id.row_item,events);
        //listView.setAdapter(adapter);
        return view;
    }

    private String sendRequestToServer(){
        //define how to connect to endpoint here
        return null;
    }
    private void displayList(String user_id){


            class Member extends AsyncTask<String, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }

                @Override
                protected String doInBackground(String... params) {
                    //debug
                    System.out.println("Params at DIB are :" + params.length);

                    HashMap<String, String> data = new HashMap();
                    data.put("user_id", params[0]);

                    String result = sendRequestToServer();
                    //result contains JSON object
                    return  result;
                }


                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                                                    //(Column names of table) Name, Tags, Brief Description,
                    MatrixCursor matrixCursor = new MatrixCursor(new String[] {"Name", "Tags","B_Description","date"});
                    try{
                        JSONObject jsonObject = new JSONObject(result);//the json object
                        JSONArray jsonArray = jsonObject.getJSONArray("Events_Array_From_JSON_Obj");


                        // Assuming the JSONtArray from Events has these 3 columns
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //make a JSON object for each object in the JSON returned
                            JSONObject eventItem = jsonArray.getJSONObject(i);
                            String name = eventItem.getString("name");
                            String tags = eventItem.getString("tags");
                            String B_Description = eventItem.getString("bdescription");
                            String date = eventItem.getString("date");

                            matrixCursor.addRow(new Object[]{i, name, tags,B_Description,date});
                        }


                    } catch (Exception e){
                        e.printStackTrace();
                    }

                   //list is the listView

                    //columns is the columns of the table
                    String[] columns = new String[]{
                            "name",
                            "tags",
                            "bdescription",
                            "date"
                    };

                    int[] to = new int[]{
                            R.id.name,
                            R.id.tags,
                            R.id.bdescription,
                            R.id.date

                    };
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                            thiscontext ,//need to study
                            R.layout.list_view_layout,
                            matrixCursor,
                            columns,
                            to,
                            0
                    );
                    list.setAdapter(adapter);
                }





            }//end of Member

            Member user = new Member();
            user.execute(user_id);


    }
 /*   // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/

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
