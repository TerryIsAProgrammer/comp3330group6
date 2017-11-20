package group6.comp3330mobileapp;

/**
 * Created by Cher on 20/11/2017.
 */

public class CommentItem {

    String username;
    String comment;

    public CommentItem() {
    }

    public CommentItem(String username, String comment){
        this.username = username;
        this.comment = comment;
    }

    public String getUsername(){return username;}
    public String getComment(){return comment;}
    public void setUsername(String username){this.username = username;}
    public void setComment(String comment){this.comment = comment;}

}
