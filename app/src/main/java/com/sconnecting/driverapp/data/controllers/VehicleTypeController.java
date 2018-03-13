package com.sconnecting.driverapp.data.controllers;

import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.models.VehicleType;


/**
 * Created by TrungDao on 7/26/16.
 */




public class VehicleTypeController extends BaseController<VehicleType> {


    public VehicleTypeController()
    {
        super(VehicleType.class);
    }

    public static void GetActiveTypesByCountry(String countryCode, final GetItemsListener listener){

        new BaseController<>(VehicleType.class).get("IsActive=1&CountryCode="+countryCode,listener);
    }


}




