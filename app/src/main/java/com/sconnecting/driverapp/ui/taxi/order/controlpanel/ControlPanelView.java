package com.sconnecting.driverapp.ui.taxi.order.controlpanel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;
import com.sconnecting.driverapp.data.models.DriverStatus;
import com.sconnecting.driverapp.data.models.TravelOrder;

/**
 * Created by TrungDao on 9/19/16.
 */

public class ControlPanelView extends Fragment {


    public OrderScreen screen;
    public View view;

    public View pnlControlArea;
    public Button btnReady;

    public TravelOrder CurrentOrder() {

        if( SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;

    }

    public DriverStatus DriverStatus() {

        return SCONNECTING.driverManager.CurrentDriverStatus;

    }


    public ControlPanelView() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        screen = (OrderScreen) context;
        screen.mControlPanelView = this;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.taxi_order_controlpanel, container, false);

        initControls(new Completion(){

            @Override
            public void onCompleted() {

                invalidate(true,null);
            }
        });
        return view;
    }


    @Override
    public void onResume(){
        super.onResume();

        invalidate(false,null);

    }

    public void initControls(final Completion listener) {

        pnlControlArea = (View) view.findViewById(R.id.pnlControlArea);
        btnReady = (Button) pnlControlArea.findViewById(R.id.btnReady);

        AnimationHelper.setOnClick(btnReady, new Completion() {
            @Override
            public void onCompleted() {

                SCONNECTING.driverManager.changeReadyStatus(new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {

                         invalidateReadyButton(null);

                    }
                });

            }
        });
    }

    public void invalidate(final Boolean isFirstTime,final Completion listener){


        if(isFirstTime){

            SCONNECTING.driverManager.invalidateStatus(new Completion() {
                @Override
                public void onCompleted() {
                    invalidateUI(isFirstTime,listener);
                }
            });
        }else {

            invalidateUI(isFirstTime,listener);
        }

    }

    public void invalidateUI(final Boolean isFirstTime,final Completion listener){


        final Boolean isShow= (CurrentOrder() == null );

        if(isShow){

            invalidateReadyButton(new Completion() {
                @Override
                public void onCompleted() {

                    pnlControlArea.setVisibility(isShow ? View.VISIBLE : View.GONE);

                    if(listener != null)
                        listener.onCompleted();


                }
            });

        }else{

            pnlControlArea.setVisibility(View.GONE);

            if(listener != null)
                listener.onCompleted();
        }

    }



    public void invalidateReadyButton(final Completion listener){

        if(btnReady != null){

            btnReady.setVisibility(DriverStatus() != null && DriverStatus().Vehicle != null ? View.VISIBLE : View.GONE);
            btnReady.setText(DriverStatus() != null && DriverStatus().IsReady == 1 ? "ĐANG RÃNH" : "ĐANG BẬN" );
            btnReady.setBackground(getResources().getDrawable(DriverStatus() != null && DriverStatus().IsReady == 1 ? R.drawable.button_style1_blue : R.drawable.button_style1_orrange));

        }
        if(listener != null)
            listener.onCompleted();
    }

}