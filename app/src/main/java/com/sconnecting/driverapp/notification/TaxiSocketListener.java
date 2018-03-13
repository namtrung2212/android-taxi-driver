package com.sconnecting.driverapp.notification;

import java.util.Map;

/**
 * Created by TrungDao on 8/7/16.
 */

public interface TaxiSocketListener {


    void onTaxiSocketLogged(String socketId);

    void onCarUpdateLocation(Map<String,Object>  data);

    void onUserRequestTaxi(Map<String,Object>  data);

    void onUserCancelRequest(Map<String,Object>  data);

    void onUserAcceptBidding(Map<String,Object>  data);

    void onUserCancelAcceptingBidding(Map<String,Object>  data);


    void onUserVoidedBfPickup(Map<String,Object>  data);

    void onUserVoidedAfPickup(Map<String,Object>  data);

    void onUserPaid(Map<String,Object>  data);

    void onDriverShouldInvalidateOrder(Map<String,Object>  data);

    void onCheckAppInForeground(Map<String,Object>  data);

}



