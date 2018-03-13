package com.sconnecting.driverapp.manager;

import android.os.Handler;
import android.os.Looper;

import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.DeviceHelper;
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.PostListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.notification.ChatSocketListener;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.models.TravelOrderChatting;

import java.util.Date;
import java.util.Map;

/**
 * Created by TrungDao on 9/19/16.
 */

public class OrderChattingHandler implements ChatSocketListener {

    OrderManager manager;

    public OrderChattingHandler(OrderManager manager){
        this.manager = manager;
    }

    public void DriverChatToUser(final String content, final GetOneListener listener) {

        TravelOrderChatting obj = new TravelOrderChatting();

        obj.Order = manager.currentOrder.id;
        obj.User = manager.currentOrder.User;
        obj.UserName = manager.currentOrder.UserName;
        obj.Driver = manager.currentOrder.Driver;
        obj.DriverName = manager.currentOrder.DriverName;
        obj.Vehicle = manager.currentOrder.Vehicle;
        obj.VehicleType = manager.currentOrder.VehicleType;
        obj.VehicleNo = manager.currentOrder.VehicleNo;
        obj.CitizenID = manager.currentOrder.CitizenID;
        // obj.Location = locationHelper.currentLocation!.Location
        obj.IsUser = 0;
        obj.IsViewed = 0;
        obj.Content = content;

        obj.createdAt = new Date();
        obj.updatedAt = new Date();

        new BaseController<>(TravelOrderChatting.class).create(obj,"DriverChatToUser", new PostListener() {
            @Override
            public void onCompleted(Boolean success,BaseModel item) {

                    if(listener != null)
                        listener.onGetOne(true,item);
            }
        });
    }

    @Override
    public void onChatSocketLogged(final String socketId) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onChatSocketLogged(socketId);
                }
            });

        } else {

        }
    }

    @Override
    public void onUserChatToDriver(final Map<String, Object> data) {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onUserChatToDriver(data);
                }
            });

        } else {

            // let arrData = data[0] as! [String: AnyObject]

            String userId = data.get("UserID").toString();
            String driverId = data.get("DriverID").toString();
            final String orderId = data.get("OrderID").toString();
            final String contentId = data.get("ContentID").toString();
            final String content = data.get("Content").toString();

            if (manager.currentOrder != null && manager.currentOrder.getId() != null && manager.currentOrder.getId().equals(orderId)) {


                DeviceHelper.playDefaultNotificationSound();

                SCONNECTING.orderScreen.mMonitoringView.userProfileView.increaseMessageNo(1, new GetDoubleValueListener() {
                    @Override
                    public void onCompleted(Boolean success, Double number) {


                    }
                });

                SCONNECTING.orderScreen.mMonitoringView.userProfileView.invalidateLastMessage(false, content, new Completion() {
                    @Override
                    public void onCompleted() {


                    }
                });

                SCONNECTING.orderScreen.mMonitoringView.userProfileView.chattingView.chattingTable.addItemFromUser(contentId, content, new Completion() {
                    @Override
                    public void onCompleted() {

                    }
                });

                //if(SCONNECTING.orderScreen.mMonitoringView.isCollapsedProfile == false) {
                SCONNECTING.orderScreen.mMonitoringView.userProfileView.chattingView.chattingTable.loadNewData(new Completion() {
                    @Override
                    public void onCompleted() {

                    }
                });
                //}
            }
        }
    }

}