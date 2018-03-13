package com.sconnecting.driverapp.data.entity;

import android.os.Parcel;

import com.sconnecting.driverapp.data.storages.client.RealmDouble;

import org.parceler.ParcelConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungDao on 8/11/16.
 */

public class LocationObjectListParcelConverter implements ParcelConverter<LocationObjectList> {

    @Override
    public void toParcel(LocationObjectList input, Parcel parcel) {

        if (input == null) {
            parcel.writeInt(-1);
        }
        else {

            double[] arr = new double[input.getList().size()];

            int i = -1;
            for (RealmDouble obj : input.getList()) {
                i++;
                arr[i] = obj.getDoubleValue();
            }

            parcel.writeInt(1);
            parcel.writeInt(arr.length);
            for (int j=0; j<arr.length; j++) {
                parcel.writeDouble(arr[i]);
            }

        }
    }

    @Override
    public LocationObjectList fromParcel(Parcel parcel) {

        int isNull = parcel.readInt();
        if (isNull < 0) return null;

        int iSize = parcel.readInt();

        List<Double> list = new ArrayList<>();
        for (int j=0; j<iSize; j++) {
            list.add(parcel.readDouble());
        }

        return new LocationObjectList(list);
    }
}