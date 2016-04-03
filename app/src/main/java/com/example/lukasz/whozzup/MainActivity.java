package com.example.lukasz.whozzup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import android.content.Intent;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


// Updated your class body:


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    JSONArray likes;
    List<Event> allEvents;
    JSONArray friends;
    private Util util = new Util();

    public void setAllEvents(List<Event> list){
        allEvents = list;
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
    private static final String TAG = MainActivity.class.getSimpleName();
    private String provider;
    boolean updated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!isLoggedIn()){
            Intent intent = new Intent(this, LoginActicity.class);
            startActivityForResult(intent,1);



        }




    }


    @Override
    protected void onResume() {
        super.onResume();

        if(isLoggedIn() && updated == false){
            //Log.d(TAG, "yay");
            likes = new JSONArray();
            friends = new JSONArray();

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/likes",
                    null,
                    HttpMethod.GET,
                    graphCallback).executeAsync();

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/friends",
                    null,
                    HttpMethod.GET,
                    graphCallback2).executeAsync();

            new UpdateProfile().execute("https://protected-ocean-61024.herokuapp.com/user/update/likes/");
            new UserInfo().execute("https://protected-ocean-61024.herokuapp.com/user/");
            updated = true;

            Fragment fragment = new Events();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, fragment)
                    .commit();

        }
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        FragmentManager fragmentManager = getFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_profile) {
            fragment = new Profile();
        } else if (id == R.id.nav_events) {
            fragment = new Events();
        } else if (id == R.id.nav_create) {
            fragment = new Create();
        } else if (id == R.id.nav_friends) {
            fragment = new FriendListFragment();
        } else if (id == R.id.nav_share) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_exit) {
            LoginManager.getInstance().logOut();
            this.finish();
            System.exit(0);
        }

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                info.put("tags", likes);
                info.put("friends", friends);

//                Log.d(TAG, likes.toString());
//                Log.d(TAG, info.toString());

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
//            Log.d(TAG, result.toString());

        }
    }

    private class UserInfo extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... str) {
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


                printout = new DataOutputStream(con.getOutputStream());
                String data = info.toString();
                byte[] send = data.getBytes("UTF-8");
                printout.write(send);
                printout.flush();
                printout.close();

                in = con.getInputStream();

                globals glob = ((globals) getApplicationContext());
                glob.setGlobalVarValue(util.readJsonStream(in));

                String res = "yay";
                return res;

            } catch (Exception e) {
                Log.d(TAG, e.toString());
                return e.toString();
            }
        }

    }



    //setup a general callback for each graph request sent, this callback will launch the next request if exists.
    final GraphRequest.Callback graphCallback = new GraphRequest.Callback(){
        @Override
        public void onCompleted(GraphResponse response) {
            try {
                JSONArray rawData = response.getJSONObject().getJSONArray("data");
                for(int j=0; j<rawData.length();j++){
                    JSONObject photo = new JSONObject();
                    photo.put("id", ((JSONObject)rawData.get(j)).get("id"));
                    photo.put("name", ((JSONObject)rawData.get(j)).get("name"));
//                    Log.d(TAG, photo.toString());
                     likes.put(photo);
                }

                //get next batch of results of exists
                GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                if(nextRequest != null){
                    nextRequest.setCallback(this);
                    nextRequest.executeAsync();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    final GraphRequest.Callback graphCallback2 = new GraphRequest.Callback(){
        @Override
        public void onCompleted(GraphResponse response) {
            try {
                JSONArray rawData = response.getJSONObject().getJSONArray("data");
                for(int j=0; j<rawData.length();j++){
                    JSONObject photo = new JSONObject();
                    photo.put("id", ((JSONObject)rawData.get(j)).get("id"));
                    photo.put("name", ((JSONObject)rawData.get(j)).get("name"));
//                    Log.d(TAG, photo.toString());
                    friends.put(photo);
                }

                //get next batch of results of exists
                GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                if(nextRequest != null){
                    nextRequest.setCallback(this);
                    nextRequest.executeAsync();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
