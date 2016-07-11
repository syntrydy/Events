package com.it.mougang.gasmyr.events.Services;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseNotificationService extends FirebaseMessagingService {
    public MyFirebaseNotificationService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FIREBAESE",remoteMessage.getNotification().getBody());
    }
}
