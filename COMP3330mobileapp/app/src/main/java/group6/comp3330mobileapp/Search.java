package group6.comp3330mobileapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Search extends BaseActivity{

    //private DrawerLayout mDrawerLayout;
    //private ActionBarDrawerToggle mToggle;

    Calendar myCalendar = Calendar.getInstance();

    EditText keyword;
    Spinner spnrLocation;
    EditText location;
    Spinner uni;
    Spinner type;

    EditText startDate;
    EditText endDate;

    EditText startTime;
    EditText endTime;


    String[] districts = {"Select a District",
            "Central and Western", "Eastern", "Southern", "Wan Chai",
            "Sham Shui Po", "Kowloon City", "Kwun Tong", "Wong Tai Sin",
            "Yau Tsim Mong", "Islands", "Kwai Tsing", "North", "Sai Kung",
            "Sha Tin", "Tai Po", "Tsuen Wan", "Tuen Mun", "Yuen Long"};

    String[] unis = {"Select a University", "The University of Hong Kong",
            "The Chinese University of Hong Kong", "The Hong Kong University of Science and Technology",
            "City University of Hong Kong", "The Hong Kong Polytechnic University",
            "Hong Kong Baptist University", "Lingnan University", "The Education University of Hong Kong"};

    String[] types = {"Select a Event Type","Academic", "Sports", "Leisure", "Service", "Others"};

    Button search;


    DatePickerDialog.OnDateSetListener date1;
    DatePickerDialog.OnDateSetListener date2;

    TimePickerDialog.OnTimeSetListener time1;
    TimePickerDialog.OnTimeSetListener time2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setNavigationViewListener();

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close );

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        keyword = findViewById(R.id.keyword);
        spnrLocation = findViewById(R.id.spinnerDistrict);
        location = findViewById(R.id.locationKeyword);
        uni = findViewById(R.id.spinnerUni);
        type = findViewById(R.id.spinnerType);

        startDate= findViewById(R.id.StartDate);
        endDate=  findViewById(R.id.EndDate);

        startTime= findViewById(R.id.StartTime);
        endTime=  findViewById(R.id.EndTime);

        search = findViewById(R.id.buttonSearch);


        ArrayAdapter<String> adapterLoc = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, districts);
        spnrLocation.setAdapter(adapterLoc);

        ArrayAdapter<String> adapterUni = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, unis);
        uni.setAdapter(adapterUni);

        ArrayAdapter<String> adapterType = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, types);
        type.setAdapter(adapterType);


        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(startDate);
            }

            public void onCancel(DialogInterface dialog){
                updateLabelCancel(startDate);
            }
        };


        date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(endDate);
            }

            public void onCancel(DialogInterface dialog){
                updateLabelCancel(endDate);
            }
        };

        time1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE, i1);
                updateTimeLabel(startTime);
            }

            public void onCancel(DialogInterface dialog){
                updateLabelCancel(startTime);
            }

        };

        time2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE, i1);
                updateTimeLabel(endTime);
            }

            public void onCancel(DialogInterface dialog){
                updateLabelCancel(endTime);
            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Search.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Search.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        startTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                new TimePickerDialog(Search.this, time1, myCalendar
                        .get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                new TimePickerDialog(Search.this, time2, myCalendar
                        .get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String keywordStr = keyword.getText().toString();
                String distStr = districts[spnrLocation.getSelectedItemPosition()];
                String locStr = location.getText().toString();
                String uniStr = unis[uni.getSelectedItemPosition()];
                String typeStr = types[type.getSelectedItemPosition()];
                String startD = startDate.getText().toString();
                String endD = endDate.getText().toString();
                String startT = startTime.getText().toString();
                String endT = endTime.getText().toString();


                Date startTS = null;
                Date endTS = null;

                if (TextUtils.isEmpty(startD)){
                    startD = "01/01/1990";
                }
                if (TextUtils.isEmpty(startT)){
                    startT = "00:00";
                }
                if (TextUtils.isEmpty(endD)){
                    endD = "01/01/3000";
                }
                if (TextUtils.isEmpty(endT)){
                    endT = "23:59";
                }

                String startDT = startD + " " + startT;
                String endDT = endD + " " + endT;
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm", java.util.Locale.getDefault());


                try {
                    startTS = formatter.parse(startDT);
                    endTS = formatter.parse(endDT);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("events");

                doSearch(myRef,keywordStr,distStr,locStr,uniStr,typeStr,startTS,endTS);
            }
        });

    }

    private void updateLabel(EditText et) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, java.util.Locale.getDefault());

        et.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTimeLabel(EditText et) {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, java.util.Locale.getDefault());

        et.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelCancel(EditText et) {
        et.setText("");
    }

    private String doSearch(DatabaseReference myRef, final String keywordStr, final String distStr, final String locStr, final String uniStr, final String typeStr, Date startTS, Date endTS){

        Log.println(Log.DEBUG,"a","a");

        myRef.orderByChild("datetime")
                .startAt(String.format(java.util.Locale.getDefault(),"%015d",startTS.getTime()))
                .endAt(String.format(java.util.Locale.getDefault(),"%015d",endTS.getTime()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<Event> eventArr = new ArrayList<>();

                        Log.println(Log.DEBUG,"a","a");
                        for (DataSnapshot d:dataSnapshot.getChildren()){

                            String eventID = d.getKey();
                            String contact = d.child("contact").getValue().toString();
                            String datetime = (String)d.child("datetime").getValue();
                            String description = (String)d.child("description").getValue();
                            String district = (String)d.child("district").getValue();
                            String event_name = (String)d.child("event_name").getValue();
                            String location = (String)d.child("location").getValue();
                            String organiser = (String)d.child("organiser").getValue();
                            String type = (String)d.child("type").getValue();
                            String university = (String)d.child("university").getValue();

                            Event e1 = new Event(eventID,contact,datetime, description,district,event_name,location,organiser,type,university);

                            if (e1.allMatch(keywordStr,distStr,keywordStr,locStr,typeStr,uniStr)) {
                                eventArr.add(e1);
                            }
                        }
                        Log.println(Log.DEBUG,"a","a");
                        //Toast.makeText(Search.this,"size of eventarrr: "+eventArr.size() , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });

        return null;
    }


}

class Event{

    private String eventID;
    private String contact;
    private String datetime;
    private String description;
    private String district;
    private String event_name;
    private String location;
    private String organiser;
    private String type;
    private String university;

    public Event(String eventID,String contact,String datetime,String description,String district,String event_name,String location,String organiser,String type,String university){

        this.eventID = eventID;
        this.contact = contact;
        this.datetime = datetime;
        this.description = description;
        this.district = district;
        this.event_name = event_name;
        this.location = location;
        this.organiser = organiser;
        this.type = type;
        this.university = university;

    }

    private boolean descMatch(String str){
        return this.description.toLowerCase().contains(str.toLowerCase());
    }

    private boolean nameMatch(String str){
        return this.event_name.toLowerCase().contains(str.toLowerCase());
    }

    private boolean locMatch(String str){
        return this.location.toLowerCase().contains(str.toLowerCase());
    }

    private boolean distMatch(String str){
        return str.equals("Select a District")||this.district.toLowerCase().equals(str.toLowerCase());
    }

    private boolean typeMatch(String str){
        return str.equals("Select a Event Type")||this.type.toLowerCase().equals(str.toLowerCase());
    }

    private boolean uniMatch(String str){
        return str.equals("Select a University")||this.university.toLowerCase().equals(str.toLowerCase());
    }

    public boolean allMatch(String description,String district,String event_name,String location,String type,String university){
        return (descMatch(description) || nameMatch(event_name))
                && locMatch(location) && distMatch(district)
                && typeMatch(type) && uniMatch(university);
    }

    public String getEventID(){
        return eventID;
    }


}

