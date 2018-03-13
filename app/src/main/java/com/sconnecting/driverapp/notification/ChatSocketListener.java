package com.sconnecting.driverapp.notification;

import java.util.Map;

/**
 * Created by TrungDao on 8/22/16.
 */

public interface ChatSocketListener {


    void onChatSocketLogged(String socketId);

    void onUserChatToDriver(Map<String,Object>  data);
}

