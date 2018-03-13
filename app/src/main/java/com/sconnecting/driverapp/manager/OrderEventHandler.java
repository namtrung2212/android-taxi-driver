package com.sconnecting.driverapp.manager;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.DeviceHelper;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.notification.TaxiSocketListener;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.base.RegionalHelper;
import com.sconnecting.driverapp.data.models.OrderStatus;
import com.sconnecting.driverapp.data.models.TravelOrder;

import org.json.JSONException;

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderEventHandler implements TaxiSocketListener {

    OrderManager manager;
    SweetAlertDialog alertDialog;

    public OrderEventHandler(OrderManager manager){
        this.manager = manager;
    }

    @Override
    public void onTaxiSocketLogged(final String socketId) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onTaxiSocketLogged(socketId);
                }
            });

        } else {

        }
    }

    @Override
    public void onCarUpdateLocation(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onCarUpdateLocation(data);
                }
            });

        } else {

            // let arrData = data[0] as! [String: AnyObject]

            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            String orderId = data.get("OrderID").toString();

            Double latitude = Double.parseDouble(data.get("latitude").toString());
            Double longitude = Double.parseDouble(data.get("longitude").toString());
            Float degree = Float.parseFloat(data.get("degree").toString());
            Double distance = Double.parseDouble(data.get("distance").toString());

            if (manager.currentOrder.getId() != null && manager.currentOrder.getId().equals(orderId)) {


                if(SCONNECTING.orderScreen.mMapMarkerManager.currentVehicle().driverId.equals(driverId)) {
                    SCONNECTING.orderScreen.mMapMarkerManager.currentVehicle().updateLocation(latitude, longitude, (float)degree, new Completion() {
                        @Override
                        public void onCompleted() {

                            SCONNECTING.orderScreen.mMapMarkerManager.currentVehicle().moveToCarLocation();
                        }
                    });
                }

            }


        }
    }


    @Override
    public void onUserRequestTaxi(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onUserRequestTaxi(data);
                }
            });

        } else {


            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            String orderId = data.get("OrderID").toString();

            if (driverId.equals(SCONNECTING.driverManager.CurrentDriver.getId())) {


                new BaseController<>(TravelOrder.class).getById(true, orderId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {


                        final TravelOrder order = (TravelOrder) item;

                        if (order != null ) {

                            DeviceHelper.playDefaultNotificationSound();

                            if(alertDialog != null && alertDialog.isShowing())
                                alertDialog.dismissWithAnimation();

                            alertDialog = new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText("Có khách đón tại")
                                    .setContentText(String.format("%s \r\n \n Tài xế có muốn đón khách không?", order.OrderPickupPlace == null ? "" : order.OrderPickupPlace))
                                    .setConfirmText("Xem qua")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            manager.reset(order,true, null);

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .setCancelText("Không")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            manager.actionHandler.DriverRejectRequest(order, null);

                                            manager.reset(true, null);

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();


                        }

                    }
                });


            }


        }
    }

    @Override
    public void onUserCancelRequest(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onUserCancelRequest(data);
                }
            });

        } else {


            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            String orderId = data.get("OrderID").toString();

            if (driverId.equals(SCONNECTING.driverManager.CurrentDriver.getId())) {


                new BaseController<>(TravelOrder.class).getById(true, orderId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {


                        final TravelOrder order = (TravelOrder) item;

                        if (order != null ) {

                            DeviceHelper.playDefaultNotificationSound();

                            if(alertDialog != null && alertDialog.isShowing())
                                alertDialog.dismissWithAnimation();

                            alertDialog = new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText("Khách đã hủy yêu cầu")
                                    .setContentText(" ")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            manager.resetToLastOpenningOrder(null);

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();


                        }

                    }
                });


            }


        }
    }

    @Override
    public void onUserAcceptBidding(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onUserAcceptBidding(data);
                }
            });

        } else {


            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            String orderId = data.get("OrderID").toString();

            if (driverId.equals(SCONNECTING.driverManager.CurrentDriver.getId())) {


                new BaseController<>(TravelOrder.class).getById(true, orderId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {

                        final TravelOrder order = (TravelOrder) item;

                        if (order != null) {

                            DeviceHelper.playDefaultNotificationSound();

                            if(alertDialog != null && alertDialog.isShowing())
                                alertDialog.dismissWithAnimation();

                            alertDialog = new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText("Xác nhận đặt trước")
                                    .setContentText(String.format("Khách hàng đồng ý đón tại địa chỉ : \r \n %s \r\n vào lúc %s", order.OrderPickupPlace == null ? "" : order.OrderPickupPlace, order.getPickupTimeString()))
                                    .setConfirmText("Xác nhận")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {


                                            manager.actionHandler.DriverAcceptRequest(order, new GetOneListener() {
                                                @Override
                                                public void onGetOne(Boolean success, final BaseModel newItem) {

                                                    if (success && newItem != null) {


                                                    }else {


                                                        if(alertDialog != null && alertDialog.isShowing())
                                                            alertDialog.dismissWithAnimation();

                                                        alertDialog = new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE);
                                                        alertDialog.setTitleText("Khách đã hủy yêu cầu.")
                                                                .setConfirmText("Đồng ý")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog dialog) {

                                                                        dialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();

                                                    }

                                                }
                                            });



                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .setCancelText("Từ chối")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            manager.actionHandler.DriverRejectRequest(order, null);

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();

                        }
                    }
                });
            }
        }
    }

    @Override
    public void onUserCancelAcceptingBidding(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onUserCancelAcceptingBidding(data);
                }
            });

        } else {


            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            String orderId = data.get("OrderID").toString();

            if (driverId.equals(SCONNECTING.driverManager.CurrentDriver.getId())) {


                new BaseController<>(TravelOrder.class).getById(true, orderId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {


                        if (item != null) {

                            final TravelOrder order = (TravelOrder) item;


                        }
                    }
                });
            }

        }
    }

    @Override
    public void onUserVoidedBfPickup(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onUserVoidedBfPickup(data);
                }
            });

        } else {


            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            String orderId = data.get("OrderID").toString();

            if (driverId.equals(SCONNECTING.driverManager.CurrentDriver.getId())) {


                new BaseController<>(TravelOrder.class).getById(true, orderId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {


                        final TravelOrder order = (TravelOrder) item;

                        if (order != null && order.Status.equals(OrderStatus.VoidedBfPickupByUser)) {

                            manager.reset(true, null);

                            DeviceHelper.playDefaultNotificationSound();

                            if(alertDialog != null && alertDialog.isShowing())
                                alertDialog.dismissWithAnimation();

                            alertDialog = new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText("Khách hủy đón")
                                    .setContentText(String.format("Khách hủy đón tại địa chỉ : \r \n %s", order.OrderPickupPlace == null ? "" : order.OrderPickupPlace))
                                    .setConfirmText("Đồng ý")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            SCONNECTING.driverManager.changeReadyStatus(new GetOneListener() {
                                                @Override
                                                public void onGetOne(Boolean success, BaseModel item) {

                                                    // AppDelegate.mainWindow?.taxiViewCtrl.controlPanelView.invalidateReadyButton(nil)
                                                }
                                            });


                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onUserVoidedAfPickup(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onUserVoidedAfPickup(data);
                }
            });

        } else {


            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            String orderId = data.get("OrderID").toString();

            if (driverId.equals(SCONNECTING.driverManager.CurrentDriver.getId())) {


                new BaseController<>(TravelOrder.class).getById(true, orderId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {


                        final TravelOrder order = (TravelOrder) item;

                        if (order != null && order.Status.equals(OrderStatus.VoidedAfPickupByUser)) {

                            manager.reset(true, null);

                            DeviceHelper.playDefaultNotificationSound();

                            if(alertDialog != null && alertDialog.isShowing())
                                alertDialog.dismissWithAnimation();

                            alertDialog = new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText("Khách hủy ")
                                    .setContentText("Khách hủy chuyến giữa hành trình.")
                                    .setConfirmText("Đồng ý")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {


                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    manager.reset(order, true, null);
                                                }
                                            }, 2000);
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onUserPaid(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onUserPaid(data);
                }
            });

        } else {


            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            String orderId = data.get("OrderID").toString();

            if (driverId.equals(SCONNECTING.driverManager.CurrentDriver.getId())) {


                new BaseController<>(TravelOrder.class).getById(true, orderId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {

                        final TravelOrder order = (TravelOrder) item;

                        if (order != null && order.IsPaid == 1) {

                            manager.reset(true, null);

                            DeviceHelper.playDefaultNotificationSound();

                            if(alertDialog != null && alertDialog.isShowing())
                                alertDialog.dismissWithAnimation();

                            alertDialog = new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText("Khách đã thanh toán")
                                    .setContentText(String.format("Khách đã thanh toán bằng thẻ với số tiền %s ", RegionalHelper.toCurrency(order.PayAmount, "VND")))
                                    .setConfirmText("Đồng ý")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            if (manager.currentOrder != null && order.getId().equals(manager.currentOrder.getId())) {


                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        manager.reset(true, null);

                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                manager.invalidate(false,true, null);
                                                            }
                                                        }, 5000);
                                                    }
                                                }, 2000);


                                                SCONNECTING.driverManager.changeReadyStatus(new GetOneListener() {
                                                    @Override
                                                    public void onGetOne(Boolean success, BaseModel item) {

                                                    }
                                                });
                                            }

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
            }

        }
    }

    @Override
    public void onDriverShouldInvalidateOrder(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onDriverShouldInvalidateOrder(data);
                }
            });

        } else {

            String userId = data.get("UserID").toString();
            final String orderId = data.get("OrderID").toString();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (manager.currentOrder != null && manager.currentOrder.getId() != null && manager.currentOrder.getId().equals(orderId)) {

                        manager.invalidate(false,true, null);
                    }

                }
            }, 2000);
        }
    }

    @Override
    public void onCheckAppInForeground(final Map<String, Object> data) {


        if (Looper.myLooper() != Looper.getMainLooper()) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onCheckAppInForeground(data);
                    }
                });

        } else {


                boolean isForeground = DeviceHelper.isAppInForeground();
                if(isForeground){

                    try {
                        SCONNECTING.notificationHelper.taxiSocket.DriverAppInForeground();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        SCONNECTING.notificationHelper.taxiSocket.DriverAppInBackground();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

        }

    }

}
