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

public class EditProfileAsso extends BaseActivity {

    ImageView icon;
    TextView userNameId;
    TextView biography;
    TextView name;
    TextView university;
    EditText phoneEdit;
    EditText emailEdit;

    Button save;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    //DatabaseReference testRef = myRef.child("users");
    String key;// = "003";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_asso);

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
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);

        save = (Button) findViewById(R.id.save);

        //for loading event inforamtion
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String iconA = dataSnapshot.child("users").child(key).child("icon").getValue().toString();
                String userNameA = dataSnapshot.child("users").child(key).child("username").getValue().toString();
                String userIdA = dataSnapshot.child("users").child(key).child("userID").getValue().toString();
                String biographyA = dataSnapshot.child("users").child(key).child("biography").getValue().toString();
                String nameA = dataSnapshot.child("users").child(key).child("name").getValue().toString();
                String universityA = dataSnapshot.child("users").child(key).child("university").getValue().toString();
                String phoneA = dataSnapshot.child("users").child(key).child("tel_no").getValue().toString();
                String emailA = dataSnapshot.child("users").child(key).child("email").getValue().toString();

                Log.v("E-Value", "iconI is: " + iconA);
                Log.v("E-Value", "userNameI is: " + userNameA);
                Log.v("E-Value", "userIdI is: " + userIdA);
                Log.v("E-Value", "password is: " + biographyA);
                Log.v("E-Value", "nameI is: " + nameA);
                Log.v("E-Value", "universityI is: " + universityA);
                Log.v("E-Value", "phoneI is: " + phoneA);
                Log.v("E-Value", "emailI is: " + emailA);

                userNameId.setText("Username : " + userNameA + "\n" + "User ID : " + userIdA);
                biography.setText(biographyA);
                name.setText(nameA);
                university.setText(universityA);
                phoneEdit.setText(phoneA);
                emailEdit.setText(emailA);

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

        //updated user information
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                myRef.child("users").child(key).child("tel_no").setValue(phoneEdit.getText().toString());
                myRef.child("users").child(key).child("email").setValue(emailEdit.getText().toString());

                Toast.makeText(EditProfileAsso.this,"Updated",Toast.LENGTH_SHORT).show();

                finish();

            }
        });

    }

}
