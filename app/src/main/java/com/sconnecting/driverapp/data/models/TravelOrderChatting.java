package com.sconnecting.driverapp.data.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.entity.LocationObject;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.TravelOrderChattingRealmProxy;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = { TravelOrderChattingRealmProxy.class },
        value = Parcel.Serialization.FIELD,
        analyze = { TravelOrderChatting.class })
public class TravelOrderChatting extends RealmObject implements BaseModel, Serializable {

    public TravelOrderChatting(){

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

    @SerializedName("Order")
    @Expose
    public String Order;

    @SerializedName("User")
    @Expose
    public String User;

    @SerializedName("UserName")
    @Expose
    public String UserName;

    @SerializedName("Driver")
    @Expose
    public String Driver;

    @SerializedName("DriverName")
    @Expose
    public String DriverName;

    @SerializedName("CitizenID")
    @Expose
    public String CitizenID;

    @SerializedName("Vehicle")
    @Expose
    public String Vehicle;

    @SerializedName("VehicleType")
    @Expose
    public String VehicleType;

    @SerializedName("VehicleNo")
    @Expose
    public String VehicleNo;


    @SerializedName("IsUser")
    @Expose
    public Integer IsUser = 0;

    @SerializedName("IsViewed")
    @Expose
    public Integer IsViewed = 0;



    @SerializedName("Content")
    @Expose
    public String Content;

    @SerializedName("ImageIDs")
    @Expose
    public String ImageIDs;



    @SerializedName("Location")
    @Expose
    public LocationObject Location;




}