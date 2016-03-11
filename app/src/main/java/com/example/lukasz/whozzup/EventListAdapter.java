package com.example.lukasz.whozzup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

/**
 * Created by Lukasz on 2/24/2016.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    public EventListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public EventListAdapter(Context context, int resource, List<Event> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageHelper hlp = new ImageHelper();

        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.event_list_item, null);
            Event p = getItem(position);
            System.out.println(position);

            if (p != null) {
                ImageView tt1 = (ImageView) convertView.findViewById(R.id.icon);

                String category = p.getCategory();
                Bitmap largeIcon = BitmapFactory.decodeResource(getContext().getResources(), getImage(category));
                largeIcon = hlp.getRoundedCornerBitmap(largeIcon, 500);
                tt1.setImageBitmap(largeIcon);


                TextView tt2 = (TextView) convertView.findViewById(R.id.event_time);
                TextView tt3 = (TextView) convertView.findViewById(R.id.event_title);

                if (tt2 != null) {
                    tt2.setText(p.getFullDate());
                }

                if (tt3 != null) {
                    System.out.println(p.getTitle());
                    tt3.setText(p.getTitle());
                }
            }
        }


        return convertView;
    }

    private int getImage(String category){
        if (category.equals("Food"))
            return R.drawable.food;
        else if (category.equals("Hiking"))
            return R.drawable.hiking;
        else if (category.equals("Sports"))
            return R.drawable.sports;
        else if (category.equals("Movies"))
            return R.drawable.cinema;
        else if (category.equals("Drinks"))
            return R.drawable.drinks;
        return R.drawable.food;
    }

}