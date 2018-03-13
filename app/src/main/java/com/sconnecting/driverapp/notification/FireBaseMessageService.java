package com.sconnecting.driverapp.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.base.DeviceHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by TrungDao on 12/2/16.
 */

public class FireBaseMessageService  extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sendNotification(remoteMessage);
    }



    private void sendNotification(RemoteMessage message) {

        NotificationHelper.ShowNotification(message,getApplicationContext());

        DeviceHelper.WakeUpFromLockScreen();

    }



}
