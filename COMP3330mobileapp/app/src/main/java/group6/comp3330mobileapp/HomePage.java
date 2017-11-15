package group6.comp3330mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Terry on 11/6/2017.
 */

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private DatabaseReference mDatabase;
    TextView title ;
    private int userKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        setNavigationViewListener();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close );
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        title = (TextView) findViewById(R.id.TitleView);

        Bundle extras = getIntent().getExtras();
        userKey = extras.getInt("userKey");

        Query query = mDatabase.child("users").orderByChild("userID").equalTo(userKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot thisSnapshot: dataSnapshot.getChildren()) {
                        String username = (String)thisSnapshot.child("username").getValue();
                        String identity = (String)thisSnapshot.child("identity").getValue();
                        title.setText("Username: " + username + " Identity:" + identity);
                    }

                }else{
                    title.setText("Wrong query la dllm");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Intent myIntent = new Intent(this,Search.class);
                startActivity(myIntent);
                break;
        }


        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_main:
                Intent myIntent_main = new Intent(this,HomePage.class);
                startActivity(myIntent_main);
                break;
            case R.id.nav_search:
                Intent myIntent_search = new Intent(this,Search.class);
                startActivity(myIntent_search);
                break;
            case R.id.nav_log_out:
                Intent myIntent_logout = new Intent(this,MainActivity.class);
                startActivity(myIntent_logout);
                break;
        }
        //close navigation drawer
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
