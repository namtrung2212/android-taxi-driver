package com.sconnecting.driverapp.ui.taxi.search.lateorder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.models.TravelOrder;

import org.parceler.Parcels;

/**
 * Created by TrungDao on 10/17/16.
 */

public class LateOrderScreen extends AppCompatActivity  implements  ActivityCompat.OnRequestPermissionsResultCallback{

    public Toolbar mainToolbar;
    public IconTextView btnBack;
    public TextView mToolbarTitle;

    public LateOrderMap mMapView;
    public LateOrderPanelView mLateOrderPanelView;
    public TravelOrder currentOrder;


    public interface OnMapReadyListener {
        void onReady();
    }

    public OnMapReadyListener mapReadyListener;

    @Override
    protected void onResume() {
        super.onResume();

        AppDelegate.CurrentActivity = this;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taxi_late_order);

        initControls(new Completion() {
            @Override
            public void onCompleted() {


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        Intent myIntent = getIntent();
        currentOrder = Parcels.unwrap(myIntent.getParcelableExtra("Order"));

        mapReadyListener = new OnMapReadyListener() {
            @Override
            public void onReady() {


                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            invalidateUI(true, null);

                        }
                    }, 1000 * 1);


            }
        };



    }


    public void initControls(final Completion listener) {

        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);


        setSupportActionBar(mainToolbar);
        mainToolbar.setTitleTextColor(Color.WHITE);
        mainToolbar.setVisibility(View.VISIBLE);

        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(this.getTitle());


        btnBack = (IconTextView) findViewById(R.id.btnBack);
        btnBack.setTextColor(Color.WHITE);
        AnimationHelper.setOnClick(btnBack, new Completion() {
            @Override
            public void onCompleted() {

                onBackPressed();
            }
        });

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(89,145,196));
        }


        if(listener != null)
            listener.onCompleted();
    }



    @Override

    public void onBackPressed() {

        // Write your code here

        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);


    }

    public void invalidateUI(final Boolean isFirstTime, final Completion listener) {


            mMapView.invalidate(isFirstTime, new Completion() {
                @Override
                public void onCompleted() {

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mLateOrderPanelView.invalidate(isFirstTime, listener);

                        }
                    }, 1000 * 1);

                }
            });


    }


}