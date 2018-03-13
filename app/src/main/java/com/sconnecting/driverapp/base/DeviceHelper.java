package com.sconnecting.driverapp.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Display;

import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by TrungDao on 8/9/16.
 */

public class DeviceHelper {


    public static Boolean isAppInForeground() {

        class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

            @Override
            protected Boolean doInBackground(Context... params) {
                final Context context = params[0].getApplicationContext();
                return isAppOnForeground(context);
            }

            private boolean isAppOnForeground(Context context) {

                KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                if( myKM.inKeyguardRestrictedInputMode()) {
                    //it is locked
                    return false;
                }

                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                if (appProcesses == null) {
                    return false;
                }
                final String packageName = context.getPackageName();
                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                        return true;
                    }
                }
                return false;
            }
        }

        boolean isForeground = true;

        try {
            isForeground = new ForegroundCheckTask().execute(AppDelegate.getContext()).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return isForeground;

    }


    public static boolean isTablet(Context context) {

        double screenInches = 0;
        try {

            DisplayMetrics dm = context.getResources().getDisplayMetrics();

            float screenWidth  = dm.widthPixels / dm.xdpi;

            float screenHeight = dm.heightPixels / dm.ydpi;

            screenInches = Math.sqrt(Math.pow(screenWidth, 2) +

                    Math.pow(screenHeight, 2));

        } catch(Throwable t) {

        }

        return (screenInches >= 6);


    }


    public static  void WakeUpFromLockScreen() {

        PowerManager pm = (PowerManager) AppDelegate.getContext().getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();

        if(isScreenOn==false)
        {

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");

            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }
    }

    public static void playDefaultNotificationSound(){

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(AppDelegate.getContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void playNotificationSound(){

        MediaPlayer.create(AppDelegate.getContext(), R.raw.notifysound).start();
    }


}
