package group6.comp3330mobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentList extends AppCompatActivity {

    List<CommentItem> lstComment;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    CommentAdapter adapter;
    ListView listView;

    String key = "004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);

        lstComment = new ArrayList<>();
        listView = (ListView) findViewById(R.id.commentListview);
        listView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot user1 : dataSnapshot.child("comment").child(key).getChildren()){

                    Log.v("E-Value", "user1 is: " + user1);

                    String username = user1.child("username").getValue().toString();
                    String comment = user1.child("comment").getValue().toString();

                    lstComment.add(new CommentItem(username,comment));

                }

                Log.v("E-Value", "size is: " + lstComment.size());
                if(lstComment.size()>0){
                    adapter = new CommentAdapter(CommentList.this, R.layout.comment_list_row, lstComment);
                    listView.setAdapter((ListAdapter) adapter);
                }else{
                    Toast.makeText(CommentList.this,"No data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
