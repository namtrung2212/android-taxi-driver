package com.sconnecting.driverapp.base.listener;

import com.sconnecting.driverapp.data.entity.BaseModel;

public interface PostListener<T extends BaseModel>{

    public void onCompleted(Boolean success,T item);


}
