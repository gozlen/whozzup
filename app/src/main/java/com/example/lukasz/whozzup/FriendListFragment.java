package com.example.lukasz.whozzup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FriendListFragment extends ListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        globals glob = ((globals) getActivity().getApplicationContext());
        User user = glob.getGlobalVarValue();


        if (user == null || user.friends == null){
            new allFriends().execute("https://protected-ocean-61024.herokuapp.com/user/friends/");
        } else {
            FriendListAdapter customAdapter = new FriendListAdapter(getActivity(), R.layout.friend_list_item, user.friends);

            setListAdapter(customAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Friend f = (Friend) l.getItemAtPosition(position);
        if(!f.equals(null)){
        System.out.println("ID: " + f.getId() + ". Name: " + f.getName());

        Fragment fragment = null;

        FragmentManager fragmentManager = getFragmentManager();
            String nul = null;
        fragment = new Profile().newInstance(f.getId(),null);

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, fragment)
                    .commit();


        }
        else{
            System.out.print("-----NULL");
        }

        /*Create nextFrag= new Create();
        this.getFragmentManager().beginTransaction()
                .replace(R.layout.fragment_create, nextFrag)
                .addToBackStack(null)
                .commit();*/

    }

    private class allFriends extends AsyncTask<String, Void, List<Friend>> {
        protected List<Friend> doInBackground(String... str){
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
                JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                List<Friend> friendList = util.readFriendsArray(reader);

                return friendList;


            } catch (Exception e) {
                return new ArrayList<>();

            }
        }
        protected void onPostExecute(List<Friend> friendList){

            globals glob = ((globals) getActivity().getApplicationContext());
            User user = glob.getGlobalVarValue();

            user.friends = friendList;
            FriendListAdapter customAdapter = new FriendListAdapter(getActivity(), R.layout.friend_list_item, user.friends);

            setListAdapter(customAdapter);
        }
    }

}
