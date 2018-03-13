package com.sconnecting.driverapp.ui.taxi.order.monitoring.message;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;
import com.sconnecting.driverapp.base.ImageHelper;
import com.squareup.picasso.Callback;

import static android.view.View.LAYOUT_DIRECTION_LTR;
import static android.view.View.LAYOUT_DIRECTION_RTL;

/**
 * Created by TrungDao on 8/26/16.
 */

public class TravelChattingCell extends RecyclerView.ViewHolder {

    public View cellView;
    public CircularImageView imgAvatar;
    public TextView lblContent;
    public LinearLayout linearlayout;
    public View backgroundPanel;


    public TravelChattingCell(View view) {
        super(view);


        cellView = view;

        linearlayout = (LinearLayout) view.findViewById(R.id.linearlayout);
        backgroundPanel =  view.findViewById(R.id.backgroundPanel);

        imgAvatar = (CircularImageView) view.findViewById(R.id.imgAvatar);
        imgAvatar.setVisibility(View.INVISIBLE);
        lblContent = (TextView) view.findViewById(R.id.lblContent);

    }


    public void updateWithModel(TravelChattingObject obj) {


        this.updateAvatar(obj);

        this.updateContent(obj);

        GradientDrawable draw = (GradientDrawable) backgroundPanel.getBackground();
        draw.setColor(Color.parseColor(obj.cellObject.IsUser == 1 ? "#F7F7F7" : "#7DBBE9"));
        draw.setStroke(1 , Color.parseColor(obj.cellObject.IsUser == 1 ? "#DADAD9" : "#7DBBE9"));

        linearlayout.setLayoutDirection(obj.cellObject.IsUser == 1 ? LAYOUT_DIRECTION_LTR : LAYOUT_DIRECTION_RTL);

    }


    public void updateAvatar(TravelChattingObject obj ){

        String url = null;

        if(obj.cellObject.IsUser == 1)
            url = obj.order.User != null ? ServerStorage.ServerURL + "/avatar/user/" + obj.order.User  : "";
        else
            url = obj.order.Driver != null ? ServerStorage.ServerURL + "/avatar/driver/" + obj.order.Driver  : "";

        ImageHelper.loadImage(cellView.getContext(), url, R.drawable.avatar, 100, 100, imgAvatar, new Callback() {
            @Override
            public void onSuccess() {

                imgAvatar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError() {

                imgAvatar.setVisibility(View.VISIBLE);

            }
        });


    }


    public void updateContent(TravelChattingObject obj){

        lblContent.setText(obj.cellObject.Content);

    }



}