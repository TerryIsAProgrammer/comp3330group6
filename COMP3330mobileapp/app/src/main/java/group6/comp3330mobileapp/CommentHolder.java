package group6.comp3330mobileapp;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Cher on 20/11/2017.
 */

public class CommentHolder {
    TextView userName;
    TextView comment;

    public  CommentHolder(View itemView){

        userName = (TextView) itemView.findViewById(R.id.userName);
        comment = (TextView) itemView.findViewById(R.id.comment);
    }
}
