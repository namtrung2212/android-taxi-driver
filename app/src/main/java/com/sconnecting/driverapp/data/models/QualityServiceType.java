package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sconnecting.driverapp.data.entity.BaseModel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


/**
 * Created by TrungDao on 7/26/16.
 */

@RealmClass
public class QualityServiceType extends RealmObject implements BaseModel {

    @PrimaryKey
    @SerializedName("_id")
    @Expose
    public String id;

    @SerializedName("updatedAt")
    @Expose
    public Date updatedAt;

    @SerializedName("createdAt")
    @Expose
    public Date createdAt;

    public Date retrieveAt;
    public Date useAt;

    public String getId() {
        return id;
    }
    public Date getRetrieveAt() {
        return retrieveAt;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public Date getUsedAt() {
        return useAt;
    }
    public void setRetrieveAt(Date value){ this.retrieveAt = value;}
    public void setUsedAt(Date value){ this.useAt = value;}

    public boolean isNew(){
        return id == null || id.trim().isEmpty();

    }

    @SerializedName("CountryCode")
    @Expose
    public String Country;

    @SerializedName("Name")
    @Expose
    public String Name;

    @SerializedName("LocaleName")
    @Expose
    public String LocaleName;

    @SerializedName("EffectDateFrom")
    @Expose
    public Date EffectDateFrom ;

    @SerializedName("EffectDateTo")
    @Expose
    public Date EffectDateTo ;


    @SerializedName("IsEnable")
    @Expose
    public Integer IsEnable = 1;

    @SerializedName("IsActive")
    @Expose
    public Integer IsActive = 0;
}
