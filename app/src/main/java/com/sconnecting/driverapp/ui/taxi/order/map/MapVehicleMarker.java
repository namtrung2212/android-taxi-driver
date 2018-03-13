package com.sconnecting.driverapp.ui.taxi.order.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.data.entity.LocationObject;
import com.sconnecting.driverapp.location.LocationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import com.sconnecting.driverapp.google.GoogleMapUtil;
import com.sconnecting.driverapp.data.models.DriverStatus;

/**
 * Created by TrungDao on 8/2/16.
 */

public class MapVehicleMarker {


    public Marker marker;

    public String driverId;
    public DriverStatus driverStatus;


    Boolean isShow = true;

    public OrderScreen parent;

    public MapVehicleMarker(OrderScreen parent, String driverId){

        this.parent = parent;
        this.driverId = driverId;
    }

    public void invalidate(final Boolean isAnimation,final Completion listener){

        if(driverStatus == null){

            if(marker != null)
                marker.setVisible(false);

            if(listener != null)
                listener.onCompleted();

            return;
        }
        if(this.marker == null){

            loadMarker(isAnimation,listener);

        }else{

            marker.setTitle(driverStatus.DriverName);
            marker.setVisible(isShow);

            if(driverStatus.Location != null ) {
                if (isShow && isAnimation) {
                    GoogleMapUtil.animateMarker(parent.mMapView.gmsMapView, marker, driverStatus.Location.getLatLng(), (long) 5000);
                } else {
                    marker.setPosition(driverStatus.Location.getLatLng());
                }
            }

            if(listener != null)
                listener.onCompleted();
        }


        if(this.parent.mMapView.gmsMapView != null){
            CameraPosition camera =  this.parent.mMapView.gmsMapView.getCameraPosition();
            if(camera != null)
                this.rotateCar((double)camera.bearing);

        }


    }

    private void loadMarker(final Boolean isAnimation,final Completion listener){

        if(driverStatus.Location != null ) {
            Picasso.with(this.parent).load(R.drawable.car).resize(72, 72).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    if(marker != null)
                        marker.remove();


                    marker = parent.mMapView.gmsMapView.addMarker(new MarkerOptions()
                            .position(driverStatus.Location.getLatLng())
                            .title(driverStatus.DriverName)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .anchor((float) 0.5, (float) 0.5)
                    );

                    marker.setVisible(isShow);

                    if (isShow && isAnimation) {
                        GoogleMapUtil.animateMarker(parent.mMapView.gmsMapView, marker, driverStatus.Location.getLatLng(), (long) 5000);
                    } else {
                        marker.setPosition(driverStatus.Location.getLatLng());
                    }


                    if (listener != null)
                        listener.onCompleted();

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                    if (listener != null)
                        listener.onCompleted();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        }
    }

    public void hideCar(final Boolean isAnimation,final Completion listener){

        isShow = false;
        invalidate(isAnimation, listener);
    }

    public void showCar(final Boolean isAnimation,final Completion listener){

        isShow = true;
        invalidate(isAnimation, listener);
    }

    public void rotateCar(Double degree){

        if(this.marker != null && this.driverStatus != null && this.driverStatus.Degree != null)
            this.marker.setRotation((float) (this.driverStatus.Degree - degree));

    }


    public void updateLocation(Location location, Completion listener) {

        updateLocation(location.getLatitude(),location.getLongitude(),location.getBearing(),listener);

    }
    public void updateLocation(Boolean isAnimation, Location location, Completion listener) {

        updateLocation(isAnimation,location.getLatitude(),location.getLongitude(),location.getBearing(),listener);

    }
    public void updateLocation(Double latitude, Double longitude ,float degree, Completion listener){

        Boolean isAnimation = true;

        if(this.driverStatus == null){
            this.driverStatus = new DriverStatus();
            this.driverStatus.Driver = this.driverId;
            isAnimation = false;
        }

        if(this.driverStatus.Location == null){
            isAnimation = false;

        }else{
            Location source = LocationHelper.newLocation(latitude , longitude);
            Location destiny =  this.driverStatus.Location.getLocation();
            if( Math.abs(source.distanceTo(destiny)) > 50){
                isAnimation = false;
            }

        }

        updateLocation(isAnimation,latitude,longitude,degree,listener);
    }

    public void updateLocation(Boolean isAnimation,  Double latitude , Double longitude ,float degree,final Completion listener) {

        if(this.driverStatus == null) {

            this.driverStatus = new DriverStatus();
            this.driverStatus.Driver = this.driverId;

        }

        this.driverStatus.Location = new LocationObject(latitude, longitude);

        this.driverStatus.Degree = Double.valueOf(degree);

        this.invalidate(isAnimation, new Completion() {
            @Override
            public void onCompleted() {

                if(listener !=null)
                    listener.onCompleted();
            }
        });

    }

    public Double distanceFromUser(){
        if( SCONNECTING.locationHelper.getLocation() != null && SCONNECTING.driverManager.CurrentDriver != null){
            if(this.driverStatus != null && this.driverStatus.Location != null){
                return (double)SCONNECTING.locationHelper.getLocation().distanceTo(this.driverStatus.Location.getLocation());
            }
        }

        return -1.0;
    }

    public Double distanceFromLocation(Location location) {

        if(this.driverStatus != null && this.driverStatus.Location != null){
            return (double)location.distanceTo(this.driverStatus.Location.getLocation());

         }

        return -1.0;
    }

    public void moveToCarLocation(){

        if(marker != null && driverStatus != null && driverStatus.Location != null) {
            LatLng coordinate = driverStatus.Location.getLatLng();

            this.parent.mMapView.moveToLocation(coordinate,null, (float)18,this.driverStatus.Degree);
        }
    }


}
