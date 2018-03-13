package com.sconnecting.driverapp.ui.taxi.order.map;

import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;

/**
 * Created by TrungDao on 8/2/16.
 */

public class MapMarkerManager {


    MapVehicleMarker _currentVehicle;

    public MapVehicleMarker currentVehicle(){

        if(_currentVehicle != null) {
            _currentVehicle.driverStatus = SCONNECTING.driverManager.CurrentDriverStatus;
            return _currentVehicle;
        }

        _currentVehicle = new MapVehicleMarker(parent,SCONNECTING.driverManager.CurrentDriver.getId());
        _currentVehicle.driverStatus = SCONNECTING.driverManager.CurrentDriverStatus;

        SCONNECTING.locationHelper.getLocation();

        return _currentVehicle;
    }

    OrderScreen parent;

    public MapMarkerManager(OrderScreen parent){

        this.parent = parent;
    }



}
