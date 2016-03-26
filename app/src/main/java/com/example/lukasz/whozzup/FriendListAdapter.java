package com.example.lukasz.whozzup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Lukasz on 2/24/2016.
 */
public class FriendListAdapter extends ArrayAdapter<Friend> {

    public FriendListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FriendListAdapter(Context context, int resource, List<Friend> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View superView = convertView;

        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            superView = vi.inflate(R.layout.friend_list_item, null);
            Friend p = getItem(position);

            if (p != null) {
                ProfilePictureView tt1 = (ProfilePictureView) superView.findViewById(R.id.id);
                TextView tt2 = (TextView) superView.findViewById(R.id.content);

                if (tt1 != null) {
                    tt1.setProfileId(p.getId());
                }

                if (tt2 != null) {
                    tt2.setText(p.getName());
                }
            }
        }
        return superView;
    }

}