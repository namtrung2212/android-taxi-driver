package com.sconnecting.driverapp.base.listener;

import java.util.List;

import com.sconnecting.driverapp.data.entity.BaseModel;

public interface GetItemsListener<T extends BaseModel>{
    void onGetItems(Boolean success,List<T> list);
}


