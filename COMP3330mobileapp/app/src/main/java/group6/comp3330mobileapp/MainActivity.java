package group6.comp3330mobileapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;
    private EditText inputName;
    private EditText inputPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //(R.id.text_username);
        inputName =(EditText)findViewById(R.id.text_username);
        inputPw =(EditText) findViewById(R.id.text_password);

        Button register = (Button) findViewById(R.id.register_button);
        Button login = (Button) findViewById(R.id.login_button);


        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterMainPage.class);
                try {
                    startActivity(myIntent);
                }
                catch(android.content.ActivityNotFoundException e) {
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                submitPost(view);
                //Intent myIntent = new Intent(view.getContext(), RegisterMainPage.class);
                //startActivity(myIntent);
            }
        });
    }

    private void submitPost(final View view) {
        String userName = inputName.getText().toString();
        String passWord = inputPw.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(userName)) {
//            inputName.setError(REQUIRED);
        }

        // Body is required
        if (TextUtils.isEmpty(passWord)) {
//            inputPw.setError(REQUIRED);
        }
        Query query = mDatabase.child("users").orderByChild("username").equalTo(userName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passWord = inputPw.getText().toString();
                    for (DataSnapshot thisSnapshot: dataSnapshot.getChildren()) {
                        String dbPassword = (String)thisSnapshot.child("password").getValue();

                        if (passWord.length()==dbPassword.length()){

                            int index = passWord.indexOf(dbPassword);
                            if (index == 0 ){

                                //Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();
                                int userKey = Integer.parseInt(thisSnapshot.getKey());
                                String dbIdentity = (String)thisSnapshot.child("identity").getValue();

                                GlobalVariable gv = (GlobalVariable)getApplicationContext();
                                gv.setUserID(userKey);
                                gv.setIdentity(dbIdentity);

                                Intent myIntent = new Intent(view.getContext(), HomePage.class);
                                myIntent.putExtra("userKey", userKey);
                                try {
                                    startActivity(myIntent);
                                    finish();
                                }
                                catch(android.content.ActivityNotFoundException e) {
                                }

                            }else{
                                Toast.makeText(MainActivity.this,"Index Error" + index , Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(MainActivity.this,"Different Len" , Toast.LENGTH_SHORT).show();
                        }

                    }

                }else{
                    Toast.makeText(MainActivity.this, "Wrong Username/Password",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
