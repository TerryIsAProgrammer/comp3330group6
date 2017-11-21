package group6.comp3330mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Terry on 11/6/2017.
 */

public class HomePage extends BaseActivity{

    private DatabaseReference mDatabase;
    TextView title ;
    private int userKey;

    ArrayList<Event_forMyEvent> dataModels;
    ListView listView;
    private static HomeEventsListAdapter adapter;

    Button recentbtn;
    Button hotbtn;
    Button uniBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close );
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        title = (TextView) findViewById(R.id.TitleView);

        GlobalVariable gv = (GlobalVariable)getApplicationContext();
        String thisUserName = gv.getUserName();

        title.setText("Welcome back, "  + thisUserName);
        title.findViewById(R.id.TitleView);

        recentbtn = findViewById(R.id.RecentButton);
        hotbtn = findViewById(R.id.HotEventsButton);
        uniBtn = findViewById(R.id.UEventsButton);

        recentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModels.clear();
                setListView("recent",null);
            }
        });

        hotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModels.clear();
                setListView("hot",null);
            }
        });

        uniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModels.clear();
                setListView("uni",null);
            }
        });


        listView=findViewById(R.id.homeList);
        dataModels= new ArrayList<>();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("searchResults")) {
                //boolean isNew = extras.getBoolean("searchResults", false);
                ArrayList<String> eventArr = (ArrayList<String>)extras.get("searchResults");
                dataModels.clear();
                setListView("search",eventArr);

                //if (eventArr.isEmpty()){
                //    title.setText("No Results!");
                //}else {
                    title.setVisibility(View.GONE);
                //}
                recentbtn.setVisibility(View.GONE);
                hotbtn.setVisibility(View.GONE);
                uniBtn.setVisibility(View.GONE);
            }
            else {
                dataModels.clear();
                setListView("recent",null);
            }

        } else {
            dataModels.clear();
            setListView("recent",null);
        }
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void setListView(final String sortType, final ArrayList<String> eventArr){

        mDatabase.child("events").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataModels.clear();

                //final ArrayList<String> eventArr = new ArrayList<>();

                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    //if (d.child("organiser").getValue().equals(userkey)){
                    //eventArr.add(d.getKey());

                    String eventID = (String) d.child("eventID").getValue();
                    String eventName = (String) d.child("event_name").getValue();
                    String eventTime = (String) d.child("eventTime").getValue();
                    String eventDate = (String) d.child("eventDate").getValue();
                    String timestamp = (String) d.child("datetime").getValue();
                    int view = d.child("view").getValue(int.class);
                    String uni = (String) d.child("university").getValue();


                    long tsint = Long.valueOf(timestamp).longValue();

                    long millis = System.currentTimeMillis();// % 1000;

                    if (!sortType.equals("search")) {
                        if (tsint > millis) {
                            //dataModelsP.add(new Event_forMyEvent(eventName, eventName, eventDate, eventTime, eventID,timestamp));
                            //}else{
                            dataModels.add(new Event_forMyEvent(eventName, eventName, eventDate, eventTime, eventID, timestamp, view, uni));
                        }
                    } else {
                        dataModels.add(new Event_forMyEvent(eventName, eventName, eventDate, eventTime, eventID, timestamp, view, uni));
                    }
                    //}
                }

                if (sortType.equals("recent")) {
                    Collections.sort(dataModels);

                } else if(sortType.equals("hot")) {
                    Collections.sort(dataModels);
                    Collections.sort(dataModels, new Comparator<Event_forMyEvent>() {
                        @Override
                        public int compare(Event_forMyEvent t, Event_forMyEvent t1) {
                            //return t.getView() < t1.getView() ? 1 : t.getView() == t1.getView() ? 0 : -1;
                            return t1.getView().compareTo(t.getView());
                        }
                    });

                } else if(sortType.equals("uni")) {

                    GlobalVariable gv = (GlobalVariable)getApplicationContext();
                    String thisUni = gv.getUni();

                    Iterator<Event_forMyEvent> iter = dataModels.iterator();

                    while (iter.hasNext()) {
                        Event_forMyEvent str = iter.next();

                        if (!str.getUni().equals(thisUni))
                            iter.remove();
                    }

                    /*for (Event_forMyEvent e:dataModels){
                        if (!e.getUni().equals(thisUni)) {
                            dataModels.remove(e);
                        }
                    }*/
                } else if(sortType.equals("search")){

                    Iterator<Event_forMyEvent> iter = dataModels.iterator();

                    while (iter.hasNext()) {
                        Event_forMyEvent str = iter.next();

                        if (!eventArr.contains(str.getEventID()))
                            iter.remove();
                    }

                    /*for (Event_forMyEvent e:dataModels){
                        if (!eventArr.contains(e.getEventID())) {
                            dataModels.remove(e);
                        }
                    }*/
                }

                listView=findViewById(R.id.homeList);
                adapter= new HomeEventsListAdapter(getApplicationContext(),0,dataModels);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long l){
                        Event_forMyEvent item = dataModels.get(position);
                        String eventID = item.getEventID();

                        //if (thisidentity.equals("S")) {
                        Intent intent = new Intent(HomePage.this, ViewEventInd.class);
                        intent.putExtra("eventID", eventID);
                        startActivity(intent);
                        //finish();
                        //}else if(thisidentity.e)
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
