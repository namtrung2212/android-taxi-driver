package com.sconnecting.driverapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.sconnecting.driverapp.google.GooglePlaceSearcher;
import com.sconnecting.driverapp.notification.ChatSocket;
import com.sconnecting.driverapp.notification.TaxiSocket;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;

import java.util.Locale;

/**
 * Created by TrungDao on 7/28/16.
 */

public class AppDelegate extends android.support.multidex.MultiDexApplication {

    public static String ServerURL =  "http://192.168.1.34:8000";
    public static String ChatSocketURL =  "http://192.168.1.34:4060";
    public static String TaxiNofifySocketURL =  "http://192.168.1.34:4050";
    public static String GoogleMapKey = "AIzaSyAusUO0JarHwoFFGeqVfOPHUEADDZQyTLs";  // no need
    public static String GooglePlaceKey = "AIzaSyBArZgCF0ZcAyHsIqVXbnVg-LbT-ySi6L0";

    private static Application sApplication;

    public static Activity CurrentActivity;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        Iconify.with(new FontAwesomeModule());

        Locale locale = new Locale("vi_VN");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);


        ServerStorage.ServerURL = AppDelegate.ServerURL;
        TaxiSocket.ServerURL = AppDelegate.TaxiNofifySocketURL;
        ChatSocket.ServerURL = AppDelegate.ChatSocketURL;
        GooglePlaceSearcher.placeKey =  AppDelegate.GooglePlaceKey;
    }


}