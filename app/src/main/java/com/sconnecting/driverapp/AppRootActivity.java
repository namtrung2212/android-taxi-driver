package com.sconnecting.driverapp;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sconnecting.driverapp.manager.DriverManager;
import com.sconnecting.driverapp.notification.NotificationHelper;
import com.sconnecting.driverapp.ui.activation.ActivationScreen;
import com.sconnecting.driverapp.base.listener.GetBoolValueListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;

import org.parceler.Parcels;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AppRootActivity extends AppCompatActivity {

    Activity current;
    TextView lblStatus;
    Button btnTryAgain;

    @Override
    public void onAttachedToWindow() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        current = this;

        AppDelegate.CurrentActivity = this;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            System.out.println("*** My thread is now configured to allow connection");
        }

        setContentView(R.layout.app_root);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(89,145,196));
        }

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.INTERNET,android.Manifest.permission.ACCESS_NETWORK_STATE},2212);

            return;
        }

        StartInstance();

    }


    @Override
    public void setContentView(int layoutResID) {

        super.setContentView(layoutResID);


        lblStatus = (TextView) findViewById(R.id.lblStatus);

        btnTryAgain =(Button) findViewById(R.id.btnTryAgain);
        AnimationHelper.setOnClick(btnTryAgain, new Completion() {
            @Override
            public void onCompleted() {

                        btnTryAgain.setVisibility(View.GONE);
                        lblStatus.setText("Đang kết nối...");
                        StartInstance();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        AppDelegate.CurrentActivity = this;


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        StartInstance();
    }


    public void StartInstance(){

        TryToConnectToServer(new GetBoolValueListener() {
            @Override
            public void onCompleted(Boolean success, Boolean value) {

               if(value) {

                   NotificationHelper.TryToForwardFromNotification(getIntent(), current, new GetBoolValueListener() {
                       @Override
                       public void onCompleted(Boolean success, Boolean value) {

                           if (value == false) {

                               SCONNECTING.orderManager.resetToLastOpenningOrder(null);

                           }
                       }
                   });

               }

            }
        });
    }


    public void TryToConnectToServer(final GetBoolValueListener listener){

        if(SCONNECTING.orderScreen != null) {
            if(listener != null)
                listener.onCompleted(true,true);
            return;
        }

        ServerStorage.TestConnection(new GetBoolValueListener() {
            @Override
            public void onCompleted(Boolean success, Boolean value) {

                if (!success || !value) {

                    lblStatus.setVisibility(View.VISIBLE);
                    btnTryAgain.setVisibility(View.VISIBLE);
                    lblStatus.setText("Kết nối không thành công. Vui lòng kiểm tra lại.");

                    if(listener != null)
                        listener.onCompleted(false,false);

                } else {

                    SCONNECTING.Init(new Completion() {
                        @Override
                        public void onCompleted() {

                            DriverManager.isValidDevice(new GetBoolValueListener() {
                                @Override
                                public void onCompleted(Boolean success,Boolean isValid) {

                                    if(isValid == false){

                                        Intent intent = new Intent(getApplicationContext(), ActivationScreen.class);
                                        startActivity(intent);

                                        if(listener != null)
                                            listener.onCompleted(true,false);

                                    }else{

                                        SCONNECTING.driverManager.initCurrentDriver(new GetBoolValueListener() {
                                            @Override
                                            public void onCompleted(Boolean success, Boolean isValid) {

                                                if(!isValid){

                                                    Intent intent = new Intent(getApplicationContext(), ActivationScreen.class);
                                                    startActivity(intent);

                                                    if(listener != null)
                                                        listener.onCompleted(true,false);

                                                }else{

                                                    SCONNECTING.Start(new Completion() {
                                                        @Override
                                                        public void onCompleted() {

                                                            if(listener != null)
                                                                listener.onCompleted(true,true);

                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });



                }
            }
        });
    }

}
