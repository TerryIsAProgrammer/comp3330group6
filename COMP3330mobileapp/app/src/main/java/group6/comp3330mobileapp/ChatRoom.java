package group6.comp3330mobileapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

public class ChatRoom extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    FloatingActionButton fab;
    String username="fofo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                EditText inputit = (EditText) findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child("chatRoom").push().setValue(new ChatMessage(inputit.getText().toString(),username));
                inputit.setText("");

            }
        });

        //Load Content
        displayChatMessage();
    }

    private void displayChatMessage() {

        ListView listOfMessage = (ListView) findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.chat_room_row,FirebaseDatabase.getInstance().getReference().child("chatRoom")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get Reference to the views of chat_room_row.xml

                TextView messageText, messageUser, messageTime;
                messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));


            }
        };
        listOfMessage.setAdapter(adapter);
    }
}
