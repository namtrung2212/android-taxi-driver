package com.sconnecting.driverapp.data.controllers;

import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.models.QualityServiceType;


/**
 * Created by TrungDao on 7/26/16.
 */




public class QualityServiceTypeController extends BaseController<QualityServiceType> {


    public QualityServiceTypeController()
    {
        super(QualityServiceType.class);
    }

    public static void GetActiveTypesByCountry(String countryCode, final GetItemsListener listener){

        new BaseController<>(QualityServiceType.class).get("IsActive=1&CountryCode="+countryCode,listener);
    }


}




