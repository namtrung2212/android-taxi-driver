package com.sconnecting.driverapp.ui.taxi.order.review;

import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.RegionalHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.models.OrderStatus;
import com.sconnecting.driverapp.data.models.TravelOrder;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.VISIBLE;

/**
 * Created by TrungDao on 9/19/16.
 */

public class ReviewPanelView {

    ReviewView parent;

    View OrderMonitorArea;
    public View pnlOrderArea;
    public Boolean isCollapsed = false;
    IconTextView btnCollapse;
    TextView lblStatus;
    TextView lblCurrentPrice;
    ImageButton btnPickupIcon;
    ImageButton btnDropIcon;
    TextView lblPickupLocation;
    TextView lblDropLocation;

    TextView lblMoreInfo;
    IconTextView lblMoreInfoIcon;

    View line1;
    View line2;
    View line3;

    View pnlButtonArea;
    Button btnDeny;
    Button btnAccept;
    Button btnTripStart;
    Button btnTripFinish;
    Button btnCashPayment;
    Button btnDone;



    public TravelOrder CurrentOrder() {

        if( SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;

    }

    public ReviewPanelView(ReviewView monitoringView){

        parent = monitoringView;
    }

    public void initUI(final Completion listener) {


        OrderMonitorArea = (View) parent.view.findViewById(R.id.rv_OrderMonitorArea);
        pnlOrderArea = (View) OrderMonitorArea.findViewById(R.id.rv_pnlOrderArea);
        pnlOrderArea.setVisibility(View.GONE);
        pnlOrderArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AnimationHelper.hideKeyBoard(v);

                onClickCollapseButton();

                return false;
            }
        });

        pnlButtonArea = (View) OrderMonitorArea.findViewById(R.id.rv_pnlButtonArea);
        pnlButtonArea.setVisibility(View.GONE);

        btnCollapse = (IconTextView) pnlOrderArea.findViewById(R.id.rv_btnCollapse);
        btnCollapse.setTextColor(Color.DKGRAY);
        btnCollapse.setHighlightColor(Color.DKGRAY);
        btnCollapse.setHintTextColor(Color.DKGRAY);
        AnimationHelper.setOnClick(btnCollapse, new Completion() {
            @Override
            public void onCompleted() {

                onClickCollapseButton();
            }
        });



        lblStatus = (TextView) pnlOrderArea.findViewById(R.id.rv_lblStatus);
        lblStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickCollapseButton();
            }
        });
        lblCurrentPrice = (TextView) pnlOrderArea.findViewById(R.id.rv_lblCurrentPrice);
        lblCurrentPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickCollapseButton();
            }
        });


        btnPickupIcon = (ImageButton) pnlOrderArea.findViewById(R.id.rv_btnPickupIcon);
        btnPickupIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        if( CurrentOrder().ActPickupLoc != null ){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().ActPickupLoc.getLatLng(),true,null,null);

                        }else if( CurrentOrder().OrderPickupLoc != null ) {
                            parent.screen.mMapView.moveToLocation(CurrentOrder().OrderPickupLoc.getLatLng(),true, null,null);
                        }


                    }
                });


            }
        });

        btnDropIcon = (ImageButton) pnlOrderArea.findViewById(R.id.rv_btnDropIcon);
        btnDropIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        if( CurrentOrder().ActDropLoc != null ){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().ActDropLoc.getLatLng(),true,null,null);

                        }else if( CurrentOrder().OrderDropLoc != null ){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().OrderDropLoc.getLatLng(),true,null,null);
                        }
                    }
                });

            }
        });

        lblPickupLocation = (TextView) pnlOrderArea.findViewById(R.id.rv_lblPickupLocation);
        lblPickupLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        if( CurrentOrder().ActPickupLoc != null){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().ActPickupLoc.getLatLng(),true,null,null);
                        }else if( CurrentOrder().OrderPickupLoc != null){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().OrderPickupLoc.getLatLng(),true,null,null);
                        }
                    }
                });


            }
        });
        lblDropLocation = (TextView) pnlOrderArea.findViewById(R.id.rv_lblDropLocation);
        lblDropLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {


                        if( CurrentOrder().ActDropLoc != null){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().ActDropLoc.getLatLng(),true,null,null);
                        }else if( CurrentOrder().OrderDropLoc != null){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().OrderDropLoc.getLatLng(),true,null,null);
                        }
                    }
                });

            }
        });

        lblMoreInfoIcon = (IconTextView) pnlOrderArea.findViewById(R.id.rv_lblMoreInfoIcon);
        lblMoreInfo = (TextView) pnlOrderArea.findViewById(R.id.rv_lblMoreInfo);


        btnDeny = (Button) pnlButtonArea.findViewById(R.id.rv_btnDeny);
        btnDeny.setText("TỪ CHỐI");
        btnDeny.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {


                        SCONNECTING.orderManager.actionHandler.DriverRejectRequest(CurrentOrder(), new GetOneListener() {
                            @Override
                            public void onGetOne(Boolean success, BaseModel item) {

                            SCONNECTING.orderManager.resetToLastOpenningOrder(null);

                            }
                        });
                    }
                });

            }
        });


        btnAccept = (Button) pnlButtonArea.findViewById(R.id.rv_btnAccept);
        btnAccept.setText("CHẤP NHẬN");
        btnAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                    SCONNECTING.orderManager.actionHandler.DriverAcceptRequest(CurrentOrder(), new GetOneListener() {
                        @Override
                        public void onGetOne(Boolean success,final BaseModel newItem) {

                            if (success && newItem != null) {

                                SCONNECTING.driverManager.changeReadyStatus(new GetOneListener() {
                                    @Override
                                    public void onGetOne(Boolean success, BaseModel item) {

                                        SCONNECTING.orderManager.reset((TravelOrder)newItem,true,null);
                                    }
                                });

                            }else {


                                new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Khách đã hủy yêu cầu.")
                                        .setConfirmText("Đồng ý")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog dialog) {

                                                SCONNECTING.orderManager.resetToLastOpenningOrder(null);

                                                dialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();

                            }
                        }
                    });

                    }
                });
            }
        });

        line1 =  pnlOrderArea.findViewById(R.id.rv_line1);
        line2 =  pnlOrderArea.findViewById(R.id.rv_line2);
        line3 =  pnlOrderArea.findViewById(R.id.rv_line3);

        if(listener != null)
            listener.onCompleted();

    }

    private void onClickCollapseButton() {
        isCollapsed = !isCollapsed;

        invalidateUI(false,null);

    }

    public void invalidate(final Boolean isFirstTime,final Completion listener) {

        Boolean isShow= CurrentOrder() != null && CurrentOrder().Status.equals(OrderStatus.Requested)
                && CurrentOrder().Driver != null && ( CurrentOrder().Driver.equals(SCONNECTING.driverManager.CurrentDriver.getId()));

        if(isShow){

            invalidateUI(isFirstTime,new Completion() {
                @Override
                public void onCompleted() {

                    show(true, listener);

                }
            });

        }else{

            show(false,listener);

        }

    }

    public void invalidateUI(final Boolean isFirstTime,final Completion listener) {

        this.btnCollapse.setText(this.isCollapsed ? "{fa-chevron-up}" : "{fa-chevron-down}");

        String strStatus = "KHÁCH ĐỀ NGHỊ ĐÓN";
        this.lblStatus.setText(strStatus.toUpperCase());


        invalidatePickupDropLocation(isFirstTime,new Completion() {
            @Override
            public void onCompleted() {

                invalidateMoreInfo(isFirstTime,new Completion() {
                    @Override
                    public void onCompleted() {

                        invalidateTripPrice(isFirstTime,listener);

                    }
                });

            }
        });


        invalidateButtonArea(isFirstTime,null);




    }


    public void invalidatePickupDropLocation(final Boolean isFirstTime,final Completion listener) {


        String strAddress = "";
        if (this.CurrentOrder() != null && this.CurrentOrder().OrderPickupLoc != null && this.CurrentOrder().OrderPickupPlace != null) {
            strAddress =  this.CurrentOrder().OrderPickupPlace.substring(0,  this.CurrentOrder().OrderPickupPlace.lastIndexOf(", "));
        }
        this.lblPickupLocation.setText(strAddress);

        strAddress = "";
        if (this.CurrentOrder() != null && this.CurrentOrder().OrderDropLoc != null && this.CurrentOrder().OrderDropPlace != null) {
            strAddress =  this.CurrentOrder().OrderDropPlace.substring(0,  this.CurrentOrder().OrderDropPlace.lastIndexOf(", "));
        }
        this.lblDropLocation.setText(strAddress);


        this.btnPickupIcon.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);
        this.btnDropIcon.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);
        this.lblPickupLocation.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);
        this.lblDropLocation.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);
        this.line1.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);
        this.line2.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);

        if(listener !=null)
            listener.onCompleted();

    }

    public void invalidateMoreInfo(final Boolean isFirstTime,final Completion listener) {


        this.lblMoreInfo.setVisibility ( (this.CurrentOrder().OrderDropLoc == null ) || ( this.CurrentOrder().OrderPickupLoc == null ) ? View.GONE : VISIBLE);

        if(this.lblMoreInfo.getVisibility() == VISIBLE){

            String strPlanning = null;

            if(this.CurrentOrder().OrderDistance > 0 && this.CurrentOrder().OrderDuration > 0) {

                String strDistance = String.format("%.1f Km", this.CurrentOrder().OrderDistance / 1000);
                long hours = (long) (this.CurrentOrder().OrderDuration / 3600);
                long minutes = (long) (Math.round((double)(this.CurrentOrder().OrderDuration % 3600) / 60));

                String strDuration = "";
                if (hours > 0) {
                    strDuration = String.format("%d giờ %d phút", hours, minutes);
                } else {
                    strDuration = String.format("%d phút", minutes);

                }
                strPlanning = strDistance + " - " + strDuration;
            }

            if(strPlanning != null){
                strPlanning = "Dự kiến : " + strPlanning;
            }else{
                strPlanning = "";
            }
            this.lblMoreInfo.setText(strPlanning);

        }

        this.lblMoreInfo.setVisibility( (!this.isCollapsed && this.lblMoreInfo.getText().toString().isEmpty() == false) ? VISIBLE : View.GONE);
        lblMoreInfoIcon.setVisibility(lblMoreInfo.getVisibility());
        this.line3.setVisibility(lblMoreInfo.getVisibility());

        if(listener !=null)
            listener.onCompleted();

    }

    public void invalidateButtonArea(final Boolean isFirstTime,final Completion listener) {

        btnDeny.setVisibility(VISIBLE);
        btnAccept.setVisibility(VISIBLE);
        pnlButtonArea.setVisibility(VISIBLE);

        if(listener !=null)
            listener.onCompleted();

    }

    public void invalidateTripPrice(final Boolean isFirstTime, final Completion listener) {

        if(this.CurrentOrder().IsMateHost == 1)
            this.lblCurrentPrice.setText((this.CurrentOrder().HostOrderPrice >= 0) ?  RegionalHelper.toCurrency(this.CurrentOrder().HostOrderPrice, this.CurrentOrder().Currency) :  this.lblCurrentPrice.getText());
        else
            this.lblCurrentPrice.setText((this.CurrentOrder().OrderPrice >= 0) ?  RegionalHelper.toCurrency(this.CurrentOrder().OrderPrice, this.CurrentOrder().Currency) :  this.lblCurrentPrice.getText());

        this.lblCurrentPrice.setVisibility(VISIBLE);

        if(listener !=null)
            listener.onCompleted();
    }

    public void show(Boolean isShow , final Completion listener){

        this.OrderMonitorArea.setVisibility(isShow ? VISIBLE : View.GONE);
        this.pnlOrderArea.setVisibility(isShow ? VISIBLE : View.GONE);

        if(listener != null )
            listener.onCompleted();
    }

}