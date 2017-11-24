package group6.comp3330mobileapp;


import android.content.Intent;
import android.icu.util.Calendar;
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
import android.widget.Toast;

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

public class ViewEventInd extends BaseActivity {

    ImageView posterI;
    TextView viewEventNameI;
    TextView viewDateI;
    TextView viewTimeI;
    TextView viewLocationI;
    TextView viewContactNameI;
    TextView viewContactPhoneI;
    TextView viewDescriptionI;

    Button buttonJoin;
    Button addCalendar;
    Button addComment;

    CommentAdapter adapter;
    ListView listView;
    List<CommentItem> lstComment;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String key;// = "005";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    //StorageReference pathReference = mStorageRef.child("icon/3.jpg");
    //StorageReference pathReference = mStorageRef.child("eventPoster/"+"event1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event_ind);

        Intent intent = getIntent();

        key = intent.getStringExtra("eventID");

        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        posterI = (ImageView) findViewById(R.id.posterI);
        viewEventNameI = (TextView) findViewById(R.id.viewEventNameI);
        viewDateI = (TextView) findViewById(R.id.viewDateI);
        viewTimeI = (TextView) findViewById(R.id.viewTimeI);
        viewLocationI = (TextView) findViewById(R.id.viewLocationI);
        viewContactNameI = (TextView) findViewById(R.id.viewContactNameI);
        viewContactPhoneI = (TextView) findViewById(R.id.viewContactPhoneI);
        viewDescriptionI = (TextView) findViewById(R.id.viewDescriptionI);

        buttonJoin = findViewById(R.id.buttonJoin);
        addCalendar = findViewById(R.id.addCalendar);
        addComment = findViewById(R.id.addComment);

        lstComment = new ArrayList<>();
        listView = (ListView) findViewById(R.id.commentListview);
        listView.setAdapter(adapter);

        //Check button join status
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GlobalVariable gv = (GlobalVariable) getApplicationContext();
                int userKey = gv.getUserID();
                String userKeyStringString = String.format(java.util.Locale.getDefault(), "%03d", userKey);
                if (dataSnapshot.child("participates").child(key).hasChild(userKeyStringString)) {
                    // run some code
                    //myRef.child("participates").child(key).child(userKeyStringString).setValue(true);
                    //Toast.makeText(ViewEventInd.this,"Activity Joined!" , Toast.LENGTH_SHORT).show();
                    buttonJoin.setText("Not\nJoin");
                    addCalendar.setVisibility(View.VISIBLE);
                } else {
                    buttonJoin.setText("Join\nNow!");
                    addCalendar.setVisibility(View.GONE);
                    //Toast.makeText(ViewEventInd.this,"Activity Not Join!" , Toast.LENGTH_SHORT).show();

                }

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
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        String userIdentity = gv.getIdentity();
        if (userIdentity.equals("A")) {
            buttonJoin.setVisibility(View.GONE);
            addCalendar.setVisibility(View.GONE);
        }

        buttonJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GlobalVariable gv = (GlobalVariable) getApplicationContext();
                        int userKey = gv.getUserID();
                        String userKeyStringString = String.format(java.util.Locale.getDefault(), "%03d", userKey);
                        if (!dataSnapshot.child("participates").child(key).hasChild(userKeyStringString)) {
                            // run some code
                            myRef.child("participates").child(key).child(userKeyStringString).setValue(true);
                            Toast.makeText(ViewEventInd.this, "Activity Joined!", Toast.LENGTH_SHORT).show();
                            buttonJoin.setText("Not\nJoin");
                            addCalendar.setVisibility(View.VISIBLE);
                            Log.v("E-Value", "exist is: " + userKeyStringString);
                        } else {
                            //myRef.child("participates").child(key).child(userKeyStringString).setValue(false);
                            myRef.child("participates").child(key).child(userKeyStringString).removeValue();
                            buttonJoin.setText("Join\nNow!");
                            addCalendar.setVisibility(View.GONE);
                            Toast.makeText(ViewEventInd.this, "Activity Not Join!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        addCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String eventName = dataSnapshot.child("events").child(key).child("event_name").getValue().toString();
                        String date = dataSnapshot.child("events").child(key).child("eventDate").getValue().toString();
                        String time = dataSnapshot.child("events").child(key).child("eventTime").getValue().toString();
                        String description = dataSnapshot.child("events").child(key).child("description").getValue().toString();

                        Log.v("E-Value", "eventName is: " + eventName);
                        Log.v("E-Value", "date is: " + date);
                        Log.v("E-Value", "time is: " + time);

                        int month = Integer.parseInt(date.substring(0, 2));
                        int day = Integer.parseInt(date.substring(3, 5));
                        int year = Integer.parseInt(date.substring(6, 8));

                        Log.v("E-Value", "month is: " + month);
                        Log.v("E-Value", "day is: " + day);
                        Log.v("E-Value", "year is: " + year);

                        int hour = Integer.parseInt(time.substring(0, time.indexOf(":")));
                        int min = Integer.parseInt(time.substring(time.indexOf(":") + 1));
                        Log.v("E-Value", "hour is: " + hour);
                        Log.v("E-Value", "min is: " + min);

                        Calendar cal = Calendar.getInstance();

                        cal.set(year + 2000, month - 1, day, hour, min);
                        long beginTime = cal.getTimeInMillis();

                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("title", eventName);
                        intent.putExtra("description", description);
                        intent.putExtra("beginTime", beginTime);
                        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        //for loading event inforamtion
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

                StorageReference pathReference = mStorageRef.child("eventPoster/" + name);
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(pathReference).into(posterI);


                String timestamp = (String) dataSnapshot.child("events").child(key).child("datetime").getValue();
                long tsint = Long.valueOf(timestamp).longValue();

                long millis = System.currentTimeMillis();

                if (tsint < millis) {
                    buttonJoin.setVisibility(View.GONE);
                    addCalendar.setVisibility(View.GONE);
                }

                long views = (long) dataSnapshot.child("events").child(key).child("view").getValue();
                myRef.child("events").child(key).child("view").setValue(views + 1);
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //for loading poster
        //Glide.with(this /* context */).using(new FirebaseImageLoader()).load(pathReference).into(posterI);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Comment.class);
                intent.putExtra("eventID", key);
                startActivity(intent);
            }
        });
    }


}
