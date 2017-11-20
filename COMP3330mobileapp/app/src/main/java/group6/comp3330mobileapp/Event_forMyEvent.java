package group6.comp3330mobileapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Crystal on 17/11/2017.
 */

public class Event_forMyEvent implements Comparable<Event_forMyEvent>{
    private String picURL;
    private String title;
    private String time;
    private String date;
    private String timeStamp;

    private String eventID;

    Event_forMyEvent(String picURL, String title, String date, String time, String eventID,String timeStamp){
        this.picURL = picURL;
        this.title = title;
        this.time = time;
        this.eventID = eventID;
        this.date = date;
        this.timeStamp = timeStamp;
    }

    public int compareTo(Event_forMyEvent e) {
        if (Long.valueOf(timeStamp).longValue() < Long.valueOf(e.getTimeStamp()).longValue()){
            return 1;
        }
        return 0;
    }


    public String getPicURL(){
        return picURL;
    }

    public String getTitle(){
        return title;
    }

    public String getTime(){
        return time;
    }

    public String getEventID(){
        return eventID;
    }

    public String getDate(){
        return date;
    }

    public String getTimeStamp(){
        return timeStamp;
    }
}
