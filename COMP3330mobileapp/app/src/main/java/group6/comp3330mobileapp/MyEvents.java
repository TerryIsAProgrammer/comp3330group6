package group6.comp3330mobileapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyEvents extends BaseActivity {

    ArrayList<Event_forMyEvent> dataModels;
    ArrayList<Event_forMyEvent> dataModelsP;
    ListView listView;
    ListView listViewP;
    private static MyEventsListAdapter adapter;
    private static MyEventsListAdapter adapterP;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String userkey;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        GlobalVariable gv = (GlobalVariable)getApplicationContext();
        int userKeyInt = gv.getUserID();
        String userIdentity = gv.getIdentity();
        userkey = String.format(java.util.Locale.getDefault(),"%03d",userKeyInt);

        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close );
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=findViewById(R.id.futureEvents);
        listViewP=findViewById(R.id.pastEvents);

        dataModels= new ArrayList<>();
        dataModelsP= new ArrayList<>();


        if (userIdentity.equals("S")){

            myRef.child("participates").addValueEventListener(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final ArrayList<String> eventArr = new ArrayList<>();

                    for (DataSnapshot d:dataSnapshot.getChildren()) {
                        if (d.hasChild(userkey)){
                            eventArr.add(d.getKey());
                        }
                    }

                    myRef.child("events").addValueEventListener(new ValueEventListener(){

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (String s:eventArr) {
                                String eventID = (String) dataSnapshot.child(s).child("eventID").getValue();
                                String eventName = (String) dataSnapshot.child(s).child("event_name").getValue();
                                String eventTime = (String) dataSnapshot.child(s).child("eventTime").getValue();
                                String eventDate = (String) dataSnapshot.child(s).child("eventDate").getValue();
                                String timestamp = (String) dataSnapshot.child(s).child("datetime").getValue();
                                long tsint = Long.valueOf(timestamp).longValue();

                                long millis = System.currentTimeMillis();// % 1000;

                                if (tsint <= millis){
                                    dataModelsP.add(new Event_forMyEvent(eventName, eventName, eventDate, eventTime, eventID,timestamp));
                                }else{
                                    dataModels.add(new Event_forMyEvent(eventName, eventName, eventDate, eventTime, eventID,timestamp));
                                }
                            }

                            listView=findViewById(R.id.futureEvents);
                            listViewP=findViewById(R.id.pastEvents);

                            adapter= new MyEventsListAdapter(getApplicationContext(),0,dataModels);
                            adapterP= new MyEventsListAdapter(getApplicationContext(),0,dataModelsP);

                            listView.setAdapter(adapter);
                            listViewP.setAdapter(adapterP);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapter,View v, int position,long l){
                                    Event_forMyEvent item = dataModels.get(position);
                                    String eventID = item.getEventID();

                                    Intent intent = new Intent(MyEvents.this,ViewEventInd.class);
                                    intent.putExtra("eventID",eventID);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            listViewP.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapter,View v, int position,long l){
                                    Event_forMyEvent item = dataModelsP.get(position);
                                    String eventID = item.getEventID();

                                    Intent intent = new Intent(MyEvents.this,ViewEventInd.class);
                                    intent.putExtra("eventID",eventID);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            //Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


        } else if (userIdentity.equals("A")){

            myRef.child("events").addValueEventListener(new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final ArrayList<String> eventArr = new ArrayList<>();

                    for (DataSnapshot d:dataSnapshot.getChildren()) {
                        if (d.child("organiser").getValue().equals(userkey)){
                            eventArr.add(d.getKey());

                            String eventID = (String) d.child("eventID").getValue();
                            String eventName = (String) d.child("event_name").getValue();
                            String eventTime = (String) d.child("eventTime").getValue();
                            String eventDate = (String) d.child("eventDate").getValue();
                            String timestamp = (String) d.child("datetime").getValue();
                            long tsint = Long.valueOf(timestamp).longValue();

                            long millis = System.currentTimeMillis();// % 1000;

                            if (tsint <= millis){
                                dataModelsP.add(new Event_forMyEvent(eventName, eventName, eventDate, eventTime, eventID,timestamp));
                            }else{
                                dataModels.add(new Event_forMyEvent(eventName, eventName, eventDate, eventTime, eventID,timestamp));
                            }
                        }
                    }

                    /*dataModels.sort(new Comparator<Event_forMyEvent>() {
                        @Override
                        public int compare(Event_forMyEvent t, Event_forMyEvent t1) {
                            if (Long.valueOf(t.getTimeStamp()).longValue() < Long.valueOf(t1.getTimeStamp()).longValue()){
                                return 1;
                            }
                            return 0;
                        }
                    });

                    Collections.sort(dataModels, new Comparator<Event_forMyEvent>() {
                        @Override
                        public int compare(Event_forMyEvent t, Event_forMyEvent t1) {
                            if (Long.valueOf(t.getTimeStamp()).longValue() < Long.valueOf(t1.getTimeStamp()).longValue()){
                                return 1;
                            }
                            return 0;
                        }
                    });

                    Collections.sort(dataModelsP, new Comparator<Event_forMyEvent>() {
                        @Override
                        public int compare(Event_forMyEvent t, Event_forMyEvent t1) {
                            if (Long.valueOf(t.getTimeStamp()).longValue() < Long.valueOf(t1.getTimeStamp()).longValue()){
                                return 1;
                            }
                            return 0;
                        }
                    });

                    dataModelsP.sort(new Comparator<Event_forMyEvent>() {
                        @Override
                        public int compare(Event_forMyEvent t, Event_forMyEvent t1) {
                            if (Long.valueOf(t.getTimeStamp()).longValue() < Long.valueOf(t1.getTimeStamp()).longValue()){
                                return 1;
                            }
                            return 0;
                        }
                    });*/
                    Collections.sort(dataModels);
                    Collections.sort(dataModelsP);

                    listView=findViewById(R.id.futureEvents);
                    listViewP=findViewById(R.id.pastEvents);

                    adapter= new MyEventsListAdapter(getApplicationContext(),0,dataModels);
                    adapterP= new MyEventsListAdapter(getApplicationContext(),0,dataModelsP);

                    listView.setAdapter(adapter);
                    listViewP.setAdapter(adapterP);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> adapter,View v, int position,long l){
                            Event_forMyEvent item = dataModels.get(position);
                            String eventID = item.getEventID();

                            Intent intent = new Intent(MyEvents.this,ViewEventAsso.class);
                            intent.putExtra("eventID",eventID);
                            startActivity(intent);
                            finish();
                        }
                    });

                    listViewP.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> adapter,View v, int position,long l){
                            Event_forMyEvent item = dataModelsP.get(position);
                            String eventID = item.getEventID();

                            Intent intent = new Intent(MyEvents.this,ViewEventAsso.class);
                            intent.putExtra("eventID",eventID);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }
}
