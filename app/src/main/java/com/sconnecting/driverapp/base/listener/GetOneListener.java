package com.sconnecting.driverapp.base.listener;

import com.sconnecting.driverapp.data.entity.BaseModel;

public interface GetOneListener<T extends BaseModel>{

    void onGetOne(Boolean success,T item);

}
