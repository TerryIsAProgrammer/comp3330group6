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

public class Comment extends BaseActivity {

    ImageView poster;
    TextView userName;
    TextView eventID;
    TextView eventName;
    EditText comment;
    Button post;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference testRef = myRef.child("comment");
    String userID = "003";
    String eventKey = "004";
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);

        poster = (ImageView) findViewById(R.id.poster);
        userName = (TextView) findViewById(R.id.userName);
        eventID = (TextView) findViewById(R.id.eventID);
        eventName = (TextView) findViewById(R.id.eventName);
        comment = (EditText) findViewById(R.id.comment);
        post = (Button) findViewById(R.id.post);

        //for loading event inforamtion
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String eventIDC = dataSnapshot.child("events").child(eventKey).child("eventID").getValue().toString();
                String eventNameC = dataSnapshot.child("events").child(eventKey).child("event_name").getValue().toString();
                String userNameC = dataSnapshot.child("users").child(userID).child("username").getValue().toString();

                Log.v("E-Value", "eventIDC is: " + eventIDC);
                Log.v("E-Value", "eventNameC is: " + eventNameC);
                Log.v("E-Value", "eventNameC is: " + userNameC);

                eventID.setText(eventIDC);
                eventName.setText(eventNameC);
                userName.setText(userNameC);

                //StorageReference pathReference = mStorageRef.child("icon/"+iconI);
                StorageReference pathReference = mStorageRef.child("eventPoster/"+eventNameC);
                //for loading poster
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(pathReference).into(poster);

                post.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                        String eventIDC = eventID.getText().toString();
                        String eventNameC = eventName.getText().toString();
                        String userNameC = userName.getText().toString();
                        String commentC = comment.getText().toString();

                        Log.v("E-Value", "iconI is: " + eventIDC);
                        Log.v("E-Value", "iconI is: " + eventNameC);
                        Log.v("E-Value", "userNameI is: " + userID);
                        Log.v("E-Value", "userNameI is: " + userNameC);
                        Log.v("E-Value", "userIdI is: " + commentC);

                        Comment.EventComment comment = new Comment.EventComment(eventIDC, eventNameC, userID, userNameC, commentC);

                        testRef.child(eventKey).child(userID).setValue(comment);

                        Toast.makeText(Comment.this,"Posted",Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class EventComment{

        private String eventID;
        private String event_name;
        private String userID;
        private String username;
        private String comment;

        public EventComment(){

        }

        public EventComment (String eventID, String event_name, String userID, String username, String comment){

            this.eventID = eventID;
            this.event_name = event_name;
            this.userID = userID;
            this.username = username;
            this.comment = comment;
        }

        public String getEventID(){return eventID;}
        public String getEvent_name(){return event_name;}
        public String getUserID(){return userID;}
        public String getUsername(){return username;}
        public String getComment(){return comment;}

    }
}
