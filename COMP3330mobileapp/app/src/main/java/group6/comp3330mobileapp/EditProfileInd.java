package group6.comp3330mobileapp;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class EditProfileInd extends BaseActivity {

    ImageView icon;
    TextView userNameId;
    TextView name;
    TextView university;
    TextView degree;
    TextView uid;
    EditText phoneEdit;
    EditText emailEdit;
    Button save;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String key;// = "001";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_ind);

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
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);

        save = (Button) findViewById(R.id.save);


        //for loading event inforamtion
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String userNameI = dataSnapshot.child("users").child(key).child("username").getValue().toString();
                String userIdI = dataSnapshot.child("users").child(key).child("userID").getValue().toString();
                String nameI = dataSnapshot.child("users").child(key).child("name").getValue().toString();
                String universityI = dataSnapshot.child("users").child(key).child("university").getValue().toString();
                String degreeI = dataSnapshot.child("users").child(key).child("degree").getValue().toString();
                String uidI = dataSnapshot.child("users").child(key).child("uid").getValue().toString();
                String phoneI = dataSnapshot.child("users").child(key).child("tel_no").getValue().toString();
                String emailI = dataSnapshot.child("users").child(key).child("email").getValue().toString();

                String iconI = dataSnapshot.child("users").child(key).child("icon").getValue().toString();


                Log.v("E-Value", "userNameI is: " + userNameI);
                Log.v("E-Value", "userIdI is: " + userIdI);
                Log.v("E-Value", "nameI is: " + nameI);
                Log.v("E-Value", "universityI is: " + universityI);
                Log.v("E-Value", "degreeI is: " + degreeI);
                Log.v("E-Value", "uidI is: " + uidI);
                Log.v("E-Value", "phoneI is: " + phoneI);
                Log.v("E-Value", "emailI is: " + emailI);


                userNameId.setText("Username : " + userNameI + "\n" + "User ID : " + userIdI);
                name.setText(nameI);
                university.setText(universityI);
                degree.setText(degreeI);
                uid.setText(uidI);
                phoneEdit.setText(phoneI);
                emailEdit.setText(emailI);

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

        //updated user information
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                myRef.child("users").child(key).child("tel_no").setValue(phoneEdit.getText().toString());
                myRef.child("users").child(key).child("email").setValue(emailEdit.getText().toString());
                Toast.makeText(EditProfileInd.this,"Updated",Toast.LENGTH_SHORT).show();

            }
        });

    }

}
