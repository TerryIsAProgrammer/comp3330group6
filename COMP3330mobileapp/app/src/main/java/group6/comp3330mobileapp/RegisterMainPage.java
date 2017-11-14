package group6.comp3330mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Terry on 11/6/2017.
 */

public class RegisterMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_main);

        Button student = (Button) findViewById(R.id.student_button);
        Button association = (Button) findViewById(R.id.association_button);


        student.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterStudent.class);
                try {
                    startActivity(myIntent);
                }
                catch(android.content.ActivityNotFoundException e) {

                }

            }
        });

        association.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterAssociation.class);
                try {
                    startActivity(myIntent);
                }
                catch(android.content.ActivityNotFoundException e) {

                }

            }
        });
    }
}


