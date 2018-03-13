package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.entity.LocationObject;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by TrungDao on 8/6/16.
 */



@RealmClass
public class TravelComPrice extends RealmObject implements BaseModel {


    public TravelComPrice(){

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

    @SerializedName("Company")
    @Expose
    public String Company;

    @SerializedName("Team")
    @Expose
    public String Team;

    @SerializedName("CountryCode")
    @Expose
    public String Country;

    @SerializedName("PickupCenterLoc")
    @Expose
    public LocationObject PickupCenterLoc;

    @SerializedName("PickupRadian")
    @Expose
    public Double PickupRadian = 0.0;

    @SerializedName("VehicleType")
    @Expose
    public String VehicleType;

    @SerializedName("QualityService")
    @Expose
    public String QualityService;

    @SerializedName("Priority")
    @Expose
    public Integer Priority = 0;

    @SerializedName("IsEnable")
    @Expose
    public Integer IsEnable = 1;

    @SerializedName("IsActive")
    @Expose
    public Integer IsActive = 0;

    @SerializedName("EffectDateFrom")
    @Expose
    public Date EffectDateFrom ;

    @SerializedName("EffectDateTo")
    @Expose
    public Date EffectDateTo ;

    @SerializedName("TimeInDayFrom")
    @Expose
    public Double TimeInDayFrom = 0.0;

    @SerializedName("TimeInDayTo")
    @Expose
    public Double TimeInDayTo = 24.0 ;

    @SerializedName("FromKm")
    @Expose
    public Double FromKm = 0.0;

    @SerializedName("Currency")
    @Expose
    public String Currency;

    @SerializedName("OpenningPrice")
    @Expose
    public Double OpenningPrice = 0.0;

    @SerializedName("PricePerKm")
    @Expose
    public Double PricePerKm = 0.0;

}


