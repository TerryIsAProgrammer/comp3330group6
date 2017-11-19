package group6.comp3330mobileapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Cher on 19/11/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String CHANNEL_ID = "my_channel_01";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationChannel mChannel = new NotificationChannel("my_channel_01", "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
        mChannel.setDescription("Channel description");
        Notification.Builder notification = new Notification.Builder(this)
                .setContentTitle("Content Title: FCM notification")
                .setContentText(remoteMessage.getNotification().getBody())
                .setTicker("This is the ticker")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(mChannel);
        notificationManager.notify(0,notification.build());


    }
}
