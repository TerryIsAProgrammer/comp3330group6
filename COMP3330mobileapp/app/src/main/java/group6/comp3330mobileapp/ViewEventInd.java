package group6.comp3330mobileapp;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import static android.R.attr.content;
import static android.R.attr.contextUri;
import static android.R.attr.value;

public class ViewEventInd extends AppCompatActivity {

    ImageView posterI;
    TextView viewEventNameI;
    TextView viewDateI;
    TextView viewTimeI;
    TextView viewLocationI;
    TextView viewContactNameI;
    TextView viewContactPhoneI;
    TextView viewDescriptionI;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String key = "005";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    //StorageReference pathReference = mStorageRef.child("icon/3.jpg");
    //StorageReference pathReference = mStorageRef.child("eventPoster/"+"event1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_i);

        posterI = (ImageView) findViewById(R.id.posterI);
        viewEventNameI = (TextView) findViewById(R.id.viewEventNameI);
        viewDateI = (TextView) findViewById(R.id.viewDateI);
        viewTimeI = (TextView) findViewById(R.id.viewTimeI);
        viewLocationI = (TextView) findViewById(R.id.viewLocationI);
        viewContactNameI = (TextView) findViewById(R.id.viewContactNameI);
        viewContactPhoneI = (TextView) findViewById(R.id.viewContactPhoneI);
        viewDescriptionI = (TextView) findViewById(R.id.viewDescriptionI);


        //for loading event inforamtion
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated

                String name = dataSnapshot.child("events").child(key).child("event_name").getValue().toString();
                String date = dataSnapshot.child("events").child(key).child("eventDate").getValue().toString();
                String time = dataSnapshot.child("events").child(key).child("eventTime").getValue().toString();
                String location = dataSnapshot.child("events").child(key).child("location").getValue().toString();
                String contactName = dataSnapshot.child("events").child(key).child("contactName").getValue().toString();
                String contactPhone = dataSnapshot.child("events").child(key).child("contactPhone").getValue().toString();
                String description = dataSnapshot.child("events").child(key).child("description").getValue().toString();

                Log.v("E-Value", "name is: " + dataSnapshot.child("events").child(key).child("event_name").getValue().toString());
                Log.v("E-Value", "date is: " + date);
                Log.v("E-Value", "time is: " + time);
                Log.v("E-Value", "location is: " + location);
                Log.v("E-Value", "contactName is: " + contactName);
                Log.v("E-Value", "contactPhone is: " + contactPhone);
                Log.v("E-Value", "description is: " + description);

                viewEventNameI.setText(name);
                viewDateI.setText(date);
                viewTimeI.setText(time);
                viewLocationI.setText(location);
                viewContactNameI.setText(contactName);
                viewContactPhoneI.setText(contactPhone);
                viewDescriptionI.setText(description);

                StorageReference pathReference = mStorageRef.child("eventPoster/"+name);
                Glide.with(ViewEventInd.this).using(new FirebaseImageLoader()).load(pathReference).into(posterI);


            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        //for loading poster
        //Glide.with(this /* context */).using(new FirebaseImageLoader()).load(pathReference).into(posterI);


    }


}
