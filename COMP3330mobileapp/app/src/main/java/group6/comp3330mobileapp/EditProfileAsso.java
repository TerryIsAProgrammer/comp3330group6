package group6.comp3330mobileapp;

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

public class EditProfileAsso extends AppCompatActivity {

    ImageView icon;
    TextView userName;
    TextView userId;
    TextView identity;
    TextView name;
    TextView gender;
    TextView university;
    TextView degree;
    TextView uid;
    EditText phoneEdit;
    EditText emailEdit;
    TextView biography;
    TextView password;
    TextView iconId;
    Button save;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference testRef = myRef.child("users");
    String key = "003";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_asso);

        icon = (ImageView) findViewById(R.id.icon);
        userName = (TextView) findViewById(R.id.userName);
        userId = (TextView) findViewById(R.id.userId);
        identity = (TextView) findViewById(R.id.identity);
        name = (TextView) findViewById(R.id.name);
        gender = (TextView) findViewById(R.id.gender);
        university = (TextView) findViewById(R.id.university);
        degree = (TextView) findViewById(R.id.degree);
        uid = (TextView) findViewById(R.id.uid);
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        biography = (TextView) findViewById(R.id.biography);
        password = (TextView) findViewById(R.id.password);
        iconId = (TextView) findViewById(R.id.iconId);
        save = (Button) findViewById(R.id.save);

        //for loading event inforamtion
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String iconI = dataSnapshot.child("users").child(key).child("icon").getValue().toString();
                String userNameI = dataSnapshot.child("users").child(key).child("username").getValue().toString();
                String userIdI = dataSnapshot.child("users").child(key).child("userID").getValue().toString();
                String identityI = dataSnapshot.child("users").child(key).child("identity").getValue().toString();
                String nameI = dataSnapshot.child("users").child(key).child("name").getValue().toString();
                String genderI = dataSnapshot.child("users").child(key).child("gender").getValue().toString();
                String universityI = dataSnapshot.child("users").child(key).child("university").getValue().toString();
                String degreeI = dataSnapshot.child("users").child(key).child("degree").getValue().toString();
                String uidI = dataSnapshot.child("users").child(key).child("uid").getValue().toString();
                String phoneI = dataSnapshot.child("users").child(key).child("tel_no").getValue().toString();
                String emailI = dataSnapshot.child("users").child(key).child("email").getValue().toString();
                String passwordI = dataSnapshot.child("users").child(key).child("password").getValue().toString();
                String biographyI = dataSnapshot.child("users").child(key).child("biography").getValue().toString();

                Log.v("E-Value", "iconI is: " + iconI);
                Log.v("E-Value", "userNameI is: " + userNameI);
                Log.v("E-Value", "userIdI is: " + userIdI);
                Log.v("E-Value", "identityI is: " + identityI);
                Log.v("E-Value", "nameI is: " + nameI);
                Log.v("E-Value", "genderI is: " + genderI);
                Log.v("E-Value", "universityI is: " + universityI);
                Log.v("E-Value", "degreeI is: " + degreeI);
                Log.v("E-Value", "uidI is: " + uidI);
                Log.v("E-Value", "phoneI is: " + phoneI);
                Log.v("E-Value", "emailI is: " + emailI);
                Log.v("E-Value", "password is: " + passwordI);
                Log.v("E-Value", "password is: " + biographyI);

                iconId.setText(iconI);
                userName.setText(userNameI );
                userId.setText(userIdI);
                identity.setText(identityI);
                name.setText(nameI);
                gender.setText(genderI);
                university.setText(universityI);
                degree.setText(degreeI);
                uid.setText(uidI);
                phoneEdit.setText(phoneI);
                emailEdit.setText(emailI);
                password.setText(passwordI);
                biography.setText(biographyI);

                //StorageReference pathReference = mStorageRef.child("icon/"+iconI);
                StorageReference pathReference = mStorageRef.child("icon/3.jpg");
                //for loading poster
                Glide.with(EditProfileAsso.this).using(new FirebaseImageLoader()).load(pathReference).into(icon);

                //updated user information
                save.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String iconI = iconId.getText().toString();
                                String userNameI = userName.getText().toString();
                                String userIdI = userId.getText().toString();
                                String identityI = identity.getText().toString();
                                String nameI = name.getText().toString();
                                String genderI = gender.getText().toString();
                                String universityI = university.getText().toString();
                                String degreeI = degree.getText().toString();
                                String uidI = uid.getText().toString();
                                String tel_noUpdated = phoneEdit.getText().toString();
                                String emailUpdated = emailEdit.getText().toString();
                                String passwordI = password.getText().toString();
                                String biographyI = biography.getText().toString();

                                /*
                                String iconI = "ABC";
                                String userNameI = "Cher";
                                String userIdI = "123";
                                String identityI = "Ind";
                                String nameI = "Fofo";
                                String genderI = "F";
                                String universityI = "HKU";
                                String degreeI = "CS";
                                String uidI = "u33333";
                                String tel_noUpdated = "1234567";
                                String emailUpdated = "fofo@gmai.com";
                                String passwordI = "a1234";
                                */

                                Log.v("E-Value", "iconI is: " + iconI);
                                Log.v("E-Value", "userNameI is: " + userNameI);
                                Log.v("E-Value", "userIdI is: " + userIdI);
                                Log.v("E-Value", "identityI is: " + identityI);
                                Log.v("E-Value", "nameI is: " + nameI);
                                Log.v("E-Value", "genderI is: " + genderI);
                                Log.v("E-Value", "universityI is: " + universityI);
                                Log.v("E-Value", "degreeI is: " + degreeI);
                                Log.v("E-Value", "uidI is: " + uidI);
                                Log.v("E-Value", "phoneI is: " + tel_noUpdated);
                                Log.v("E-Value", "emailI is: " + emailUpdated);
                                Log.v("E-Value", "password is: " + passwordI);
                                Log.v("E-Value", "password is: " + biographyI);

                                EditProfileAsso.Users user = new EditProfileAsso.Users(iconI, userNameI, userIdI, identityI, nameI, genderI, universityI,
                                        degreeI, uidI, tel_noUpdated, emailUpdated, passwordI, biographyI);
                                testRef.child(key).setValue(user);

                                Toast.makeText(EditProfileAsso.this,"Updated",Toast.LENGTH_SHORT).show();

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

    public class Users{

        private String icon;
        private String username;
        private String userID;
        private String identity;
        private String name;
        private String gender;
        private String university;
        private String degree;
        private String uid;
        private String tel_no;
        private String email;
        private String password;
        private String biography;

        public Users(){

        }

        public Users (String icon, String username,String userID, String identity, String name, String gender, String university, String degree, String uid,
                      String tel_no, String email, String password, String biography){

            this.icon = icon;
            this.username = username;
            this.userID = userID;
            this.identity = identity;
            this.name = name;
            this.gender = gender;
            this.university = university;
            this.degree = degree;
            this.uid = uid;
            this.tel_no = tel_no;
            this.email = email;
            this.password = password;
            this.biography = biography;
        }

        public String getIcon(){return icon;}
        public String getUsername(){return username;}
        public String getUserID(){return userID;}
        public String getIdentity(){return identity;}
        public String getName(){return name;}
        public String getGender(){return gender;}
        public String getUniversity(){return university;}
        public String getDegree(){return degree;}
        public String getUid(){return uid;}
        public String getTel_no(){return tel_no;}
        public String getEmail(){return email;}
        public String getPassword(){return password;}
        public String getBiography(){return biography;}

    }

}
