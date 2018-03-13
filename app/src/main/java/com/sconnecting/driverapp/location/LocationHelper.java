package com.sconnecting.driverapp.location;

/**
 * Created by TrungDao on 7/31/16.
 */


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.maps.model.LatLng;
import com.sconnecting.driverapp.SCONNECTING;

import java.util.ArrayList;
import java.util.List;

import com.sconnecting.driverapp.data.entity.LocationObject;
import com.sconnecting.driverapp.google.GooglePlaceSearcher;
import com.sconnecting.driverapp.google.LocationInfo;
import com.sconnecting.driverapp.base.listener.Completion;

public class LocationHelper extends Service implements LocationListener{

    public Location location = null;
    double latitude;
    double longitude;
    public String CountryCode;
    public String Province;
    public String Locality;
    public String PostalCode;

    public Location firstLocation;

    public static Location newLocation(LatLng latLong) {
        Location loc = new Location("locationHelper");
        loc.setLatitude(latLong.latitude);
        loc.setLongitude(latLong.longitude);
        return loc;
    }

    public static Location newLocation(Double latitude, Double longitude) {
        Location loc = new Location("locationHelper");
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);
        return loc;
    }

    public static List<Double> newArrLocation(Double latitude, Double longitude) {
        List<Double> result = new ArrayList<>();
        result.add(latitude);
        result.add(longitude);
        return result;
    }

    public static LatLng newLatLng(List<Double> latLong) {
        if (latLong == null || latLong.size() < 2)
            return null;

        return new LatLng(latLong.get(0), latLong.get(1));
    }

    public static LatLng newLatLng(Location loc) {
        if (loc == null)
            return null;

        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    private final Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;


    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 20; // 50 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5; // 30 second

    protected android.location.LocationManager locationManager;

    public LocationHelper(Context context) {
        this.mContext = context;


    }


    @SuppressWarnings("MissingPermission")
    public Location getLocation() {

        try {
            locationManager = (android.location.LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

            if (locationManager == null || ( !isGPSEnabled && !isNetworkEnabled)) {


            } else {

                this.canGetLocation = true;

                if (isGPSEnabled) {

                    Location loc = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
                    if (loc != null)
                        location = loc;

                    locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                    }
                    return location;
                }

                if (isNetworkEnabled) {

                    Location loc = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
                    if(loc != null)
                        location = loc;

                    locationManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                    }
                    return location;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }
        return location;
    }


    public double getLatitude() {

        location = getLocation();
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    public double getLongitude() {

        location = getLocation();
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public LatLng getLatLng(){
        return new LatLng(getLatitude(),getLongitude());
    }

    public LocationObject getLocationObject(){

        return  new LocationObject(getLatitude(),getLongitude() );
    }

    @Override
    public void onLocationChanged(Location newloc) {

        Location oldLoc = location;

        if (firstLocation == null || oldLoc == null || (newloc != null && newloc.distanceTo(oldLoc) >= MIN_DISTANCE_CHANGE_FOR_UPDATES)) {

            location = newloc;
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if(firstLocation == null || oldLoc == null) {
                new GooglePlaceSearcher().getAddress(new LatLng(location.getLatitude(), location.getLongitude()), new GooglePlaceSearcher.GetPlaceListener() {
                    @Override
                    public void onCompleted(LocationInfo locAddress) {

                        if (locAddress != null) {

                            CountryCode = locAddress.CountryCode;
                            Province = locAddress.Province;
                            Locality = locAddress.Locality;
                            PostalCode = locAddress.PostalCode;
                        }

                    }
                });


                if(SCONNECTING.orderScreen.locationReadyListener != null)
                    SCONNECTING.orderScreen.locationReadyListener.onReady();

            }

            if(SCONNECTING.orderScreen != null && SCONNECTING.orderScreen.mMapLocation != null){
                SCONNECTING.orderScreen.mMapLocation.changeLocation(location);
            }


            SCONNECTING.driverManager.updatePositionHistory(new Completion() {
                @Override
                public void onCompleted() {

                }
            });


            firstLocation = location;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}