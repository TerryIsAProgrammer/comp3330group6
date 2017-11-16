package group6.comp3330mobileapp;
import android.app.Application;

public class GlobalVariable extends Application {

    private int userID;
    private String identity;

    public void setUserID(int userID){
        this.userID=userID;
    }

    public void setIdentity(String identity){
        this.identity=identity;
    }

    public int getUserID(){
        return userID;
    }

    public String getIdentity(){return identity;}
}