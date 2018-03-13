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
 * Created by TrungDao on 8/2/16.
 */

@RealmClass
public class UserStatus extends RealmObject implements BaseModel {


    public UserStatus(){

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

    @SerializedName("User")
    @Expose
    public String User;

    @SerializedName("UserSetting")
    @Expose
    public String UserSetting;

    @SerializedName("Location")
    @Expose
    public LocationObject Location;

    @SerializedName("Address")
    @Expose
    public String Address;

    @SerializedName("Speed")
    @Expose
    public Double Speed = 0.0;

    @SerializedName("IsActivated")
    @Expose
    public Integer IsActivated = 0;

    @SerializedName("ActivatedDate")
    @Expose
    public Date ActivatedDate;

    @SerializedName("AutoLockChangeDate")
    @Expose
    public Date AutoLockChangeDate;

    @SerializedName("IsLocked")
    @Expose
    public Integer IsLocked = 0;

    @SerializedName("LockChangedDate")
    @Expose
    public Date LockChangedDate;

    @SerializedName("LockedReason")
    @Expose
    public String LockedReason;

    @SerializedName("LastLogin")
    @Expose
    public Date LastLogin;

    @SerializedName("LastOnline")
    @Expose
    public Date LastOnline;

    @SerializedName("IsOnline")
    @Expose
    public Integer IsOnline = 0;

    @SerializedName("MonthlyLocation")
    @Expose
    public LocationObject MonthlyLocation;

    @SerializedName("MonthlyDistance")
    @Expose
    public Double MonthlyDistance = 0.0;

    @SerializedName("ServedQty")
    @Expose
    public Integer ServedQty = 0;

    @SerializedName("VoidedBfPickupByDriver")
    @Expose
    public Integer VoidedBfPickupByDriver = 0;

    @SerializedName("VoidedBfPickupByUser")
    @Expose
    public Integer VoidedBfPickupByUser = 0;

    @SerializedName("VoidedAfPickupByDriver")
    @Expose
    public Integer VoidedAfPickupByDriver = 0;

    @SerializedName("VoidedAfPickupByUser")
    @Expose
    public Integer VoidedAfPickupByUser = 0;

}
