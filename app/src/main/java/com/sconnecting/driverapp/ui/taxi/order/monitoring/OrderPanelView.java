package com.sconnecting.driverapp.ui.taxi.order.monitoring;

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
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.base.RegionalHelper;
import com.sconnecting.driverapp.data.models.OrderStatus;
import com.sconnecting.driverapp.data.models.TravelOrder;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.VISIBLE;

/**
 * Created by TrungDao on 9/19/16.
 */

public class OrderPanelView {

    MonitoringView parent;

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
    Button btnVoid;
    Button btnGoToPickup;
    Button btnTripStart;
    Button btnTripFinish;
    Button btnCashPayment;
    Button btnDone;



    public TravelOrder CurrentOrder() {

        if( SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;

    }

    public OrderPanelView(MonitoringView monitoringView){

        parent = monitoringView;
    }

    public void initUI(final Completion listener) {


        OrderMonitorArea = (View) parent.view.findViewById(R.id.OrderMonitorArea);
        pnlOrderArea = (View) OrderMonitorArea.findViewById(R.id.pnlOrderArea);
        pnlOrderArea.setVisibility(View.GONE);
        pnlOrderArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AnimationHelper.hideKeyBoard(v);

                onClickCollapseButton();

                return false;
            }
        });

        pnlButtonArea = (View) OrderMonitorArea.findViewById(R.id.pnlButtonArea);
        pnlButtonArea.setVisibility(View.GONE);

        btnCollapse = (IconTextView) pnlOrderArea.findViewById(R.id.btnCollapse);
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
                            parent.screen.mMapView.moveToLocation(CurrentOrder().ActPickupLoc.getLatLng(),true,null,null);

                        }else if( CurrentOrder().OrderPickupLoc != null ) {
                            parent.screen.mMapView.moveToLocation(CurrentOrder().OrderPickupLoc.getLatLng(),true, null,null);
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
                            parent.screen.mMapView.moveToLocation(CurrentOrder().ActDropLoc.getLatLng(),true,null,null);

                        }else if( CurrentOrder().OrderDropLoc != null ){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().OrderDropLoc.getLatLng(),true,null,null);
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
                            parent.screen.mMapView.moveToLocation(CurrentOrder().ActPickupLoc.getLatLng(),true,null,null);
                        }else if( CurrentOrder().OrderPickupLoc != null){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().OrderPickupLoc.getLatLng(),true,null,null);
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
                            parent.screen.mMapView.moveToLocation(CurrentOrder().ActDropLoc.getLatLng(),true,null,null);
                        }else if( CurrentOrder().OrderDropLoc != null){
                            parent.screen.mMapView.moveToLocation(CurrentOrder().OrderDropLoc.getLatLng(),true,null,null);
                        }
                    }
                });

            }
        });

        lblMoreInfoIcon = (IconTextView) pnlOrderArea.findViewById(R.id.lblMoreInfoIcon);
        lblMoreInfo = (TextView) pnlOrderArea.findViewById(R.id.lblMoreInfo);


        btnVoid = (Button) pnlButtonArea.findViewById(R.id.btnVoid);
        btnVoid.setText("HỦY CHUYẾN");
        btnVoid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Bạn muốn hủy hành trình ?")
                                .setContentText("Việc hủy chuyến thường xuyên sẽ được ghi nhận vào hồ sơ của bạn.")
                                .setConfirmText("Hủy")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        SCONNECTING.orderManager.actionHandler.DriverVoidOrder(CurrentOrder(), null);

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


        btnGoToPickup = (Button) pnlButtonArea.findViewById(R.id.btnGoToPickup);
        btnGoToPickup.setText("BẮT ĐẦU ĐÓN");
        btnGoToPickup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        SCONNECTING.orderManager.actionHandler.DriverStartPicking(CurrentOrder(), new GetOneListener() {
                            @Override
                            public void onGetOne(Boolean success, BaseModel item) {

                                isCollapsed = true;
                                invalidateUI(false,null);
                            }
                        });

                    }
                });
            }
        });

        btnTripStart = (Button) pnlButtonArea.findViewById(R.id.btnTripStart);
        btnTripStart.setText("KHỞI HÀNH");
        btnTripStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {


                        new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Bắt đầu hành trình ?")
                                .setContentText("Bắt đầu tính cước xe và thông báo đến khách hàng.")
                                .setConfirmText("Bắt đầu")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {


                                        SCONNECTING.orderManager.actionHandler.DriverStartTrip(CurrentOrder(), new GetOneListener() {
                                            @Override
                                            public void onGetOne(Boolean success, BaseModel item) {

                                                  isCollapsed = true;
                                                  invalidateUI(false,null);
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

        btnTripFinish = (Button) pnlButtonArea.findViewById(R.id.btnTripFinish);
        btnTripFinish.setText("TRẢ KHÁCH");
        btnTripFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Trả khách tại đây ?")
                                .setContentText("Đề nghị kiểm tra lại hành lý. Giữ thái độ chuyên nghiệp, thân thiện.")
                                .setConfirmText("Trả khách")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        SCONNECTING.orderManager.actionHandler.DriverFinishTrip(CurrentOrder(), null);

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

        btnCashPayment = (Button) pnlButtonArea.findViewById(R.id.btnCashPayment);
        btnCashPayment.setText("NHẬN TIỀN MẶT");
        btnCashPayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                       String strMustPay = RegionalHelper.toCurrency(SCONNECTING.orderManager.currentOrder.ActPrice,SCONNECTING.orderManager.currentOrder.Currency);

                        new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Nhận tiền mặt ?")
                                .setContentText("Số tiền : " + strMustPay + " Xác nhận và kiểm tra lại tiền thực nhận.")
                                .setConfirmText("Đã nhận")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        SCONNECTING.orderManager.actionHandler.DriverReceivedCash(CurrentOrder(), null);

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .showCancelButton(true)
                                .setCancelText("Bỏ qua")
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

        btnDone = (Button) pnlButtonArea.findViewById(R.id.btnDone);
        btnDone.setText("HOÀN TẤT");
        btnDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        String strMustPay = RegionalHelper.toCurrency(SCONNECTING.orderManager.currentOrder.ActPrice,SCONNECTING.orderManager.currentOrder.Currency);

                        new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Hoàn tất hành trình")
                                .setContentText("Số tiền : " + strMustPay + " sẽ được ghi nợ cho khách hàng.")
                                .setConfirmText("Hoàn tất")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        SCONNECTING.orderManager.reset(true, new Completion() {
                                            @Override
                                            public void onCompleted() {

                                                SCONNECTING.driverManager.changeReadyStatus(new GetOneListener() {
                                                    @Override
                                                    public void onGetOne(Boolean success, BaseModel item) {

                                                        parent.screen.mControlPanelView.invalidateReadyButton(null);

                                                    }
                                                });

                                            }
                                        });

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .showCancelButton(true)
                                .setCancelText("Bỏ qua")
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


        line1 =  pnlOrderArea.findViewById(R.id.line1);
        line2 =  pnlOrderArea.findViewById(R.id.line2);
        line3 =  pnlOrderArea.findViewById(R.id.line3);

        if(listener != null)
            listener.onCompleted();

    }

    private void onClickCollapseButton() {
        isCollapsed = !isCollapsed;

        invalidateUI(false,null);


        if( !isCollapsed){

            parent.userProfileView.isCollapsed = true;
            parent.userProfileView.invalidateUI(false,null);

        }
    }

    public void invalidate(final Boolean isFirstTime,final Completion listener) {

        Boolean isShow= CurrentOrder() != null && CurrentOrder().Driver != null && ( CurrentOrder().Driver.equals(SCONNECTING.driverManager.CurrentDriver.getId()))
                && (CurrentOrder().IsDriverAccepted() || CurrentOrder().IsMonitoring()|| CurrentOrder().IsStopped());

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

        invalidateStatus(isFirstTime, new GetDoubleValueListener() {
            @Override
            public void onCompleted(Boolean success, Double value) {

                final double distance = value;

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


                invalidateButtonArea(isFirstTime,distance,null);
            }
        });



    }

    public void invalidateStatus(final Boolean isFirstTime,final GetDoubleValueListener listener) {

        String strStatus = "";
        double distance = 0.0;

        if(this.CurrentOrder() != null) {

            if(this.CurrentOrder().IsDriverAccepted()) {
                strStatus = "Khách đang chờ...";

            }else if (this.CurrentOrder().IsDriverPicking()) {
                strStatus = "Đang đến điểm đón khách.";


                if ( SCONNECTING.driverManager.CurrentDriver.getId() == CurrentOrder().Driver
                        && CurrentOrder().OrderPickupLoc != null && SCONNECTING.locationHelper.location != null) {

                    distance = SCONNECTING.locationHelper.location.distanceTo(CurrentOrder().OrderPickupLoc.getLocation());

                    if (distance >= 1000) {
                        strStatus = String.format("Đang đón khách. Khoảng cách %.1f Km", distance / 1000);

                    } else if (distance > 50) {
                        strStatus = String.format("Đang đón khách. Khoảng cách %.0f m", distance);

                    } else {
                        strStatus = "Đã đến điểm hẹn.";

                        if (strStatus.toUpperCase().equals(this.lblStatus.getText().toString()) == false) {

                            new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Đã đến đúng điểm hẹn.")
                                    .setContentText(CurrentOrder().OrderPickupPlace + "\r \n Khách đã nhận được tin báo.")
                                    .setConfirmText("Chờ khách")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            invalidateUI(isFirstTime,null);

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();


                        }
                    }

                }

            } else if (this.CurrentOrder().IsOnTheWay()) {

                strStatus = "Đang trong hành trình. ";

            } else if (this.CurrentOrder().IsVoidedByDriver() && this.CurrentOrder().IsFinishedNotYetPaid()) {

                strStatus = "Tài xế đã huỷ hành trình. Chưa thanh toán. ";

            } else if (this.CurrentOrder().IsVoidedByUser() && this.CurrentOrder().IsFinishedNotYetPaid()) {

                strStatus = "Khách đã huỷ hành trình. Chưa thanh toán. ";

            } else if (this.CurrentOrder().IsFinishedNotYetPaid()) {

                strStatus = "Đã đến nơi. Chưa thanh toán.";

            } else if (this.CurrentOrder().IsFinishedAndPaid()) {

                strStatus = "Đã thanh toán";

                if (this.CurrentOrder().IsVoidedByDriver()) {

                    strStatus = "Tài xế huỷ hành trình. Đã thanh toán. ";

                } else if (this.CurrentOrder().IsVoidedByUser()) {

                    strStatus = "Khách huỷ hành trình. Đã thanh toán. ";

                }

            }
        }

        this.lblStatus.setText(strStatus.toUpperCase());

        if(listener !=null)
            listener.onCompleted(true,distance);
    }

    public void invalidatePickupDropLocation(final Boolean isFirstTime,final Completion listener) {


        String strAddress = "";
        if (this.CurrentOrder() != null && this.CurrentOrder().OrderPickupLoc != null && this.CurrentOrder().OrderPickupPlace != null) {
            strAddress =  this.CurrentOrder().OrderPickupPlace.substring(0,  this.CurrentOrder().OrderPickupPlace.lastIndexOf(", "));
        } else if (this.CurrentOrder() != null && this.CurrentOrder().ActPickupLoc != null && this.CurrentOrder().ActPickupPlace != null) {
            strAddress =  this.CurrentOrder().ActPickupPlace.substring(0,  this.CurrentOrder().ActPickupPlace.lastIndexOf(", "));
        }
        this.lblPickupLocation.setText(strAddress);

        strAddress = "";
        if (this.CurrentOrder() != null && this.CurrentOrder().OrderDropLoc != null && this.CurrentOrder().OrderDropPlace != null) {
            strAddress =  this.CurrentOrder().OrderDropPlace.substring(0,  this.CurrentOrder().OrderDropPlace.lastIndexOf(", "));
        } else if (this.CurrentOrder() != null && this.CurrentOrder().ActDropLoc != null && this.CurrentOrder().ActDropPlace != null) {
            strAddress =  this.CurrentOrder().ActDropPlace.substring(0,  this.CurrentOrder().ActDropPlace.lastIndexOf(", "));

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

        if(this.CurrentOrder() != null && this.CurrentOrder().IsWaitingDriver()){

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

        }else if(this.CurrentOrder() != null &&  this.CurrentOrder().IsStopped()){


            this.lblMoreInfo.setVisibility(VISIBLE);

            String strActual = null;

            if(this.CurrentOrder().ActPickupLoc != null && this.CurrentOrder().ActDropLoc != null){

                Location actPickupLoc = this.CurrentOrder().ActPickupLoc.getLocation();
                Location actDropLoc = this.CurrentOrder().ActDropLoc.getLocation();
                Double distance = this.CurrentOrder().ActDistance != null ? this.CurrentOrder().ActDistance : (double)actDropLoc.distanceTo(actPickupLoc);

                String strDistance = String.format("%.1f Km", distance/1000 );
                long timeInterval = this.CurrentOrder().ActDropTime.getTime() - this.CurrentOrder().ActPickupTime.getTime();
                long hours =  (long)(timeInterval / (1000 * 60 * 60));
                long minutes =  (long)(Math.round((double)(timeInterval % (1000 * 60 * 60 )) / (1000 * 60)));

                String strDuration = "";
                if( hours > 0){
                    strDuration =  String.format("%d giờ %d phút", hours, minutes );
                }else{
                    strDuration =  String.format("%d phút", minutes );

                }
                strActual = strDistance + " - " + strDuration;

            }


            if(strActual != null){
                strActual = "Thực tế : " + strActual;
            }else{
                strActual = "";
            }
            this.lblMoreInfo.setText(strActual);


        }else if(this.CurrentOrder() != null &&  this.CurrentOrder().IsOnTheWay()){

            this.lblMoreInfo.setText("");

        }


        this.lblMoreInfo.setVisibility( (!this.isCollapsed && this.lblMoreInfo.getText().toString().isEmpty() == false) ? VISIBLE : View.GONE);
        lblMoreInfoIcon.setVisibility(lblMoreInfo.getVisibility());
        this.line3.setVisibility(lblMoreInfo.getVisibility());

        if(listener !=null)
            listener.onCompleted();

    }

    public void invalidateButtonArea(final Boolean isFirstTime,Double distance,final Completion listener) {

        btnVoid.setVisibility( (CurrentOrder().IsDriverAccepted() || CurrentOrder().IsMonitoring()) ? VISIBLE: View.GONE);
        btnGoToPickup.setVisibility(  CurrentOrder().IsDriverAccepted() ? View.VISIBLE: View.GONE);
        btnTripStart.setVisibility( CurrentOrder().IsDriverPicking()  && distance <= 50 ? VISIBLE: View.GONE);
        btnTripFinish.setVisibility( CurrentOrder().IsOnTheWay() ? VISIBLE: View.GONE);
        btnCashPayment.setVisibility( CurrentOrder().IsFinishedNotYetPaid() ? VISIBLE: View.GONE);
        btnDone.setVisibility( CurrentOrder().IsFinishedNotYetPaid() ? VISIBLE: View.GONE);

        pnlButtonArea.setVisibility( (!isCollapsed && (
                                                       btnVoid.getVisibility() == VISIBLE
                                                    || btnGoToPickup.getVisibility() == VISIBLE
                                                    || btnTripStart.getVisibility() == VISIBLE
                                                    || btnTripFinish.getVisibility() == VISIBLE
                                                    || btnCashPayment.getVisibility() == VISIBLE
                                                    || btnDone.getVisibility() == VISIBLE   )) ? VISIBLE: View.GONE);


        if(listener !=null)
            listener.onCompleted();

    }

    public void invalidateTripPrice(final Boolean isFirstTime, final Completion listener) {


        if(this.CurrentOrder().IsStopped()){

            this.lblCurrentPrice.setText((this.CurrentOrder().ActPrice >= 0) ?  RegionalHelper.toCurrency(this.CurrentOrder().ActPrice, this.CurrentOrder().Currency) :  this.lblCurrentPrice.getText());
            this.lblCurrentPrice.setVisibility(VISIBLE);


        }else{

            if(this.CurrentOrder().IsMateHost == 1)
                this.lblCurrentPrice.setText((this.CurrentOrder().HostOrderPrice >= 0) ?  RegionalHelper.toCurrency(this.CurrentOrder().HostOrderPrice, this.CurrentOrder().Currency) :  this.lblCurrentPrice.getText());
            else
                this.lblCurrentPrice.setText((this.CurrentOrder().OrderPrice >= 0) ?  RegionalHelper.toCurrency(this.CurrentOrder().OrderPrice, this.CurrentOrder().Currency) :  this.lblCurrentPrice.getText());
            this.lblCurrentPrice.setVisibility(VISIBLE);

        }


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