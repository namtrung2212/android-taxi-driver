package com.sconnecting.driverapp.ui.taxi.order.monitoring;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetBoolValueListener;
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.ImageHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.controllers.TravelOrderController;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;
import com.sconnecting.driverapp.ui.taxi.order.monitoring.message.TravelChattingObject;
import com.sconnecting.driverapp.ui.taxi.order.monitoring.message.TravelChattingView;
import com.sconnecting.driverapp.data.models.TravelOrder;
import com.sconnecting.driverapp.data.models.TravelOrderChatting;
import com.sconnecting.driverapp.data.models.User;
import com.squareup.picasso.Callback;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UserProfileView extends Fragment {


    public OrderScreen screen;
    public View view;
    MonitoringView parent;

    View pnlProfileArea;
    public Boolean isCollapsed = true;
    IconTextView btnCollapse;
    ImageButton btnCall;
    CircularImageView imgAvatar;
    ImageView redCircle;
    TextView lblMessageNo;

    TextView lblUserName;
    TextView lblLastMessage;

    public TravelChattingView chattingView;

    public User user;

    public TravelOrder CurrentOrder() {

        if (SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;

    }


    public UserProfileView() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        screen = (OrderScreen) context;
        screen.mMonitoringView.userProfileView = this;

        parent = screen.mMonitoringView;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.taxi_order_monitoring_userprofile, container, false);

        initControls(new Completion() {

            @Override
            public void onCompleted() {

                invalidate(true, null);
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        invalidate(false, null);

    }

    public void initControls(final Completion listener) {

        pnlProfileArea = (View) view.findViewById(R.id.pnlProfileArea);
        pnlProfileArea.setVisibility(View.GONE);
        pnlProfileArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AnimationHelper.hideKeyBoard(v);

                onClickCollapseButton();

                return false;
            }
        });

        btnCollapse = (IconTextView) pnlProfileArea.findViewById(R.id.btnCollapse);
        btnCollapse.setTextColor(Color.DKGRAY);
        btnCollapse.setHighlightColor(Color.DKGRAY);
        btnCollapse.setHintTextColor(Color.DKGRAY);
        AnimationHelper.setOnClick(btnCollapse, new Completion() {
            @Override
            public void onCompleted() {

                onClickCollapseButton();
            }
        });


        btnCall = (ImageButton) pnlProfileArea.findViewById(R.id.btnCall);
        AnimationHelper.setOnClick(btnCall, new Completion() {
            @Override
            public void onCompleted() {

                if(user==null)
                    return;

                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + user.PhoneNo));
                view.getContext().startActivity(intent);
            }
        });


        imgAvatar = (CircularImageView) pnlProfileArea.findViewById(R.id.imgAvatar);
        redCircle = (ImageView) pnlProfileArea.findViewById(R.id.redCircle);
        redCircle.setVisibility(View.GONE);
        lblMessageNo = (TextView) pnlProfileArea.findViewById(R.id.lblMessageNo);
        lblMessageNo.setVisibility(View.GONE);

        lblUserName = (TextView) pnlProfileArea.findViewById(R.id.lblUserName);
        lblLastMessage = (TextView) pnlProfileArea.findViewById(R.id.lblLastMessage);

        chattingView = new TravelChattingView( this.parent.screen,pnlProfileArea.findViewById(R.id.chattingView));

        if(listener != null)
            listener.onCompleted();

    }

    private void onClickCollapseButton() {

        isCollapsed = !isCollapsed;

        if(!isCollapsed)
            chattingView.setReadAll();

        invalidateUI(false,new Completion() {
            @Override
            public void onCompleted() {

            }
        });

        if( !isCollapsed){

            parent.orderPanelView.isCollapsed = true;
            parent.orderPanelView.invalidateUI(false,null);

        }
    }


    Boolean shouldToShow(){

        Boolean isShow  = (user != null) &&  CurrentOrder() != null && CurrentOrder().Driver != null && ( CurrentOrder().Driver.equals(SCONNECTING.driverManager.CurrentDriver.getId()))
                && (  CurrentOrder().IsWaitingDriver() ||  CurrentOrder().IsMonitoring() ||  CurrentOrder().IsStopped() );

        return isShow;
    }

    public void show(Boolean show, final GetBoolValueListener listener){

        if(show){

            if(this.pnlProfileArea.getVisibility() != View.VISIBLE ){

                this.pnlProfileArea.setVisibility(VISIBLE);

                if(listener != null )
                    listener.onCompleted(true,true);
            }else{

                if(listener != null )
                    listener.onCompleted(true,false);
            }


        }else{

            if(this.pnlProfileArea.getVisibility() == View.VISIBLE){

                this.pnlProfileArea.setVisibility(View.GONE);

                if(listener != null )
                    listener.onCompleted(true,true);
            }else{

                if(listener != null )
                    listener.onCompleted(true,false);
            }

        }
    }

    public void invalidate(final Boolean isFirstTime,final Completion listener) {

        if(shouldToShow()){

            parent.screen.showToolbar(true);
            if (this.parent.screen.mPlaceSearcher != null && this.parent.screen.mPlaceSearcher.mSearchView != null)
                this.parent.screen.mPlaceSearcher.mSearchView.setVisibility(GONE);

            show(true, new GetBoolValueListener() {
                @Override
                public void onCompleted(Boolean success, Boolean changed) {

                    invalidateUI(changed,listener);

                }
            });



        }else{

            show(false, new GetBoolValueListener() {
                @Override
                public void onCompleted(Boolean success, Boolean value) {

                    if(listener != null )
                        listener.onCompleted();
                }
            });

        }

    }

    public void invalidateAvatar(final Boolean isFirstTime,final Completion listener) {

        if(isFirstTime && CurrentOrder() != null) {
            String url = CurrentOrder().User != null ? ServerStorage.ServerURL + "/avatar/user/" + CurrentOrder().User : "";

            if (url.isEmpty()) {
                if (listener != null)
                    listener.onCompleted();
                return;
            }
            ImageHelper.loadImage(parent.screen, url, R.drawable.avatar, 140, 140, imgAvatar, new Callback() {
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
        if(listener != null )
            listener.onCompleted();
    }



    public void increaseMessageNo(Integer increase , final GetDoubleValueListener listener){

        if(increase == null) {

            invalidateMessageNo(false,null,listener);
            return;
        }

        String strNo = lblMessageNo.getText().toString();
        if(strNo.isEmpty() == false) {

            Integer count =  Integer.valueOf(strNo);
            if(count != null)
                count += increase;

            invalidateMessageNo(false,count, listener);
        }else {

            invalidateMessageNo(false,null, listener);
        }


    }

    public void invalidateMessageNo(final Boolean isFirstTime,Integer count , final GetDoubleValueListener listener){

        if(count != null && isFirstTime == false){

            this.redCircle.setVisibility( (!this.shouldToShow()  || !this.isCollapsed || count <= 0) ? View.GONE : VISIBLE);
            this.lblMessageNo.setVisibility(  this.redCircle.getVisibility());
            this.lblMessageNo.setText(String.valueOf(count));
            if(listener != null)
                listener.onCompleted(true, Double.valueOf(count));
            return;

        }

        if(isFirstTime == false){
            if(listener != null)
                listener.onCompleted(false, null);
            return;
        }

        TravelOrderController.CountNotYetViewedMessageByDriver(CurrentOrder().getId(), new GetDoubleValueListener() {
            @Override
            public void onCompleted(Boolean success, Double number) {

                redCircle.setVisibility( (!shouldToShow()  || !isCollapsed || number == null || number <= 0) ? View.GONE : VISIBLE);
                lblMessageNo.setVisibility(  redCircle.getVisibility());
                lblMessageNo.setText( number != null ? String.valueOf(number.intValue()) : "");
                if(listener != null)
                    listener.onCompleted(true,  number != null ? Double.valueOf(number) : 0);
            }
        });

    }


    public void invalidateLastMessage(final Boolean isFirstTime,String message , final Completion listener){

        this.lblLastMessage.setVisibility( (!this.shouldToShow()  || !this.isCollapsed) ? View.GONE : VISIBLE);

        if( message != null){
            this.lblLastMessage.setText(message);
            if(listener != null)
                listener.onCompleted();
            return;
        }


        if(isFirstTime == false && this.chattingView != null && this.chattingView.chattingTable != null && this.chattingView.chattingTable.dataSource.size() > 0){

            TravelChattingObject lastObj = this.chattingView.chattingTable.dataSource.size() > 0 ? this.chattingView.chattingTable.dataSource.get(this.chattingView.chattingTable.dataSource.size() - 1) : null;

            if( lastObj != null && lastObj.cellObject != null && lastObj.cellObject.IsUser == 1 && lastObj.cellObject.Content != null ){
                lblLastMessage.setText(lastObj.cellObject.Content);
            }else{
                lblLastMessage.setText("");
            }

            if(listener != null)
                listener.onCompleted();

        }else if(isFirstTime){

            TravelOrderController.GetLastChattingMessage(CurrentOrder().getId(), new GetOneListener() {
                @Override
                public void onGetOne(Boolean success, BaseModel item) {

                    TravelOrderChatting chatting = (TravelOrderChatting)item;
                    if(chatting != null  && chatting.IsUser == 1  && chatting.Content != null){
                        lblLastMessage.setText(chatting.Content);
                    }else{
                        lblLastMessage.setText("");
                    }
                    if(listener != null)
                        listener.onCompleted();
                }
            });
        }


    }


    public void invalidateUI(final Boolean isFirstTime,final Completion listener) {

        if (CurrentOrder() != null){

            this.invalidateAvatar(isFirstTime, null);
            this.btnCollapse.setText(this.isCollapsed ? "{fa-chevron-down}" : "{fa-chevron-up}");

            this.lblUserName.setText((this.user != null) ? this.user.Name.toString().toUpperCase() : "");
            this.lblUserName.setVisibility(VISIBLE);

            this.chattingView.invalidate(!this.isCollapsed, new GetBoolValueListener() {
                @Override
                public void onCompleted(Boolean success, Boolean changed) {

                    invalidateMessageNo(isFirstTime, null, new GetDoubleValueListener() {
                        @Override
                        public void onCompleted(Boolean success, Double number) {

                            invalidateLastMessage(isFirstTime, null, listener);
                        }
                    });
                }
            });


            chattingView.chattingView.setVisibility(this.isCollapsed ? GONE : VISIBLE);
        }

    }

}
