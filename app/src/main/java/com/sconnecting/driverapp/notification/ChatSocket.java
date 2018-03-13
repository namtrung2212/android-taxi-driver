package com.sconnecting.driverapp.notification;

import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.CollectionHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.models.TravelOrder;
import com.sconnecting.driverapp.data.models.TravelOrderChatting;

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

public class ChatSocket {


    public static String ServerURL = "";

    public Socket socket;

    public ChatSocketListener socketListener;

    public ChatSocket(){

        IO.Options opts = new IO.Options();
        opts.reconnection = true;
        opts.forceNew = true;
        // opts.forceNew = true;
        //  opts.query = "auth_token=" + authToken;


        try {
            socket = IO.socket(ServerURL , opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(socket != null)
            addHandlers();
    }

    public void connect(ChatSocketListener newSocketListener, final Completion listener){

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
                    socketListener.onChatSocketLogged(data);
                }
            }
        }).on("UserChatToDriver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (socketListener != null) {
                    Map<String, Object> data = null;
                    try {
                        data = CollectionHelper.toMap((JSONObject) args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socketListener.onUserChatToDriver(data);
                }
            }
        });
    }

    public void loggin() throws JSONException {


        JSONObject obj = new JSONObject();
        obj.put("DriverID", SCONNECTING.driverManager.CurrentDriver.id);

        socket.emit("DriverLogin", obj);
    }

}
