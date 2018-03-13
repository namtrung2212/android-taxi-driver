package com.sconnecting.driverapp.data.storages.client;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by TrungDao on 8/9/16.
 */

public class CachingInfo  extends RealmObject {


    @PrimaryKey
    public String id;

    public String URL;
    public String ModelType;

    public Date RetrievedAt;
    public Date ExpireTime;


    public String getId() {
        return id;
    }

    public CachingInfo(String URL,String  ModelType,Double expireMinutes){

        this.id = ModelType + URL;
        this.URL = URL;
        this.ModelType = ModelType;
        this.RetrievedAt = new Date();
        this.ExpireTime = new Date(RetrievedAt.getTime() +  (long)(expireMinutes * 60 * 1000));

    }


    public CachingInfo(){

    }


}
