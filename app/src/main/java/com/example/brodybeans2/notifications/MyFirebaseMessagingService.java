package com.example.brodybeans2.notifications;

import android.app.Notification;
import android.util.Log;

import com.example.brodybeans2.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //private String token;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("TAG", "The token refreshed:" + s);
        //this.token = s;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(123, notification);
    }

    /*
    public String getToken () {
        return this.token;
    }

     */



}
