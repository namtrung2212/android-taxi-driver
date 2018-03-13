package com.sconnecting.driverapp.ui.taxi.order.review;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.models.TravelOrder;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;

/**
 * Created by TrungDao on 8/6/16.
 */

public class ReviewView extends Fragment {


    public OrderScreen screen;
    public View view;

    public ReviewPanelView reviewPanelView;


    public TravelOrder CurrentOrder() {

        if( SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;

    }

    public ReviewView() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        screen = (OrderScreen) context;
        screen.mReviewView = this;

        reviewPanelView = new ReviewPanelView(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.taxi_order_review, container, false);

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

        reviewPanelView.initUI(listener);
    }

    public void invalidate(final Boolean isFirstTime,final Completion listener){

        this.invalidateUI(isFirstTime,listener);

    }

    public void invalidateUI(final Boolean isFirstTime,final Completion listener){

        reviewPanelView.invalidate(isFirstTime,listener);


    }

}
