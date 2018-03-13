package com.sconnecting.driverapp.data.entity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import io.realm.LocationObjectRealmProxy;
import io.realm.RealmObject;

/**
 * Created by TrungDao on 8/9/16.
 */


@Parcel(implementations = { LocationObjectRealmProxy.class },
        value = Parcel.Serialization.FIELD,
        analyze = { LocationObject.class })
public class LocationObject extends RealmObject {

    public Double latitude;
    public Double longitude;

    public LocationObject(){}

    public LocationObject(LatLng value){

        if(value !=null){

            latitude = value.latitude;
            longitude = value.longitude;
        }else {

            latitude = null;
            longitude = null;
        }

    }
    public LocationObject(double latitude,double longitude){

        this.latitude = latitude;
        this.longitude = longitude;

    }


    public LatLng getLatLng() {

        if(latitude == null || longitude == null)
            return  null;

        return new LatLng(latitude,longitude);
    }

    public Location getLocation(){
            Location loc = new Location("locationHelper");
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            return  loc;
    }

    public void setLatLng(LatLng value) {

        if(value !=null){

            latitude = value.latitude;
            longitude = value.longitude;
        }else {

            latitude = null;
            longitude = null;
        }
    }

}