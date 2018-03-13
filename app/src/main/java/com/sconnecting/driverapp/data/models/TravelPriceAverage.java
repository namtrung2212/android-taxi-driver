package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import com.sconnecting.driverapp.data.entity.BaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by TrungDao on 8/2/16.
 */

@RealmClass
public class TravelPriceAverage extends RealmObject implements BaseModel {

    public TravelPriceAverage(){

    }

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

    @SerializedName("VehicleType")
    @Expose
    public String VehicleType;

    @SerializedName("QualityService")
    @Expose
    public String QualityService;

    @SerializedName("Currency")
    @Expose
    public String Currency;

    @SerializedName("PricePerKm")
    @Expose
    public Double PricePerKm;

    @SerializedName("OpenningPrice")
    @Expose
    public Double OpenningPrice;

}


