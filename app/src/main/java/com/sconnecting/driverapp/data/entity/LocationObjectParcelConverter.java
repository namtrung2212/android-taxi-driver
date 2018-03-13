package com.sconnecting.driverapp.data.entity;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

/**
 * Created by TrungDao on 8/11/16.
 */

public class LocationObjectParcelConverter implements ParcelConverter<LocationObject> {
    
    @Override
    public void toParcel(LocationObject input, Parcel parcel) {

        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeParcelable(Parcels.wrap(input), 0);

        }
    }

    @Override
    public LocationObject fromParcel(Parcel parcel) {

        int isNull = parcel.readInt();
        if (isNull < 0) return null;

        LocationObject item = (LocationObject) Parcels.unwrap(parcel.readParcelable(LocationObject.class.getClassLoader()));
        return item;
    }
}