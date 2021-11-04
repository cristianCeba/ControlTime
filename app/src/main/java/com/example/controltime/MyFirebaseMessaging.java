package com.example.controltime;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessaging extends FirebaseMessagingService{

    @Override
    public void onNewToken(@NonNull String s) {
        System.out.println(" toke -- > " + s);

        super.onNewToken(s);
    }

}


