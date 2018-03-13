package com.sconnecting.driverapp.ui.taxi.order.monitoring.message;

import com.sconnecting.driverapp.data.models.TravelOrder;
import com.sconnecting.driverapp.data.models.TravelOrderChatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungDao on 8/26/16.
 */

public class TravelChattingObject implements Comparable<TravelChattingObject> {


    public TravelOrder order;
    public TravelOrderChatting cellObject;
    public Float preferWidth;

    public  TravelChattingObject(TravelOrder order, TravelOrderChatting chatting){
        this.order = order;
        this.cellObject = chatting;

    }
    @Override
    public int compareTo(TravelChattingObject another) {
        long diff = this.cellObject.createdAt.getTime() - another.cellObject.createdAt.getTime();
        if (diff > 0)
            return 1;
        else if (diff < 0)
            return -1;
        else
            return 0;
    }


    public interface GetChatObjectsListener {
        void onGetObjects(Boolean success,List<TravelChattingObject> list);
    }


    public static void fromArray(TravelOrder order , List<TravelOrderChatting> arrItems, final GetChatObjectsListener listener) {

        List<TravelChattingObject> arrObjects = new ArrayList<>();

        if(arrItems != null) {
            for (TravelOrderChatting chatting : arrItems) {
                arrObjects.add(new TravelChattingObject(order, chatting));
            }
        }

        if(listener != null)
            listener.onGetObjects(true,arrObjects);
    }

}
