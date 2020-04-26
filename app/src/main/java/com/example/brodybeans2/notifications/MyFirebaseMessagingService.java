package com.example.brodybeans2.notifications;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //private String token;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("TAG", "The token refreshed:" + s);
        //this.token = s;
    }

    /*
    public String getToken () {
        return this.token;
    }

     */



}
