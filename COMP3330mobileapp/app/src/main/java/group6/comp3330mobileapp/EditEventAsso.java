package group6.comp3330mobileapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditEventAsso extends AppCompatActivity {

    ImageView posterA;
    TextView viewA;
    TextView registration;
    TextView comment;
    TextView viewEventNameA;
    EditText viewDateA;
    EditText viewTimeA;
    EditText viewLocationA;
    TextView viewContactNameA;
    TextView viewContactPhoneA;
    EditText viewDescriptionA;
    Button save;
    Spinner spnrLocation;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference testRef = myRef.child("events");
    String key = "005";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    String[] districts = {"Select a District",
            "Central and Western", "Eastern", "Southern", "Wan Chai",
            "Sham Shui Po", "Kowloon City", "Kwun Tong", "Wong Tai Sin",
            "Yau Tsim Mong", "Islands", "Kwai Tsing", "North", "Sai Kung",
            "Sha Tin", "Tai Po", "Tsuen Wan", "Tuen Mun", "Yuen Long"};

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateDialog;
    TimePickerDialog.OnTimeSetListener timeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_asso);

        posterA = (ImageView) findViewById(R.id.posterA);
        viewA = (TextView) findViewById(R.id.view);
        registration = (TextView) findViewById(R.id.registration);
        comment = (TextView) findViewById(R.id.comment);
        viewEventNameA = (TextView) findViewById(R.id.viewEventNameA);
        viewDateA = (EditText) findViewById(R.id.viewDateA);
        viewTimeA = (EditText) findViewById(R.id.viewTimeA);
        viewLocationA = (EditText) findViewById(R.id.viewLocationA);
        viewContactNameA = (TextView) findViewById(R.id.viewContactNameA);
        viewContactPhoneA = (TextView) findViewById(R.id.viewContactPhoneA);
        viewDescriptionA = (EditText) findViewById(R.id.viewDescriptionA);
        save = (Button) findViewById(R.id.save);
        spnrLocation = (Spinner) findViewById(R.id.spinnerDistrict);

        ArrayAdapter<String> adapterLoc = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, districts);
        spnrLocation.setAdapter(adapterLoc);

        dateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(viewDateA);
            }
        };

        timeDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE, i1);
                updateTimeLabel(viewTimeA);
            }
        };

        viewDateA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditEventAsso.this, dateDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        viewTimeA.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                new TimePickerDialog(EditEventAsso.this, timeDialog, myCalendar
                        .get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        //for loading event inforamtion
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("events").child(key).child("event_name").getValue().toString();
                String date = dataSnapshot.child("events").child(key).child("eventDate").getValue().toString();
                String time = dataSnapshot.child("events").child(key).child("eventTime").getValue().toString();
                String location = dataSnapshot.child("events").child(key).child("location").getValue().toString();
                String contactName = dataSnapshot.child("events").child(key).child("contactName").getValue().toString();
                String contactPhone = dataSnapshot.child("events").child(key).child("contactPhone").getValue().toString();
                String description = dataSnapshot.child("events").child(key).child("description").getValue().toString();
                String viewCount = dataSnapshot.child("events").child(key).child("view").getValue().toString();

                //String dateTime = dataSnapshot.child("events").child(key).child("datetime").getValue().toString();
                //String district = dataSnapshot.child("events").child(key).child("district").getValue().toString();
                //String eventID = dataSnapshot.child("events").child(key).child("eventID").getValue().toString();
                //String organiser = dataSnapshot.child("events").child(key).child("organiser").getValue().toString();
                //String type = dataSnapshot.child("events").child(key).child("type").getValue().toString();
                //String university = dataSnapshot.child("events").child(key).child("university").getValue().toString();

                //"event_name:"
                //"eventDate:"
                //"eventtime:"
                //"location:"
                //"contactName"
                //"contactPhone"
                //"description:"
                //" "view:"

                //"datetime:"
                //"district:"
                //"eventID:"
                //"organiser:"
                // "type:"
                // "university:

                Log.v("E-Value", "name is: " + name);
                Log.v("E-Value", "date is: " + date);
                Log.v("E-Value", "time is: " + time);
                Log.v("E-Value", "location is: " + location);
                Log.v("E-Value", "contactName is: " + contactName);
                Log.v("E-Value", "contactPhone is: " + contactPhone);
                Log.v("E-Value", "description is: " + description);

                viewA.setText("View"+"\n"+viewCount);
                registration.setText("Registration"+"\n"+"20");
                comment.setText("Comment"+"\n"+"5");
                viewEventNameA.setText(name);
                viewDateA.setText(date);
                viewTimeA.setText(time);
                viewLocationA.setText(location);
                viewContactNameA.setText(contactName);
                viewContactPhoneA.setText(contactPhone);
                viewDescriptionA.setText(description);

                StorageReference pathReference = mStorageRef.child("eventPoster/"+name);
                //for loading poster
                Glide.with(EditEventAsso.this /* context */).using(new FirebaseImageLoader()).load(pathReference).into(posterA);

                //updated user information
                save.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String name = viewEventNameA.getText().toString();
                                String date = viewDateA.getText().toString();
                                String time = viewTimeA.getText().toString();
                                String location = viewLocationA.getText().toString();
                                String eventContactName = viewContactNameA.getText().toString();
                                String eventContactPhone = viewContactPhoneA.getText().toString();
                                String description = viewDescriptionA.getText().toString();
                                String viewCount = dataSnapshot.child("events").child(key).child("view").getValue().toString();
                                int viewCountInt = Integer.parseInt(viewCount);

                                Date dateTimeCombine = null;
                                String startDT = date + " " + time;
                                Log.v("E-Value", "startDT is: " + startDT);
                                DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm", java.util.Locale.getDefault());
                                try {
                                    dateTimeCombine = formatter.parse(startDT);

                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                                String DTCombine = String.format(java.util.Locale.getDefault(),"%015d",dateTimeCombine.getTime());
                                Log.v("E-Value", "DTCombine is: " + DTCombine);

                                String district = districts[spnrLocation.getSelectedItemPosition()];
                                String eventID = dataSnapshot.child("events").child(key).child("eventID").getValue().toString();
                                String organiser = dataSnapshot.child("events").child(key).child("organiser").getValue().toString();
                                String type = dataSnapshot.child("events").child(key).child("type").getValue().toString();
                                String university = dataSnapshot.child("events").child(key).child("university").getValue().toString();

                                EditEventAsso.Information info = new EditEventAsso.Information(name,date,time,location,
                                        eventContactName,eventContactPhone,description, DTCombine,
                                        district,university,type,eventID, organiser,viewCountInt);
                                testRef.child(eventID).setValue(info);

                                Toast.makeText(EditEventAsso.this,"Updated",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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

    public class Information{

        private String event_name;
        private String eventDate;
        private String eventTime;
        private String location;
        private String contactName;
        private String contactPhone;
        private String description;
        private String datetime;
        private String district ;
        private String university ;
        private String type;
        private String eventID;
        private int view;
        private String organiser;

        public Information(){

        }

        public Information (String event_name, String eventDate, String eventTime, String location,
                            String contactName,String contactPhone, String description, String datetime,
                            String district, String university, String type, String eventID,String organiser, int view){
            this.event_name = event_name;
            this.eventDate = eventDate;
            this.eventTime = eventTime;
            this.location = location;
            this.contactName = contactName;
            this.contactPhone = contactPhone;
            this.description = description;
            this.datetime = datetime;
            this.district = district;
            this.university = university;
            this.type = type;
            this.eventID = eventID;
            this.view = view;
            this.organiser = organiser;

        }

        public String getEvent_name(){return event_name;}
        public String getEventDate(){return eventDate;}
        public String getEventTime(){return eventTime;}
        public String getLocation(){return location;}
        public String getContactName(){return contactName;}
        public String getContactPhone(){return contactPhone;}
        public String getDescription(){return description;}
        public String getDatetime(){return datetime;}
        public String getDistrict(){return district;}
        public String getUniversity(){return university;}
        public String getType(){return type;}
        public String getEventID(){return eventID;}
        public String getOrganiser(){return organiser;}
        public int getView(){return view;}
    }





}
