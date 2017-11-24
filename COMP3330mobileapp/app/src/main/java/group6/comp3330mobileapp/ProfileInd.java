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

public class ProfileInd extends BaseActivity {

    ImageView icon;
    TextView userNameId;
    TextView name;
    TextView university;
    TextView degree;
    TextView uid;
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
        setContentView(R.layout.profile_ind);

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
        name = (TextView) findViewById(R.id.name);
        university = (TextView) findViewById(R.id.university);
        degree = (TextView) findViewById(R.id.degree);
        uid = (TextView) findViewById(R.id.uid);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);

        edit = findViewById(R.id.editProfile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditProfileInd.class);
                startActivity(intent);
            }
        });

        //for loading event inforamtion
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //String iconI = dataSnapshot.child("users").child(key).child("icon").getValue().toString();
                String userName = dataSnapshot.child("users").child(key).child("username").getValue().toString();
                String userId = dataSnapshot.child("users").child(key).child("userID").getValue().toString();
                String nameI = dataSnapshot.child("users").child(key).child("name").getValue().toString();
                String universityI = dataSnapshot.child("users").child(key).child("university").getValue().toString();
                String degreeI = dataSnapshot.child("users").child(key).child("degree").getValue().toString();
                String uidI = dataSnapshot.child("users").child(key).child("uid").getValue().toString();
                String phoneI = dataSnapshot.child("users").child(key).child("tel_no").getValue().toString();
                String emailI = dataSnapshot.child("users").child(key).child("email").getValue().toString();
                String iconI = dataSnapshot.child("users").child(key).child("icon").getValue().toString();

                if (iconI == null || iconI.isEmpty()){
                    iconI = "icon/0.jpg";
                }

                Log.v("E-Value", "userName is: " + userName);
                Log.v("E-Value", "userId is: " + userId);
                Log.v("E-Value", "name is: " + nameI);
                Log.v("E-Value", "university is: " + universityI);
                Log.v("E-Value", "degree is: " + degreeI);
                Log.v("E-Value", "uid is: " + uidI);
                Log.v("E-Value", "phone is: " + phoneI);
                Log.v("E-Value", "email is: " + emailI);

                userNameId.setText("Username : " + userName + "\n" + "User Id : " + userId);
                name.setText(nameI);
                university.setText(universityI);
                degree.setText(degreeI);
                uid.setText(uidI);
                phone.setText(phoneI);
                email.setText(emailI);

                //StorageReference pathReference = mStorageRef.child("icon/"+iconI);
                StorageReference pathReference = mStorageRef.child(iconI);
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
