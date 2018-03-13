package com.sconnecting.driverapp.ui.taxi.search;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;

/**
 * Created by TrungDao on 10/14/16.
 */


public class LateOrderSearchScreen extends AppCompatActivity {

    public String Caller;

    public Toolbar mainToolbar;
    public IconTextView btnBack;
    public TextView mToolbarTitle;

    protected boolean useToolbar = true;
    public boolean isActive = false;

    public LateOrderSearchTable mLateOrderSearchTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.Caller = getIntent().getStringExtra("caller");

        setContentView(R.layout.taxi_late_search);

        initControls(new Completion() {
            @Override
            public void onCompleted() {


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppDelegate.CurrentActivity = this;

    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;

    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);


        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);

        if (useToolbar)
        {
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
        }
        else
        {
            mainToolbar.setVisibility(View.GONE);
        }

    }


    @Override

    public void onBackPressed() {

        // Write your code here

        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);


    }


    public void initControls(final Completion listener) {


        mLateOrderSearchTable = new LateOrderSearchTable((RecyclerView) findViewById(R.id.table),(SwipeRefreshLayout) findViewById(R.id.refreshControl));

        mLateOrderSearchTable.refreshDataList(listener);
    }




}