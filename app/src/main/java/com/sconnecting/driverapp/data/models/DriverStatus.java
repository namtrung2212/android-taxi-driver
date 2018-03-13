package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.entity.LocationObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by TrungDao on 8/2/16.
 */

@RealmClass
public class DriverStatus extends RealmObject implements BaseModel {

    public DriverStatus(){

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

    @SerializedName("Driver")
    @Expose
    public String Driver;

    @SerializedName("DriverName")
    @Expose
    public String DriverName;

    @SerializedName("CitizenID")
    @Expose
    public String CitizenID;

    @SerializedName("PhoneNo")
    @Expose
    public String PhoneNo;

    @SerializedName("WorkingPlan")
    @Expose
    public String WorkingPlan;

    @SerializedName("Company")
    @Expose
    public String Company;

    @SerializedName("CompanyName")
    @Expose
    public String CompanyName;

    @SerializedName("Team")
    @Expose
    public String Team;

    @SerializedName("TeamName")
    @Expose
    public String TeamName;

    @SerializedName("CountryCode")
    @Expose
    public String Country;

    @SerializedName("Vehicle")
    @Expose
    public String Vehicle;

    @SerializedName("VehicleType")
    @Expose
    public String VehicleType;

    @SerializedName("VehicleNo")
    @Expose
    public String VehicleNo;

    @SerializedName("VehicleBrand")
    @Expose
    public String VehicleBrand;

    @SerializedName("VehicleProvince")
    @Expose
    public String VehicleProvince;

    @SerializedName("QualityService")
    @Expose
    public String QualityService;

    @SerializedName("DriverSetting")
    @Expose
    public String DriverSetting;

    @SerializedName("Location")
    @Expose
    public LocationObject Location ;

    @SerializedName("Address")
    @Expose
    public String Address;

    @SerializedName("Degree")
    @Expose
    public Double Degree = 0.0;

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

    @SerializedName("IsReady")
    @Expose
    public Integer IsReady = 0;

    @SerializedName("IsVehicleTaken")
    @Expose
    public Integer IsVehicleTaken = 0;

    @SerializedName("MonthlyLocation")
    @Expose
    public LocationObject MonthlyLocation;

    @SerializedName("MonthlyDistance")
    @Expose
    public Double MonthlyDistance = 0.0;

    @SerializedName("Rating")
    @Expose
    public Integer Rating = 0;

    @SerializedName("RateCount")
    @Expose
    public Integer RateCount = 0;

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
