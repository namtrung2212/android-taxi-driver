package com.sconnecting.driverapp.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.ui.leftmenu.LeftMenuView;

public class BaseActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    public Toolbar mainToolbar;
    public TextView mToolbarTitle;
    public LeftMenuView mLeftMenuView;

    protected boolean useToolbar = true;
    public boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID)
    {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.base_activity, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);


        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);

        if (useToolbar)
        {
            setSupportActionBar(mainToolbar);
            mainToolbar.setTitleTextColor(Color.WHITE);
            mainToolbar.setVisibility(View.VISIBLE);

            mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
            mToolbarTitle.setText(this.getTitle());


            IconTextView btnHome = (IconTextView) findViewById(R.id.btnHome);
            btnHome.setTextColor(Color.WHITE);
            AnimationHelper.setOnClick(btnHome, new Completion() {
                @Override
                public void onCompleted() {

                    showLeftMenu(true);
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

        initLeftMenu();
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
    protected void onResume() {
        super.onResume();
        AppDelegate.CurrentActivity = this;
    }

    private void initLeftMenu() {

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_container);

        navigationView = (NavigationView) findViewById(R.id.navigationView);

        mLeftMenuView.reloadMenu();
/*
        initLeftMenuHeader();

        navigationView.setNavigationItemSelectedListener(new  NavigationView.OnNavigationItemSelectedListener(){

            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                 if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });
        */
    }

    private void initLeftMenuHeader(){
/*
*/
    }

    public void showToolbar(boolean show) {

        if(mainToolbar != null)
            mainToolbar.setVisibility(show ? View.VISIBLE : View.GONE);

    }


    public void showLeftMenu(boolean show){

        if(drawerLayout != null) {
            if(show){
                drawerLayout.openDrawer(GravityCompat.START);
            }else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }
    }
}


