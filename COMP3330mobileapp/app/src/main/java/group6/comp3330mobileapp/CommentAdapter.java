package group6.comp3330mobileapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Cher on 20/11/2017.
 */

public class CommentAdapter extends ArrayAdapter<CommentItem> {

    LayoutInflater inflater;

    Context context;
    int layoutResourceID;
    List<CommentItem> commentItems = null;


    public CommentAdapter(Context context, int resource, List<CommentItem> commentItems){
        super(context, resource, commentItems);

        this.context = context;
        this.layoutResourceID = resource;
        this.commentItems = commentItems;
    }

    static class CommentHolder{
        TextView userName;
        TextView comment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommentHolder holder = null;

        if(convertView == null){
            //LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            //convertView = inflater.inflate(layoutResourceID,parent,false);

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.comment_list_row, null);

            holder = new CommentHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.userName);
            holder.comment = (TextView) convertView.findViewById(R.id.comment);

            convertView.setTag(holder);

        }else {
            holder = (CommentAdapter.CommentHolder)convertView.getTag();
        }

        CommentItem commentItem = commentItems.get(position);
        holder.userName.setText(commentItem.username);
        holder.comment.setText(commentItem.comment);

        return convertView;


    }






}
