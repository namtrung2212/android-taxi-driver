package com.sconnecting.driverapp.data.storages.client;

import io.realm.RealmObject;

/**
 * Created by TrungDao on 8/9/16.
 */

public class RealmDouble extends RealmObject {

    private Double value;

    public RealmDouble(){}

    public RealmDouble(Double value){
        this.value =  value;
    }


    public Double getDoubleValue() {
        return value;
    }

    public void setDoubleValue(Double value) {
        this.value = value;
    }

}