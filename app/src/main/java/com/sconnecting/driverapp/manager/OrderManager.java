package com.sconnecting.driverapp.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;

import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetBoolValueListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.controllers.TravelOrderController;
import com.sconnecting.driverapp.data.models.TravelOrder;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;

import org.parceler.Parcels;

/**
 * Created by TrungDao on 8/2/16.
 */

public class OrderManager {


    public TravelOrder currentOrder;

    Handler autoInvalidateTimer;

    public OrderEventHandler eventHandler = new OrderEventHandler(this);
    public OrderActionHandler actionHandler = new OrderActionHandler(this);
    public OrderChattingHandler chattingHandler = new OrderChattingHandler(this);

    public OrderManager() {

    }


    public OrderManager(TravelOrder order) {

        currentOrder = order;

    }

    public void start(final Completion listener) {

        connectToNotificationServer(listener);

    }


    public void connectToNotificationServer(final Completion listener) {

        SCONNECTING.notificationHelper.taxiSocket.connect(eventHandler, new Completion() {
            @Override
            public void onCompleted() {

                SCONNECTING.notificationHelper.chatSocket.connect(chattingHandler, new Completion() {
                    @Override
                    public void onCompleted() {

                        if (Looper.myLooper() != Looper.getMainLooper()) {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (listener != null)
                                        listener.onCompleted();
                                }
                            });

                        }


                    }
                });

            }
        });
    }


    public void reset(Boolean updateUI, final Completion listener) {

        reset(null, updateUI, new Completion() {
            @Override
            public void onCompleted() {

                if (listener != null)
                    listener.onCompleted();
            }
        });

    }

    public void reset(TravelOrder order, Boolean updateUI, final Completion listener) {

        currentOrder = order;

        invalidate(true, updateUI, listener);

    }

    public void invalidate(final Boolean isFirstTime, String orderId, final Boolean updateUI, final Completion listener) {

        connectToNotificationServer(null);

        new BaseController<>(TravelOrder.class).getById(true, orderId, new GetOneListener() {

            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if (success && item != null) {

                    currentOrder = (TravelOrder) item;

                    if (updateUI) {
                        invalidateUI(isFirstTime, listener);
                        return;
                    }
                }


                if (listener != null)
                    listener.onCompleted();
            }
        });


    }

    public void invalidate(final Boolean isFirstTime, final Boolean updateUI, final Completion listener) {


        if (currentOrder != null && currentOrder.getId() != null) {

            invalidate(isFirstTime,currentOrder.getId(),updateUI,listener);

        } else {

            connectToNotificationServer(null);

            if (updateUI) {
                SCONNECTING.orderScreen.invalidateUI(isFirstTime, listener);
            } else {
                if (listener != null)
                    listener.onCompleted();
            }
        }


    }

    public void invalidateUI(Boolean isFirstTime, final Completion listener) {

        SCONNECTING.orderScreen.invalidateUI(isFirstTime, listener);

        startInvalidateTimer(20);

    }


    void startInvalidateTimer(final int seconds) {


        if (autoInvalidateTimer == null) {
            autoInvalidateTimer = new Handler(Looper.getMainLooper());

            autoInvalidateTimer.postDelayed(new Runnable() {
                @Override
                public void run() {

                    autoInvalidateTimer.removeCallbacks(this);

                    final Runnable that = this;

                    autoInvalidate(new GetBoolValueListener() {
                        @Override
                        public void onCompleted(Boolean success, Boolean value) {

                            autoInvalidateTimer.postDelayed(that, 1000 * seconds);

                        }
                    });
                }
            }, 1000 * seconds);
        }


    }

    void autoInvalidate(final GetBoolValueListener listener) {

        invalidate(false, true, new Completion() {
            @Override
            public void onCompleted() {

                if (listener != null)
                    listener.onCompleted(true, true);

            }
        });
    }

    public void getOpenningOrder(final GetOneListener listener) {

        if (SCONNECTING.driverManager.CurrentDriver != null) {

            TravelOrderController.GetLastOpenningOrderByDriver(SCONNECTING.driverManager.CurrentDriver.getId(), listener);

        } else {
            if (listener != null)
                listener.onGetOne(false, null);
        }

    }


    public void resetToLastOpenningOrder(final GetOneListener listener) {

        if (SCONNECTING.driverManager.CurrentDriver != null) {

            TravelOrderController.GetLastOpenningOrderByDriver(SCONNECTING.driverManager.CurrentDriver.getId(), new GetOneListener() {
                @Override
                public void onGetOne(Boolean success, BaseModel item) {


                    if (AppDelegate.CurrentActivity instanceof OrderScreen && SCONNECTING.orderScreen != null) {

                        if (item != null)
                            SCONNECTING.orderManager.reset((TravelOrder) item, true, null);
                        else
                            SCONNECTING.orderManager.reset(null,true, null);

                    } else {

                        Intent intent = new Intent(AppDelegate.CurrentActivity, OrderScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (item != null) {
                            Parcelable wrappedCurrentOrder = Parcels.wrap((TravelOrder) item);
                            intent.putExtra("CurrentOrder", wrappedCurrentOrder);
                        }

                        ((Activity) AppDelegate.CurrentActivity).startActivity(intent);
                        ((Activity) AppDelegate.CurrentActivity).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                    }
                    if (listener != null)
                        listener.onGetOne(success, item);
                }
            });

        } else {
            if (listener != null)
                listener.onGetOne(false, null);
        }

    }


}
