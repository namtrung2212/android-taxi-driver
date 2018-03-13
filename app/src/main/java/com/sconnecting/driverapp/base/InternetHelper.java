package com.sconnecting.driverapp.base;

/**
 * Created by TrungDao on 7/31/16.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.base.DialogHelper;

public class InternetHelper {

    public static boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) AppDelegate.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public static boolean checkConnectingToInternet(){

        if (!isConnectingToInternet()) {
            DialogHelper.showAlertDialog(AppDelegate.getContext(), "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            return false;
        }
        return true;
    }
}
