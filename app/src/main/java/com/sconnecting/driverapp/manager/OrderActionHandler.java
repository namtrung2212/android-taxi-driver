package com.sconnecting.driverapp.manager;

import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.base.listener.DeleteListener;
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.PostListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.models.OrderStatus;
import com.sconnecting.driverapp.location.LocationHelper;
import com.sconnecting.driverapp.data.controllers.TravelOrderController;
import com.sconnecting.driverapp.data.models.DriverBidding;
import com.sconnecting.driverapp.data.models.DriverStatus;
import com.sconnecting.driverapp.data.models.TravelOrder;

import org.json.JSONException;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderActionHandler{

    OrderManager manager;

    public OrderActionHandler(OrderManager manager){
        this.manager = manager;
    }


    public void DriverAcceptRequest(final TravelOrder orderToAccept, final GetOneListener listener) {

        TravelOrderController.DriverAcceptRequest(orderToAccept.getId(), SCONNECTING.driverManager.CurrentDriver.getId(), new GetOneListener() {
            @Override
            public void onGetOne(Boolean success,final BaseModel newItem) {

                if (success && newItem != null) {

                    SCONNECTING.driverManager.changeReadyStatus(new GetOneListener() {
                        @Override
                        public void onGetOne(Boolean success, BaseModel item) {

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    manager.currentOrder = (TravelOrder) newItem;
                                    manager.invalidateUI(false,null);
                                }
                            }, 2000);
                        }
                    });

                }else {


                    new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Khách đã hủy yêu cầu.")
                            .setConfirmText("Đồng ý")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog dialog) {

                                    dialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }

                if(listener != null)
                    listener.onGetOne(success,newItem);
            }
        });
    }

    public void DriverRejectRequest(final TravelOrder orderToReject, final GetOneListener listener) {

        TravelOrderController.DriverRejectRequest(orderToReject.getId(), SCONNECTING.driverManager.CurrentDriver.getId(), new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if(listener != null)
                    listener.onGetOne(success,item);
            }
        });

    }

    public void DriverCreateBidding(final TravelOrder order, final String message, final GetOneListener listener) {

        String driverId = SCONNECTING.driverManager.CurrentDriver.getId();


        new BaseController<>(DriverStatus.class).getOneByStringField(true, "Driver", driverId, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if (item != null) {

                    DriverStatus status = (DriverStatus) item;

                    DriverBidding bidding = new DriverBidding();
                    bidding.Driver = status.Driver;
                    bidding.WorkingPlan = status.WorkingPlan;
                    bidding.Company = status.Company;
                    bidding.Team = status.Team;
                    bidding.TravelOrder = order.getId();
                    bidding.User = order.User;
                    bidding.Message = (message != null) ? message : null;

                    new BaseController<>(DriverBidding.class).create(bidding,"DriverCreateBidding", new PostListener() {
                        @Override
                        public void onCompleted(Boolean success, BaseModel newitem) {

                            if (listener != null)
                                listener.onGetOne(true, newitem);


                        }
                    });


                } else {

                    if (listener != null)
                        listener.onGetOne(false, null);

                }

            }
        });


    }

    public void GetBiddingsByOrderAndDriver(final TravelOrder order, final GetOneListener listener) {

        String driverId = SCONNECTING.driverManager.CurrentDriver.getId();

        new BaseController<>(DriverBidding.class).get("GetBiddingsByOrderAndDriver", "Driver=" + driverId + "&TravelOrder=" + order.getId(), new GetItemsListener() {
            @Override
            public void onGetItems(Boolean success, List list) {

                if (success && list.size() > 0) {
                    if (listener != null)
                        listener.onGetOne(true, (BaseModel) list.get(0));
                } else {

                    if (listener != null)
                        listener.onGetOne(false, null);
                }
            }
        });

    }

    public void DriverVoidBidding(TravelOrder order, final GetDoubleValueListener listener) {

        String driverId = SCONNECTING.driverManager.CurrentDriver.getId();

        new BaseController<>(DriverBidding.class).delete("Driver=" + driverId + "&TravelOrder=" + order.getId(), new DeleteListener() {
            @Override
            public void onDeleted(Boolean success, Integer deleted) {
                if (listener != null)
                    listener.onCompleted(success, 0.0);
            }
        });

    }

    public void nofifyLocationToUser(TravelOrder order, Location location) {

        if (order != null && order.Driver.equals(SCONNECTING.driverManager.CurrentDriver.getId()) && order.User != null && order.IsMonitoring()) {

            try {
                SCONNECTING.notificationHelper.taxiSocket.updateLocation(order, location);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void DriverVoidOrder(final TravelOrder orderToVoid, final GetOneListener listener) {

        if (orderToVoid.id == null) {
            if (listener != null)
                listener.onGetOne(false, orderToVoid);
            return;
        }


        final LatLng currentLocation = LocationHelper.newLatLng(SCONNECTING.locationHelper.getLocation());
        TravelOrderController.DriverVoidOrder(orderToVoid.getId(), currentLocation, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if(success) {

                    TravelOrder order = (TravelOrder) item;

                    if(order != null && order.Status.equals(OrderStatus.VoidedBfPickupByDriver)){

                        SCONNECTING.orderManager.reset(true, new Completion() {
                            @Override
                            public void onCompleted() {

                                SCONNECTING.driverManager.changeReadyStatus(new GetOneListener() {
                                    @Override
                                    public void onGetOne(Boolean success, BaseModel item) {
                                        // AppDelegate.mainWindow?.taxiViewCtrl.controlPanelView.invalidateReadyButton(nil)
                                    }
                                });
                            }
                        });

                    }else if(order != null && order.Status.equals(OrderStatus.VoidedAfPickupByDriver )){

                        SCONNECTING.orderManager.reset(order, true, null);

                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            SCONNECTING.orderManager.invalidate(false,true,null);
                        }
                    }, 4000);
                }

                if (listener != null)
                    listener.onGetOne(success, item);
            }
        });


    }

    public void DriverStartPicking(final TravelOrder order, final GetOneListener listener) {

        if (order.id == null || !order.IsDriverAccepted()) {
            if (listener != null)
                listener.onGetOne(false, order);
            return;
        }

        TravelOrderController.DriverStartPicking(order.getId(), SCONNECTING.driverManager.CurrentDriver.getId(), new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if(success) {

                    SCONNECTING.orderManager.currentOrder = (TravelOrder) item;

                    if(SCONNECTING.orderManager.currentOrder != null && SCONNECTING.orderManager.currentOrder.Status.equals(OrderStatus.DriverPicking)){

                        SCONNECTING.orderManager.invalidateUI(false, null);

                    }
                }
                if(listener != null)
                    listener.onGetOne(success,item);
            }
        });
    }

    public void DriverStartTrip(final TravelOrder orderToStart, final GetOneListener listener) {

        if (orderToStart.id == null || !orderToStart.IsDriverPicking()) {

            if (listener != null)
                listener.onGetOne(false, orderToStart);

            return;
        }

        final LatLng currentLocation = LocationHelper.newLatLng(SCONNECTING.locationHelper.getLocation());

        TravelOrderController.DriverStartTrip(orderToStart.getId(), SCONNECTING.driverManager.CurrentDriver.getId(), currentLocation, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if(success) {

                    SCONNECTING.orderManager.currentOrder = (TravelOrder) item;

                    SCONNECTING.orderManager.invalidate(false,true,null);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            SCONNECTING.orderManager.invalidate(false,true,null);
                        }
                    }, 4000);


                }
                if (listener != null)
                    listener.onGetOne(success, item);
            }
        });
    }

    public void DriverFinishTrip(final TravelOrder orderToFinish, final GetOneListener listener) {

        if (orderToFinish.id == null || orderToFinish.IsOnTheWay() == false) {

            if (listener != null)
                listener.onGetOne(false, orderToFinish);
            return;
        }

        final LatLng currentLocation = LocationHelper.newLatLng(SCONNECTING.locationHelper.getLocation());

        TravelOrderController.DriverFinishTrip(orderToFinish.getId(), SCONNECTING.driverManager.CurrentDriver.getId(), currentLocation, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if(success) {

                    SCONNECTING.orderManager.currentOrder = (TravelOrder) item;

                    SCONNECTING.orderManager.invalidate(false,true,null);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            SCONNECTING.orderManager.invalidate(false,true,null);
                        }
                    }, 5000);


                }

                if (listener != null)
                    listener.onGetOne(success, item);
            }
        });


    }

    public void DriverReceivedCash(TravelOrder orderToPay, final GetOneListener listener) {

        if (orderToPay.id == null || orderToPay.IsFinishedNotYetPaid() == false) {

            if (listener != null)
                listener.onGetOne(false, orderToPay);
            return;
        }

        TravelOrderController.DriverReceivedCash(orderToPay.getId(), SCONNECTING.driverManager.CurrentDriver.getId(), new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if(success) {

                    SCONNECTING.orderManager.currentOrder = (TravelOrder) item;

                    if(SCONNECTING.orderManager.currentOrder != null && SCONNECTING.orderManager.currentOrder.IsPaid == 1){

                        SCONNECTING.orderManager.resetToLastOpenningOrder(new GetOneListener() {
                            @Override
                            public void onGetOne(Boolean success, BaseModel item) {

                                if (item == null) {

                                    SCONNECTING.driverManager.ChangeDriverStatusToReadyIfFree(new GetOneListener() {
                                        @Override
                                        public void onGetOne(Boolean success, BaseModel item) {

                                            SCONNECTING.orderScreen.mControlPanelView.invalidateReadyButton(null);

                                        }
                                    });
                                }


                            }

                        });


                    }
                }
                if (listener != null)
                    listener.onGetOne(success, item);
            }
        });

    }

}
