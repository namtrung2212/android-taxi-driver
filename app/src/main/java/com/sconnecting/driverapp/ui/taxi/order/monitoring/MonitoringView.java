package com.sconnecting.driverapp.ui.taxi.order.monitoring;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;
import com.sconnecting.driverapp.data.models.TravelOrder;
import com.sconnecting.driverapp.data.models.User;
/**
 * Created by TrungDao on 8/6/16.
 */

public class MonitoringView extends Fragment {


    public OrderScreen screen;
    public View view;

    public UserProfileView userProfileView;
    public OrderPanelView orderPanelView;


    public TravelOrder CurrentOrder() {

        if( SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;

    }

    public MonitoringView() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        screen = (OrderScreen) context;
        screen.mMonitoringView = this;

        orderPanelView = new OrderPanelView(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.taxi_order_monitoring, container, false);

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

        orderPanelView.initUI(listener);
    }

    public void invalidate(final Boolean isFirstTime,final Completion listener){

        if(this.CurrentOrder() != null && this.CurrentOrder().User != null && ( this.userProfileView.user == null || this.userProfileView.user.id == null || this.userProfileView.user.id.equals(this.CurrentOrder().User) == false)){

           new BaseController<>(User.class).getById(false, this.CurrentOrder().User, new GetOneListener() {
               @Override
               public void onGetOne(Boolean success,BaseModel item) {

                   if(success)
                       userProfileView.user = (User)item;

                   invalidateUI(isFirstTime,listener);
               }
           });

        }else{

            this.invalidateUI(isFirstTime,listener);

        }



    }

    public void invalidateUI(final Boolean isFirstTime,final Completion listener){

        orderPanelView.invalidate(isFirstTime,new Completion() {
            @Override
            public void onCompleted() {

                userProfileView.invalidate(isFirstTime,new Completion() {
                    @Override
                    public void onCompleted() {

                        userProfileView.invalidateAvatar(isFirstTime,listener);
                    }
                });

            }
        });


    }

}
