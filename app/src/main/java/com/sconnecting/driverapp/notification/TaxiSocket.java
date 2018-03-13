package com.sconnecting.driverapp.notification;

import android.location.Location;

import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.CollectionHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.models.TravelOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by TrungDao on 8/7/16.
 */

public class TaxiSocket {


    public static String ServerURL = "";

    public Socket socket;

    public TaxiSocketListener socketListener;

    public TaxiSocket(){

        IO.Options opts = new IO.Options();
        opts.reconnection = true;
        opts.forceNew = true;
      //  opts.query = "auth_token=" + authToken;


        try {
            socket = IO.socket(ServerURL , opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(socket != null)
            addHandlers();
    }

    public void connect(TaxiSocketListener newSocketListener, final Completion listener){

        socketListener = newSocketListener;

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                try {
                    loggin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(listener != null)
                    listener.onCompleted();
            }

        });


        socket.connect();

    }

    public void addHandlers() {

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socket.connect();
            }

        }).on("DriverLogged", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                   String data = null;
                    try {
                        data = args[0].toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    socketListener.onTaxiSocketLogged(data);
                }
            }
        }).on("CarUpdateLocation", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onCarUpdateLocation(data);
                }
            }
        }).on("UserRequestTaxi", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onUserRequestTaxi(data);
                }
            }
        }).on("UserCancelRequest", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onUserCancelRequest(data);
                }
            }
        }).on("UserAcceptBidding", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onUserAcceptBidding(data);
                }
            }
        }).on("UserCancelAcceptingBidding", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onUserCancelAcceptingBidding(data);
                }
            }
        }).on("UserVoidedBfPickup", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onUserVoidedBfPickup(data);
                }
            }
        }).on("UserVoidedAfPickup", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onUserVoidedAfPickup(data);
                }
            }
        }).on("UserPaid", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onUserPaid(data);
                }
            }
        }).on("DriverShouldInvalidateOrder", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onDriverShouldInvalidateOrder(data);
                }
            }
        }).on("CheckAppInForeground", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onCheckAppInForeground(data);
                }
            }
        });
    }

    public void loggin() throws JSONException {


        JSONObject obj = new JSONObject();
        obj.put("DriverID", SCONNECTING.driverManager.CurrentDriver.id);

        socket.emit("DriverLogin", obj);
    }

    public void resetCar() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("DriverID", SCONNECTING.driverManager.CurrentDriver.getId());


        if(SCONNECTING.driverManager.CurrentDriverStatus.Vehicle != null){

            obj.put("VehicleID", SCONNECTING.driverManager.CurrentDriverStatus.Vehicle);

        }else{

            if(SCONNECTING.driverManager.CurrentWorkingPlan != null && SCONNECTING.driverManager.CurrentWorkingPlan.Vehicle != null){

                obj.put("VehicleID", SCONNECTING.driverManager.CurrentWorkingPlan.Vehicle);

            }
        }

        socket.emit("DriverResetCar", obj);


    }


    public void updateLocation(TravelOrder order, Location location) throws JSONException{


        JSONObject obj = new JSONObject();
        obj.put("VehicleID",  SCONNECTING.driverManager.CurrentDriverStatus.Vehicle);
        obj.put("DriverID", SCONNECTING.driverManager.CurrentDriver.getId());
        obj.put("UserID",  order.User);
        obj.put("OrderID", order.getId());
        obj.put("latitude",location.getLatitude());
        obj.put("longitude",location.getLongitude());
        obj.put("degree",location.getBearing());

        socket.emit("VehicleUpdateLocation", obj);

    }

    public void DriverAppInForeground() throws JSONException{

        JSONObject obj = new JSONObject();
        obj.put("DriverID", SCONNECTING.driverManager.CurrentDriver.getId());

        socket.emit("DriverAppInForeground", obj);

    }

    public void DriverAppInBackground() throws JSONException{

        JSONObject obj = new JSONObject();
        obj.put("DriverID", SCONNECTING.driverManager.CurrentDriver.getId());

        socket.emit("DriverAppInBackground", obj);

    }


}
