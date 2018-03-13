package com.sconnecting.driverapp.ui.leftmenu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;

/**
 * Created by TrungDao on 8/16/16.
 */


public class LeftMenuCellItem extends LeftMenuCell {

    public View cellView;

    public TextView leftIcon;
    public TextView lblTitle;
    public TextView rightIcon;
    public ImageView bottomLine;



    public LeftMenuCellItem(View view) {
        super(view);

        cellView = view;
        leftIcon = (TextView) view.findViewById(R.id.leftIcon);
        lblTitle = (TextView) view.findViewById(R.id.lblTitle);
        rightIcon = (TextView) view.findViewById(R.id.rightIcon);
        bottomLine = (ImageView) view.findViewById(R.id.bottomLine);

    }


    @Override
    public void bind(final LeftMenuObject item, final OnItemClickListener listener) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        listener.onItemClick(item);
                    }
                });
            }
        });
    }


    @Override
    public  void updateWithModel(LeftMenuObject item) {


        lblTitle.setText(item.title.toUpperCase());

        if(item.leftIcon == null){

            leftIcon.setVisibility(View.GONE);

        }else {

            leftIcon.setVisibility(View.VISIBLE);

            if (item.leftIcon.equals("Home")) {
                leftIcon.setText("{fa-home}");

            } else if (item.leftIcon.equals("NotYetPickup")) {
                leftIcon.setText("{fa-street-view}");

            } else if (item.leftIcon.equals("OnTheWay")) {
                leftIcon.setText("{fa-random}");

            } else if (item.leftIcon.equals("NotYetPaid")) {
                leftIcon.setText("{fa-credit-card}");


            } else if (item.leftIcon.equals("History")) {
                leftIcon.setText("{fa-history}");

            } else if (item.leftIcon.equals("Notification")) {
                leftIcon.setText("{fa-info-circle}");

            } else if (item.leftIcon.equals("LateOrderSearch")) {
                leftIcon.setText("{fa-calendar}");

            } else if (item.leftIcon.equals("RequestedLateOrders")) {
                leftIcon.setText("{fa-paper-plane-o}");

            } else if (item.leftIcon.equals("calendar")) {
                leftIcon.setText("{fa-calendar}");

            } else if (item.leftIcon.equals("ExchangeDrivers")) {
                leftIcon.setText("{fa-exchange}");

            } else if (item.leftIcon.equals("Support")) {
                leftIcon.setText("{fa-envelope-o}");

            }


        }

        bottomLine.setVisibility(item.isLastItemInSection() ? View.GONE : View.VISIBLE);

    }


}