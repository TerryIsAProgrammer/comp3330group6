package group6.comp3330mobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

/**
 * Created by Crystal on 17/11/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mToggle;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                if (this instanceof Search) {
                    mDrawerLayout.closeDrawers();
                }else {
                    Intent myIntent = new Intent(this,Search.class);
                    startActivity(myIntent);
                    //finish();
                }
                break;
        }


        if (mToggle.onOptionsItemSelected(item))
        {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

        GlobalVariable gv = (GlobalVariable)getApplicationContext();
        String userIdentity = gv.getIdentity();

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_main:
                if (this instanceof HomePage){
                    Intent intent = getIntent();
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        if (extras.containsKey("searchResults")) {
                            Intent myIntent_main = new Intent(this, HomePage.class);
                            myIntent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent_main);
                            finish();
                        }
                    }else {
                        mDrawerLayout.closeDrawers();
                    }
                }else {
                    Intent myIntent_main = new Intent(this, HomePage.class);
                    myIntent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent_main);
                    finish();
                }
                break;
            case R.id.nav_search:
                if (this instanceof Search) {
                    mDrawerLayout.closeDrawers();
                }else {
                    Intent myIntent_search = new Intent(this, Search.class);
                    startActivity(myIntent_search);
                }
                //finish();
                break;
            case R.id.nav_profile:
                if (this instanceof ProfileAsso ||this instanceof ProfileInd) {
                    mDrawerLayout.closeDrawers();
                }else {
                    Intent myIntent_profile = null;
                    if (userIdentity.equals("S")) {
                        myIntent_profile = new Intent(this, ProfileInd.class);
                    } else if (userIdentity.equals("A")) {
                        myIntent_profile = new Intent(this, ProfileAsso.class);
                    }
                    startActivity(myIntent_profile);
                    //finish();
                }
                break;
            case R.id.nav_my_events:
                if (this instanceof MyEvents) {
                    mDrawerLayout.closeDrawers();
                }else {
                    Intent myIntent_viewEvent = new Intent(this, MyEvents.class);
                    startActivity(myIntent_viewEvent);
                    //finish();
                }
                break;
            case R.id.nav_chat:
                if (this instanceof ChatRoom) {
                    mDrawerLayout.closeDrawers();
                }else {
                    Intent myIntent_chat = new Intent(this, ChatRoom.class);
                    startActivity(myIntent_chat);
                    //finish();
                }
                break;
            case R.id.nav_log_out:
                Intent myIntent_logout = new Intent(this,MainActivity.class);
                myIntent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(myIntent_logout);
                finish();
                break;
        }
        //close navigation drawer
        mDrawerLayout.closeDrawers();
        return true;
    }

    protected void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //@Override
    //public void onBackPressed() {
    //    mDrawerLayout.openDrawer(GravityCompat.START);
    //}
}
