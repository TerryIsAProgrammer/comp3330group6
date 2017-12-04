package group6.comp3330mobileapp;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ProfileAsso extends BaseActivity {

    ImageView icon;
    TextView userNameId;
    TextView biography;
    TextView name;
    TextView university;
    TextView phone;
    TextView email;

    Button edit;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String key = null;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_asso);

        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close );
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GlobalVariable gv = (GlobalVariable)getApplicationContext();
        int userKey = gv.getUserID();
        key = String.format(java.util.Locale.getDefault(),"%03d",userKey);

        icon = (ImageView) findViewById(R.id.icon);
        userNameId = (TextView) findViewById(R.id.userNameId);
        biography = (TextView) findViewById(R.id.biography);
        name = (TextView) findViewById(R.id.name);
        university = (TextView) findViewById(R.id.university);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);

        edit = findViewById(R.id.editProfile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditProfileAsso.class);
                startActivity(intent);
            }
        });

        //for loading event inforamtion
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String userName = dataSnapshot.child("users").child(key).child("username").getValue().toString();
                String userId = dataSnapshot.child("users").child(key).child("userID").getValue().toString();
                String biographyA = dataSnapshot.child("users").child(key).child("biography").getValue().toString();
                String nameA = dataSnapshot.child("users").child(key).child("name").getValue().toString();
                String universityA = dataSnapshot.child("users").child(key).child("university").getValue().toString();
                String phoneA = dataSnapshot.child("users").child(key).child("tel_no").getValue().toString();
                String emailA = dataSnapshot.child("users").child(key).child("email").getValue().toString();
                String iconA = dataSnapshot.child("users").child(key).child("icon").getValue().toString();

                if (iconA == null || iconA.isEmpty()){
                    iconA = "icon/0.jpg";
                }

                Log.v("E-Value", "userName is: " + userName);
                Log.v("E-Value", "userId is: " + userId);
                Log.v("E-Value", "name is: " + biographyA);
                Log.v("E-Value", "name is: " + nameA);
                Log.v("E-Value", "university is: " + universityA);
                Log.v("E-Value", "phone is: " + phoneA);
                Log.v("E-Value", "email is: " + emailA);

                userNameId.setText("Username : " + userName + "\n" + "User Id : " + userId);
                biography.setText(biographyA);
                name.setText(nameA);
                university.setText(universityA);
                phone.setText(phoneA);
                email.setText(emailA);

                StorageReference pathReference = mStorageRef.child(iconA);
                //for loading poster
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(pathReference).into(icon);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
