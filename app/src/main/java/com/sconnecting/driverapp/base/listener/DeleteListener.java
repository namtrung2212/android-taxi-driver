package com.sconnecting.driverapp.base.listener;

import com.sconnecting.driverapp.data.entity.BaseModel;

/**
 * Created by TrungDao on 7/28/16.
 */

public interface DeleteListener<T extends BaseModel>{

    public void onDeleted(Boolean success,Integer deleted);

}