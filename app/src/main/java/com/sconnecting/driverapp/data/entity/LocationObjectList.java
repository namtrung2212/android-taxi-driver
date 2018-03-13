package com.sconnecting.driverapp.data.entity;


import com.sconnecting.driverapp.data.storages.client.RealmDouble;

import org.parceler.Parcel;

import java.util.List;

import io.realm.LocationObjectListRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by TrungDao on 8/9/16.
 */


@Parcel(implementations = { LocationObjectListRealmProxy.class },
        value = Parcel.Serialization.FIELD,
        analyze = { LocationObjectList.class },
        converter = LocationObjectListParcelConverter.class)
public class LocationObjectList extends RealmObject {

    private RealmList<RealmDouble> arrayList= new RealmList<>();


    public List<RealmDouble> getList(){
        return arrayList;
    }

    public LocationObjectList(){}


    public LocationObjectList(List<Double> list) {

        if (list != null) {

            for (Double obj : list) {

                arrayList.add(new RealmDouble(obj));

            }

        }
    }
}