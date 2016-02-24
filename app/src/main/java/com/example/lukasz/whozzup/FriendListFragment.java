package com.example.lukasz.whozzup;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


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
        // TODO implement some logic
    }
}
