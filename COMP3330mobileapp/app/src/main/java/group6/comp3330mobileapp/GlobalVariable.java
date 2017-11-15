package group6.comp3330mobileapp;
import android.app.Application;

public class GlobalVariable extends Application {

    private int userID;

    public void setUserID(int userID){
        this.userID=userID;
    }

    public int getUserID(){
        return userID;
    }
}