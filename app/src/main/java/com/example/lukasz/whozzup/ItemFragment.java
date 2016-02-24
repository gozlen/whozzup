package com.example.lukasz.whozzup;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lukasz.whozzup.dummy.DummyContent;
import com.example.lukasz.whozzup.dummy.DummyContent.DummyItem;

import java.util.List;


public class ItemFragment extends ListFragment {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        globals glob = ((globals)getActivity().getApplicationContext());

        ListAdapter customAdapter = new ListAdapter(getActivity(), R.layout.fragment_item, glob.getGlobalVarValue().friends);

        setListAdapter(customAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
    }
}
