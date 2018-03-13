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
public class WorkingPlan extends RealmObject implements BaseModel {

    public WorkingPlan(){

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

    @SerializedName("IsLeader")
    @Expose
    public Integer IsLeader = 0;

    @SerializedName("Priority")
    @Expose
    public Integer Priority = 0;

    @SerializedName("IsEnable")
    @Expose
    public Integer IsEnable = 1;

    @SerializedName("IsActive")
    @Expose
    public Integer IsActive = 0;

    @SerializedName("FromDateTime")
    @Expose
    public Date FromDateTime;

    @SerializedName("ToDateTime")
    @Expose
    public Date ToDateTime;


}
