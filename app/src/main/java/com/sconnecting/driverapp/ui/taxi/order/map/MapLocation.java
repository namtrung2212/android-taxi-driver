package com.sconnecting.driverapp.ui.taxi.order.map;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;
import com.sconnecting.driverapp.data.models.TravelOrder;

/**
 * Created by TrungDao on 8/2/16.
 */

public class MapLocation {


    OrderScreen parent;
    Marker currentLocationDot;

    public TravelOrder CurrentOrder() {

        if( SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;

    }

    public MapLocation(OrderScreen scr) {
        parent = scr;

    }

    @SuppressWarnings("MissingPermission")
    public void onLocationAuthorized() {

        this.parent.mMapView.gmsMapView.setMyLocationEnabled(false);
    }

    public void changeLocation(Location location) {

        if(location == null)
            return;

        LatLng loc = new LatLng( location.getLatitude(), location.getLongitude());

        if(CurrentOrder() == null || (CurrentOrder().IsStopped() == false)) {


            if (this.parent.mMapView.shouldToMoveToCurrentLocaton && this.parent.mMapView != null && this.parent.mMapView.gmsMapView != null) {

                LatLngBounds bounds = new LatLngBounds.Builder().include(loc).build();

                int width = parent.getResources().getDisplayMetrics().widthPixels;
                int height = parent.getResources().getDisplayMetrics().heightPixels;

                if (this.parent.isMapReady) {
                    this.parent.mMapView.gmsMapView.setPadding(100, 270, 100, 400);
                    this.parent.mMapView.gmsMapView.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
                }
                // searchResultController.autocompleteBounds = bounds //TRUNG NOTED

                this.parent.mMapView.shouldToMoveToCurrentLocaton = false;
            }

            if (SCONNECTING.driverManager.CurrentDriverStatus != null  && SCONNECTING.driverManager.CurrentDriverStatus.Vehicle != null && parent.mMapMarkerManager.currentVehicle() != null) {

                parent.mMapMarkerManager.currentVehicle().updateLocation(location, new Completion() {
                    @Override
                    public void onCompleted() {

                        parent.mMapMarkerManager.currentVehicle().showCar(true, new Completion() {
                            @Override
                            public void onCompleted() {

                                parent.mMapMarkerManager.currentVehicle().moveToCarLocation();
                            }
                        });
                    }
                });

            }

        }else{

            parent.mMonitoringView.orderPanelView.invalidate(false,null);


        }

    }


}
