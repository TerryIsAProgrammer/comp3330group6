package group6.comp3330mobileapp;
import android.app.Application;

public class GlobalVariable extends Application {

    private int userID;
    private String identity;
    private String uni;
    private String userName;
<<<<<<< HEAD
=======

>>>>>>> 0621021c837e333f2dd2adea44082c80b0c292ae
    //Set variable
    public void setUserID(int userID){
        this.userID=userID;
    }
    public void setIdentity(String identity){
        this.identity=identity;
    }
    public void setUni(String uni){this.uni=uni;}
<<<<<<< HEAD
    public void setUserName(String userName){this.userName = userName;}
=======
    public void setUserName(String userName){this.userName=userName;}
>>>>>>> 0621021c837e333f2dd2adea44082c80b0c292ae

    //Get Variable
    public int getUserID(){
        return userID;
    }
    public String getIdentity(){return identity;}
    public String getUni(){return uni;}
    public String getUserName(){return userName;}
}