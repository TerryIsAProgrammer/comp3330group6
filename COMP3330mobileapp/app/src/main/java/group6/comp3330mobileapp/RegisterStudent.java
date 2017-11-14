package group6.comp3330mobileapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RegisterStudent extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.register_page_student);

            Button back = (Button) findViewById(R.id.back_button);

            back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), RegisterMainPage.class);
                    try {
                        startActivity(myIntent);
                    }
                    catch(android.content.ActivityNotFoundException e) {

                    }

                }
            });
        }
}
