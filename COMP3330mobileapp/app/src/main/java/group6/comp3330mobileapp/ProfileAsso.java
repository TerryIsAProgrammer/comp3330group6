package group6.comp3330mobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

public class ProfileAsso extends AppCompatActivity {

    ImageView icon;
    TextView userNameId;
    TextView biography;
    TextView name;
    //TextView gender;
    TextView university;
    //TextView degree;
    //TextView uid;
    TextView phone;
    TextView email;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String key = "003";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_asso);

        icon = (ImageView) findViewById(R.id.icon);
        userNameId = (TextView) findViewById(R.id.userNameId);
        biography = (TextView) findViewById(R.id.biography);
        name = (TextView) findViewById(R.id.name);
        //gender = (TextView) findViewById(R.id.gender);
        university = (TextView) findViewById(R.id.university);
        //degree = (TextView) findViewById(R.id.degree);
        //uid = (TextView) findViewById(R.id.uid);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);

        //for loading event inforamtion
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);


                //String iconI = dataSnapshot.child("users").child(key).child("icon").getValue().toString();
                String userName = dataSnapshot.child("users").child(key).child("username").getValue().toString();
                String userId = dataSnapshot.child("users").child(key).child("userID").getValue().toString();
                String biographyA = dataSnapshot.child("users").child(key).child("biography").getValue().toString();
                String nameA = dataSnapshot.child("users").child(key).child("name").getValue().toString();
                //String genderI = dataSnapshot.child("users").child(key).child("gender").getValue().toString();
                String universityA = dataSnapshot.child("users").child(key).child("university").getValue().toString();
                //String degreeI = dataSnapshot.child("users").child(key).child("degree").getValue().toString();
                //String uidI = dataSnapshot.child("users").child(key).child("uid").getValue().toString();
                String phoneA = dataSnapshot.child("users").child(key).child("tel_no").getValue().toString();
                String emailA = dataSnapshot.child("users").child(key).child("email").getValue().toString();

                Log.v("E-Value", "userName is: " + userName);
                Log.v("E-Value", "userId is: " + userId);
                Log.v("E-Value", "name is: " + nameA);
                //Log.v("E-Value", "gender is: " + genderI);
                Log.v("E-Value", "university is: " + universityA);
                //Log.v("E-Value", "degree is: " + degreeI);
                //Log.v("E-Value", "uid is: " + uidI);
                Log.v("E-Value", "phone is: " + phoneA);
                Log.v("E-Value", "email is: " + emailA);

                userNameId.setText("Username : " + userName + "\n" + "User Id : " + userId);
                biography.setText(biographyA);
                name.setText(nameA);
                //gender.setText(genderI);
                university.setText(universityA);
                //degree.setText(degreeI);
                //uid.setText(uidI);
                phone.setText(phoneA);
                email.setText(emailA);

                //StorageReference pathReference = mStorageRef.child("icon/"+iconI);
                StorageReference pathReference = mStorageRef.child("icon/3.jpg");
                //for loading poster
                Glide.with(ProfileAsso.this /* context */).using(new FirebaseImageLoader()).load(pathReference).into(icon);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }
}
