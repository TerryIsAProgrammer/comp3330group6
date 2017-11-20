package group6.comp3330mobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by Crystal on 21/11/2017.
 */

public class HomeEventsListAdapter extends ArrayAdapter<Event_forMyEvent> {


    public HomeEventsListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public HomeEventsListAdapter(Context context, int resource, List<Event_forMyEvent> items) {
        super(context, resource, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.home_page_list_adapter, null);
        }

        Event_forMyEvent p = getItem(position);

        if (p != null) {
            ImageView tt1 = v.findViewById(R.id.eventPic);
            TextView tt2 = v.findViewById(R.id.eventTitle);
            TextView tt3 = v.findViewById(R.id.eventTime);
            TextView tt4 = v.findViewById(R.id.eventDate);

            if (tt1 != null) {
                StorageReference pathReference = mStorageRef.child("eventPoster/"+p.getTitle());
                Glide.with(getContext()).using(new FirebaseImageLoader()).load(pathReference).into(tt1);
            }

            if (tt2 != null) {
                tt2.setText(p.getTitle());
            }

            if (tt3 != null) {
                tt3.setText(p.getTime());
            }

            if (tt4 != null) {
                tt4.setText(p.getDate());
            }
        }

        return v;

    }
}
