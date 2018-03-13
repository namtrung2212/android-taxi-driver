package com.sconnecting.driverapp.ui.leftmenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sconnecting.driverapp.base.BaseActivity;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;
import com.sconnecting.driverapp.base.ImageHelper;
import com.sconnecting.driverapp.base.listener.Completion;

/**
 * Created by TrungDao on 8/1/16.
 */

public class LeftMenuView extends Fragment {

    View view;

    BaseActivity parent;
    LeftMenuList menuList;

    ImageView imgAvatar;
    TextView lblDriverName;


    public LeftMenuView() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parent = (BaseActivity) context;
        parent.mLeftMenuView = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.base_leftmenu, container, false);

        initControls(new Completion(){

            @Override
            public void onCompleted() {

            }
        });
        return view;
    }


    public void initControls(final Completion listener) {

        view.setVisibility(View.VISIBLE);


        lblDriverName = (TextView) view.findViewById(R.id.lblDriverName);
        imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);

        if(SCONNECTING.driverManager != null && SCONNECTING.driverManager.CurrentDriver != null && SCONNECTING.driverManager.CurrentDriver.id != null){

            String url = ServerStorage.ServerURL + "/avatar/driver/" + SCONNECTING.driverManager.CurrentDriver.id;
            ImageHelper.loadImage(parent, url, R.drawable.avatar, 250, 250, imgAvatar,null);

        }

        if(SCONNECTING.driverManager != null && SCONNECTING.driverManager.CurrentDriver != null && SCONNECTING.driverManager.CurrentDriver.Name != null){
            lblDriverName.setText(SCONNECTING.driverManager.CurrentDriver.Name.toUpperCase());
        }


        menuList = new LeftMenuList((RecyclerView) view.findViewById(R.id.mainMenu));


        if(listener != null)
            listener.onCompleted();

    }


    public void reloadMenu( ) {

        menuList.reloadData();
    }


}

