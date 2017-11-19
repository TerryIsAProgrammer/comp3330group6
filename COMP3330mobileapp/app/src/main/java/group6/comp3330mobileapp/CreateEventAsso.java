package group6.comp3330mobileapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateEventAsso extends BaseActivity {

    EditText eventName;
    EditText eventDate;
    EditText eventTime;
    EditText eventLocation;
    EditText contactName;
    EditText contactPhone;
    EditText eventDescription;
    Button choose;
    Button submit;
    ImageView posterUpload;
    String getPath;
    Spinner spnrLocation;
    Spinner spnrUni;
    Spinner spnrType;

    int eventIDInt=0;
    String eventIDString="";


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


    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateDialog;
    TimePickerDialog.OnTimeSetListener timeDialog;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference testRef = myRef.child("events");

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Query lastQuery = databaseReference.child("events").orderByKey().limitToLast(1);

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_asso);

        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close );
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventName = (EditText) findViewById(R.id.eventName);
        eventDate = (EditText) findViewById(R.id.evnetDate);
        eventTime = (EditText) findViewById(R.id.eventTime);
        eventLocation = (EditText) findViewById(R.id.eventLocation);
        contactName = (EditText) findViewById(R.id.contactName);
        contactPhone = (EditText) findViewById(R.id.contactPhone);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        choose = (Button) findViewById(R.id.choose);
        submit = (Button) findViewById(R.id.submit);
        posterUpload = (ImageView) findViewById(R.id.posterUpload);
        spnrLocation = (Spinner) findViewById(R.id.spinnerDistrict);
        spnrUni = (Spinner) findViewById(R.id.spinnerUni);
        spnrType = (Spinner) findViewById(R.id.spinnerType);

        ArrayAdapter<String> adapterLoc = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, districts);
        spnrLocation.setAdapter(adapterLoc);

        ArrayAdapter<String> adapterUni = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, unis);
        spnrUni.setAdapter(adapterUni);

        ArrayAdapter<String> adapterType = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, types);
        spnrType.setAdapter(adapterType);


        choose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                chooseImage();
            }
        });


        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //String key = eventName.getText().toString();
                        //String eventID = eventIDString;
                        String name = eventName.getText().toString();
                        String date = eventDate.getText().toString();
                        String time = eventTime.getText().toString();
                        String location = eventLocation.getText().toString();
                        String eventContactName = contactName.getText().toString();
                        String eventContactPhone = contactPhone.getText().toString();
                        String description = eventDescription.getText().toString();
                        String distStr = districts[spnrLocation.getSelectedItemPosition()];
                        String uniStr = unis[spnrUni.getSelectedItemPosition()];
                        String typeStr = types[spnrType.getSelectedItemPosition()];
                        int view = 0;


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


                        String message = dataSnapshot.getValue().toString();
                        String message1 = dataSnapshot.getKey().toString();
                        String message2 = dataSnapshot.getChildren().toString();
                        Log.v("E-Value", "message getValue is: " + message);
                        Log.v("E-Value", "message getKey is: " + message1);
                        Log.v("E-Value", "message getChildren is: " + message2);
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String messageC = data.child("eventID").getValue().toString();
                            //MyObject myObject = data.getValue(MyObject.class);
                            Log.v("E-Value", "message for getChildren is: " + messageC);
                            eventIDString = messageC;
                            //eventIDString = messageC;
                            //Log.v("E-Value", "message in for loop getChildren is: " + eventIDString);
                        }
                        eventIDInt = Integer.valueOf(eventIDString)+1;
                        eventIDString = String.format("%03d",eventIDInt);
                        Log.v("E-Value", "message out for loop getChildren is: " + eventIDString);


                        Information info = new Information(name,date,time,location,
                                eventContactName,eventContactPhone,description,
                                DTCombine, distStr,uniStr,typeStr,eventIDString,view);
                        //testRef.child(key).setValue(info);
                        testRef.child(eventIDString).setValue(info);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Handle possible errors.
                    }
                });

                uploadImage();

            }
        });

        dateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(eventDate);
            }
        };

        timeDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                myCalendar.set(Calendar.HOUR_OF_DAY, i);
                myCalendar.set(Calendar.MINUTE, i1);
                updateTimeLabel(eventTime);
            }
        };

        eventDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventAsso.this, dateDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        eventTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                new TimePickerDialog(CreateEventAsso.this, timeDialog, myCalendar
                        .get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Poster"), PICK_IMAGE_REQUEST);

    }


    private void uploadImage() {
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            getPath = filePath.getPath();
            Log.d("E-Value", "filePath is: " + getPath.substring(getPath.lastIndexOf("/")+1));
            Log.d("E-Value", "rename is: " + eventName.getText().toString());

            //StorageReference ref = storageRef.child("icon/*"+ UUID.randomUUID().toString());
            //StorageReference ref = storageRef.child("icon/*"+ s.substring(s.lastIndexOf("/")+1));
            StorageReference ref = storageRef.child("eventPoster/"+ eventName.getText().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(CreateEventAsso.this,"Uploaded",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CreateEventAsso.this,"Failed",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+ (int)progress+"%");
                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");
                        }
                    });
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //&& resultCode == RESULT_OK && data != null && data.getData() != null
        if(requestCode == PICK_IMAGE_REQUEST ){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                posterUpload.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
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

        public Information(){

        }

        public Information (String event_name, String eventDate, String eventTime, String location,
                            String contactName,String contactPhone, String description, String datetime,
                            String district, String university, String type, String eventID,int view){
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
        public int getView(){return view;}
    }



}

