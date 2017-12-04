package group6.comp3330mobileapp;


import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewEventAsso extends BaseActivity {

    ImageView posterA;
    TextView view;
    TextView registration;
    TextView comment;
    TextView viewEventNameA;
    TextView viewDateA;
    TextView viewTimeA;
    TextView viewLocationA;
    TextView viewContactNameA;
    TextView viewContactPhoneA;
    TextView viewDescriptionA;

    Button editEvent;

    long regCount = 0;
    long commentCount = 0;

    CommentAdapter adapter;
    ListView listView;
    List<CommentItem> lstComment;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String key;// = "005";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    //StorageReference pathReference = mStorageRef.child("icon/3.jpg");
    //StorageReference pathReference = mStorageRef.child("icon/*"+key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_asso);

        Intent intent = getIntent();
        key = intent.getStringExtra("eventID");

        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close );
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        posterA = (ImageView) findViewById(R.id.posterA);
        view = (TextView) findViewById(R.id.view);
        registration = (TextView) findViewById(R.id.registration);
        comment = (TextView) findViewById(R.id.comment);
        viewEventNameA = (TextView) findViewById(R.id.viewEventNameA);
        viewDateA = (TextView) findViewById(R.id.viewDateA);
        viewTimeA = (TextView) findViewById(R.id.viewTimeA);
        viewLocationA = (TextView) findViewById(R.id.viewLocationA);
        viewContactNameA = (TextView) findViewById(R.id.viewContactNameA);
        viewContactPhoneA = (TextView) findViewById(R.id.viewContactPhoneA);
        viewDescriptionA = (TextView) findViewById(R.id.viewDescriptionA);

        editEvent = findViewById(R.id.editEvent);

        lstComment = new ArrayList<>();
        listView = (ListView) findViewById(R.id.commentListview);
        listView.setAdapter(adapter);

        //for loading event inforamtion
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);

                /*myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        regCount = dataSnapshot.child("participates").child(key).getChildrenCount();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                regCount = dataSnapshot.child("participates").child(key).getChildrenCount();
                commentCount = dataSnapshot.child("comment").child(key).getChildrenCount();


                String name = dataSnapshot.child("events").child(key).child("event_name").getValue().toString();
                String date = dataSnapshot.child("events").child(key).child("eventDate").getValue().toString();
                String time = dataSnapshot.child("events").child(key).child("eventTime").getValue().toString();
                String location = dataSnapshot.child("events").child(key).child("location").getValue().toString();
                String contactName = dataSnapshot.child("events").child(key).child("contactName").getValue().toString();
                String contactPhone = dataSnapshot.child("events").child(key).child("contactPhone").getValue().toString();
                String description = dataSnapshot.child("events").child(key).child("description").getValue().toString();
                String viewCount = dataSnapshot.child("events").child(key).child("view").getValue().toString();

                Log.v("E-Value", "name is: " + name);
                Log.v("E-Value", "date is: " + date);
                Log.v("E-Value", "time is: " + time);
                Log.v("E-Value", "location is: " + location);
                Log.v("E-Value", "contactName is: " + contactName);
                Log.v("E-Value", "contactPhone is: " + contactPhone);
                Log.v("E-Value", "description is: " + description);

                view.setText("View"+"\n"+viewCount);
                registration.setText("Registration"+"\n"+regCount);
                comment.setText("Comment"+"\n"+commentCount);
                viewEventNameA.setText(name);
                viewDateA.setText(date);
                viewTimeA.setText(time);
                viewLocationA.setText(location);
                viewContactNameA.setText(contactName);
                viewContactPhoneA.setText(contactPhone);
                viewDescriptionA.setText(description);

                StorageReference pathReference = mStorageRef.child("eventPoster/"+name);
                //for loading poster
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(pathReference).into(posterA);

                for(DataSnapshot user1 : dataSnapshot.child("comment").child(key).getChildren()){

                    Log.v("E-Value", "user1 is: " + user1);

                    String username = user1.child("username").getValue().toString();
                    String comment = user1.child("comment").getValue().toString();

                    lstComment.add(new CommentItem(username,comment));

                }

                Log.v("E-Value", "size is: " + lstComment.size());
                if(lstComment.size()>0){
                    adapter = new CommentAdapter(getApplicationContext(), R.layout.comment_list_row, lstComment);
                    listView.setAdapter((ListAdapter) adapter);
                }else{
                    //Toast.makeText(getApplicationContext(),"No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //for loading poster
        //Glide.with(this /* context */).using(new FirebaseImageLoader()).load(pathReference).into(posterA);


        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditEventAsso.class);
                intent.putExtra("eventID",key);
                startActivity(intent);
            }
        });
    }
}
