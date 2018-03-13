package com.sconnecting.driverapp.data.storages.client;

import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.data.entity.BaseModel;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by TrungDao on 8/9/16.
 */

public class ClientCaching <T extends BaseModel>{

    public Class<T> clazz;

    public ClientCaching(Class<T> clazz)
    {
        this.clazz = clazz;
    }


    static Realm realm;

    public static  Realm getRealmInstance(){

        if(realm != null)
            return realm;

        Realm.init(AppDelegate.getContext());
        realm =  Realm.getInstance(
                new RealmConfiguration.Builder()
                        .name("SCONNECTING.Caching.Realm")
                        .deleteRealmIfMigrationNeeded()
                        .build()
        );

        return realm;
    }
    public  Boolean isExpired(String URL){

        Realm realm = getRealmInstance();

        CachingInfo cachInfo= realm.where(CachingInfo.class).equalTo("URL",URL).equalTo("ModelType",this.clazz.getSimpleName()).findFirst();


        return  cachInfo == null || cachInfo.ExpireTime.before(new Date());

    }


    public Boolean isExpired(T item){

        Double cachingMInutes = ClientCachingConfig.getCachingMinutes(this.clazz.getSimpleName());
        return cachingMInutes <= 0.0 || (item.getRetrieveAt() != null &&
               new Date(item.getRetrieveAt().getTime() + (long)(cachingMInutes*60*1000)).before(new Date()));

    }


    public void set(String URL ,Double newExpireMinutes ){

        Realm realm = getRealmInstance();

        CachingInfo cachInfo= realm.where(CachingInfo.class).equalTo("URL",URL).equalTo("ModelType",this.clazz.getSimpleName()).findFirst();

        Double minutes = ClientCachingConfig.getCachingMinutes(this.clazz.getSimpleName());
        if( newExpireMinutes != null ){
            minutes = newExpireMinutes;
        }

        if(minutes == null || minutes <= 0.0)
            return;

        if ( cachInfo != null && cachInfo.RetrievedAt != null){


                Date newexpireTime = new Date(cachInfo.RetrievedAt.getTime() + (long)(minutes * 60 *1000));

                realm.beginTransaction();

                cachInfo.ExpireTime = newexpireTime;

                realm.insertOrUpdate(cachInfo);
                realm.commitTransaction();


        }else{

            realm.beginTransaction();
            cachInfo = new CachingInfo (URL, this.clazz.getSimpleName(), minutes);
            realm.insertOrUpdate(cachInfo);
            realm.commitTransaction();

        }

    }



}
