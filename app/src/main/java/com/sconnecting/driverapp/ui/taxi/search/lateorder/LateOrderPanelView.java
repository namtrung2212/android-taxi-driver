package com.sconnecting.driverapp.ui.taxi.search.lateorder;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.base.RegionalHelper;
import com.sconnecting.driverapp.data.controllers.TravelOrderController;
import com.sconnecting.driverapp.data.models.DriverBidding;
import com.sconnecting.driverapp.data.models.TravelOrder;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.VISIBLE;

/**
 * Created by TrungDao on 10/17/16.
 */


public class LateOrderPanelView extends Fragment {


    public LateOrderScreen screen;
    public View view;

    Double estPrice = null;
    DriverBidding bidding = null;

    View OrderInfoArea;
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

    View line1;
    View line2;
    View line3;

    View pnlButtonArea;
    Button btnVoid;
    Button btnBidding;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        screen = (LateOrderScreen) context;
        screen.mLateOrderPanelView = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.taxi_late_order_info, container, false);

        initControls(new Completion(){

            @Override
            public void onCompleted() {

            }
        });
        return view;
    }


    public TravelOrder CurrentOrder() {

        return screen.currentOrder;

    }

    public void initControls(final Completion listener) {


        OrderInfoArea = (View) view.findViewById(R.id.OrderInfoArea);
        pnlOrderArea = (View) OrderInfoArea.findViewById(R.id.pnlOrderArea);
        pnlOrderArea.setVisibility(View.GONE);
        pnlOrderArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AnimationHelper.hideKeyBoard(v);

                onClickCollapseButton();

                return false;
            }
        });

        pnlButtonArea = (View) OrderInfoArea.findViewById(R.id.pnlButtonArea);
        pnlButtonArea.setVisibility(View.GONE);

        btnCollapse = (IconTextView) pnlOrderArea.findViewById(R.id.btnCollapseOrder);
        btnCollapse.setTextColor(Color.DKGRAY);
        btnCollapse.setHighlightColor(Color.DKGRAY);
        btnCollapse.setHintTextColor(Color.DKGRAY);
        AnimationHelper.setOnClick(btnCollapse, new Completion() {
            @Override
            public void onCompleted() {

                onClickCollapseButton();
            }
        });


        lblStatus = (TextView) pnlOrderArea.findViewById(R.id.lblStatus);
        lblStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickCollapseButton();
            }
        });
        lblCurrentPrice = (TextView) pnlOrderArea.findViewById(R.id.lblCurrentPrice);
        lblCurrentPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickCollapseButton();
            }
        });


        btnPickupIcon = (ImageButton) pnlOrderArea.findViewById(R.id.btnPickupIcon);
        btnPickupIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        if( CurrentOrder().ActPickupLoc != null ){
                            screen.mMapView.moveToLocation(CurrentOrder().ActPickupLoc.getLatLng(),true,null);

                        }else if( CurrentOrder().OrderPickupLoc != null ) {
                            screen.mMapView.moveToLocation(CurrentOrder().OrderPickupLoc.getLatLng(),true, null);
                        }


                    }
                });


            }
        });

        btnDropIcon = (ImageButton) pnlOrderArea.findViewById(R.id.btnDropIcon);
        btnDropIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        if( CurrentOrder().ActDropLoc != null ){
                            screen.mMapView.moveToLocation(CurrentOrder().ActDropLoc.getLatLng(),true,null);

                        }else if( CurrentOrder().OrderDropLoc != null ){
                            screen.mMapView.moveToLocation(CurrentOrder().OrderDropLoc.getLatLng(),true,null);
                        }
                    }
                });

            }
        });


        lblPickupLocation = (TextView) pnlOrderArea.findViewById(R.id.lblPickupLocation);
        lblPickupLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        if( CurrentOrder().ActPickupLoc != null){
                            screen.mMapView.moveToLocation(CurrentOrder().ActPickupLoc.getLatLng(),true,null);
                        }else if( CurrentOrder().OrderPickupLoc != null){
                            screen.mMapView.moveToLocation(CurrentOrder().OrderPickupLoc.getLatLng(),true,null);
                        }
                    }
                });


            }
        });
        lblDropLocation = (TextView) pnlOrderArea.findViewById(R.id.lblDropLocation);
        lblDropLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {


                        if( CurrentOrder().ActDropLoc != null){
                            screen.mMapView.moveToLocation(CurrentOrder().ActDropLoc.getLatLng(),true,null);
                        }else if( CurrentOrder().OrderDropLoc != null){
                            screen.mMapView.moveToLocation(CurrentOrder().OrderDropLoc.getLatLng(),true,null);
                        }
                    }
                });

            }
        });

        lblMoreInfo = (TextView) pnlOrderArea.findViewById(R.id.lblMoreInfo);


        btnVoid = (Button) pnlButtonArea.findViewById(R.id.btnVoid);
        btnVoid.setText("HỦY");
        btnVoid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Bạn muốn hủy ?")
                                .setConfirmText("Hủy")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {


                                        SCONNECTING.orderManager.actionHandler.DriverVoidBidding(CurrentOrder(), new GetDoubleValueListener() {
                                            @Override
                                            public void onCompleted(Boolean success, Double value) {
                                                bidding = null;
                                                invalidate(false,null);
                                            }
                                        });

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .showCancelButton(true)
                                .setCancelText("Không")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .show();

                    }
                });

            }
        });


        btnBidding = (Button) pnlButtonArea.findViewById(R.id.btnBidding);
        btnBidding.setText("ĐẤU THẦU");
        btnBidding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        SCONNECTING.orderManager.actionHandler.DriverCreateBidding(CurrentOrder(), null, new GetOneListener() {
                            @Override
                            public void onGetOne(Boolean success, BaseModel item) {

                                invalidate(false,null);
                            }
                        });
                    }
                });
            }
        });

        line1 =  pnlOrderArea.findViewById(R.id.line1);
        line2 =  pnlOrderArea.findViewById(R.id.line2);
        line3 =  pnlOrderArea.findViewById(R.id.line3);

        if(listener != null)
            listener.onCompleted();

    }

    private void onClickCollapseButton() {
        isCollapsed = !isCollapsed;

        invalidateUI(false,null);

    }

    public void invalidate(final Boolean isFirstTime,final Completion listener) {


        invalidateUI(isFirstTime,new Completion() {
            @Override
            public void onCompleted() {

                show(true, listener);

            }
        });


    }

    public void invalidateUI(final Boolean isFirstTime,final Completion listener) {

        this.btnCollapse.setText(this.isCollapsed ? "fa-angle-up" : "fa-angle-down");

        invalidateStatus(isFirstTime, new Completion() {
            @Override
            public void onCompleted() {

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
        });



    }

    public void invalidateStatus(final DriverBidding bidding ){

        String strStatus  = "";


        if(bidding.Status.equals("Open")) {

            strStatus = "Chưa phản hồi";

        }else if(bidding.Status.equals("Expired")) {

            strStatus = "Đã hết hạn";

        }else if(bidding.Status.equals("Rejected")) {

            strStatus = "Đã từ chối";

        }else if(bidding.Status.equals("Accepted")) {

            strStatus = "Chấp nhận";

        }

        lblStatus.setText( strStatus.toUpperCase());


    }


    public void invalidateStatus(final Boolean isFirstTime,final Completion listener) {

        reloadBidding(new Completion() {
            @Override
            public void onCompleted() {

                String strStatus  = "Chưa đề nghị";

                if(bidding != null) {

                    if (bidding.Status.equals("Open")) {

                        strStatus = "Chưa phản hồi";

                    } else if (bidding.Status.equals("Expired")) {

                        strStatus = "Đã hết hạn";

                    } else if (bidding.Status.equals("Rejected")) {

                        strStatus = "Đã từ chối";

                    } else if (bidding.Status.equals("Accepted")) {

                        strStatus = "Chấp nhận";

                    }
                }

                lblStatus.setText( strStatus.toUpperCase());

                if(listener !=null)
                    listener.onCompleted();
            }
        });


    }

    public void invalidatePickupDropLocation(final Boolean isFirstTime,final Completion listener) {

        if (this.CurrentOrder() != null && this.CurrentOrder().OrderPickupLoc != null && this.CurrentOrder().OrderPickupPlace != null) {
            this.lblPickupLocation.setText(this.CurrentOrder().OrderPickupPlace);

        }

        if (this.CurrentOrder() != null && this.CurrentOrder().OrderDropLoc != null && this.CurrentOrder().OrderDropPlace != null) {
            this.lblDropLocation.setText(this.CurrentOrder().OrderDropPlace);

        }

        this.btnPickupIcon.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);
        this.lblPickupLocation.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);
        this.btnDropIcon.setVisibility(!this.isCollapsed ? VISIBLE : View.GONE);
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
        this.line3.setVisibility(lblMoreInfo.getVisibility());

        if(listener !=null)
            listener.onCompleted();

    }

    void reloadBidding(final Completion listener){


            new BaseController<>(DriverBidding.class).getOne("Driver=" + SCONNECTING.driverManager.CurrentDriver.id + "&TravelOrder=" + CurrentOrder().id, new GetOneListener() {
                @Override
                public void onGetOne(Boolean success, BaseModel item) {

                    if (success && item != null) {
                        bidding = (DriverBidding) item;
                    }

                    if (listener != null)
                        listener.onCompleted();
                }
            });


    }

    public void invalidateButtonArea(final Boolean isFirstTime,final Completion listener) {

        reloadBidding(new Completion() {
            @Override
            public void onCompleted() {

                btnVoid.setVisibility((bidding != null && bidding.Status.equals("Open")) ? VISIBLE: View.GONE);
                btnBidding.setVisibility( (bidding == null) ? View.VISIBLE: View.GONE);

                pnlButtonArea.setVisibility( (!isCollapsed && (
                        btnVoid.getVisibility() == VISIBLE || btnBidding.getVisibility() == VISIBLE
                )) ? VISIBLE: View.GONE);


                if(listener !=null)
                    listener.onCompleted();
            }
        });


    }


    public void invalidateTripPrice(final Boolean isFirstTime, final Completion listener) {

        if(estPrice == null) {
            TravelOrderController.CalculateOrderPrice(SCONNECTING.driverManager.CurrentDriver.id, CurrentOrder().User, CurrentOrder().OrderDistance, CurrentOrder().Currency, CurrentOrder().OrderPickupLoc.getLatLng(), new GetDoubleValueListener() {
                @Override
                public void onCompleted(Boolean success, Double value) {

                    if (success)
                        estPrice = value;

                    lblCurrentPrice.setText((estPrice != null && estPrice > 0) ? RegionalHelper.toCurrencyOfCountry(estPrice, SCONNECTING.driverManager.CurrentDriver.Country) : "");

                }
            });
        }else {
            lblCurrentPrice.setText((estPrice != null && estPrice > 0) ? RegionalHelper.toCurrencyOfCountry(estPrice, SCONNECTING.driverManager.CurrentDriver.Country) : "");
        }
        this.lblCurrentPrice.setVisibility(VISIBLE);

        if(listener !=null)
            listener.onCompleted();
    }

    public void show(Boolean isShow , final Completion listener){

        this.OrderInfoArea.setVisibility(isShow ? VISIBLE : View.GONE);
        this.pnlOrderArea.setVisibility(isShow ? VISIBLE : View.GONE);

        if(listener != null )
            listener.onCompleted();
    }

}