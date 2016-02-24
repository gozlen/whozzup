package com.example.lukasz.whozzup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class FriendListFragment extends ListFragment {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        globals glob = ((globals)getActivity().getApplicationContext());

        FriendListAdapter customAdapter = new FriendListAdapter(getActivity(), R.layout.fragment_item, glob.getGlobalVarValue().friends);

        setListAdapter(customAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Friend f = (Friend) l.getItemAtPosition(position);
        if(!f.equals(null)){
        System.out.println("ID: "+f.getId()+". Name: "+f.getName());}
        else{
            System.out.print("-----NULL");
        }

        /*Create nextFrag= new Create();
        this.getFragmentManager().beginTransaction()
                .replace(R.layout.fragment_create, nextFrag)
                .addToBackStack(null)
                .commit();*/

    }
}
