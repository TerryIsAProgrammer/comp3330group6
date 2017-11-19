package group6.comp3330mobileapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Cher on 19/11/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh(){

        String recent_token = FirebaseInstanceId.getInstance().getToken();

        //Delete the app and install it again, then u can get the token
        Log.d(REG_TOKEN, recent_token);

    }
}
