package com.sconnecting.driverapp.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.manager.DriverManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * Created by TrungDao on 12/2/16.
 */
public class FirebaseInstanceService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {

        SharedPreferences preferences = AppDelegate.getContext().getSharedPreferences("SCONNECTING", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("DriverFCMToken", token);
        editor.commit();

        String driverId = DriverManager.DefaultDriverID();
        if(driverId != null && driverId.isEmpty() == false){

            String url = AppDelegate.ServerURL+ "/FCM/updateToken?driverId="+ driverId + "&FCMToken="+token;
            new AsyncHttpClient().get(url,new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                }


            });
        }
    }
}
