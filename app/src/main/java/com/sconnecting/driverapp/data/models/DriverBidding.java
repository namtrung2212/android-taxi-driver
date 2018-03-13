package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.entity.LocationObject;

import org.parceler.Parcel;

import java.util.Date;

import io.realm.DriverBiddingRealmProxy;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by TrungDao on 8/7/16.
 */

@Parcel(implementations = { DriverBiddingRealmProxy.class },
        value = Parcel.Serialization.FIELD,
        analyze = { DriverBidding.class })
public class DriverBidding extends RealmObject implements BaseModel {

    public DriverBidding(){

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

    @SerializedName("TravelOrder")
    @Expose
    public String TravelOrder;

    @SerializedName("Currency")
    @Expose
    public String Currency = "VND";

    @SerializedName("OrderPickupLoc")
    @Expose
    public LocationObject OrderPickupLoc;

    @SerializedName("OrderPickupPlace")
    @Expose
    public String OrderPickupPlace;

    @SerializedName("OrderPickupCountry")
    @Expose
    public String OrderPickupCountry;

    @SerializedName("OrderPickupTime")
    @Expose
    public Date OrderPickupTime;

    @SerializedName("OrderDropLoc")
    @Expose
    public LocationObject OrderDropLoc;

    @SerializedName("OrderDropPlace")
    @Expose
    public String OrderDropPlace;

    @SerializedName("OrderDuration")
    @Expose
    public Double OrderDuration = 0.0;

    @SerializedName("OrderDistance")
    @Expose
    public Double OrderDistance = 0.0;

    @SerializedName("OrderPolyline")
    @Expose
    public String OrderPolyline;


    @SerializedName("Driver")
    @Expose
    public String Driver;

    @SerializedName("WorkingPlan")
    @Expose
    public String WorkingPlan;

    @SerializedName("Company")
    @Expose
    public String Company;

    @SerializedName("Team")
    @Expose
    public String Team;

    @SerializedName("User")
    @Expose
    public String User;

    @SerializedName("Message")
    @Expose
    public String Message;

    @SerializedName("ExpireTime")
    @Expose
    public Date ExpireTime;

    @SerializedName("Status")
    @Expose
    public String Status;

}
