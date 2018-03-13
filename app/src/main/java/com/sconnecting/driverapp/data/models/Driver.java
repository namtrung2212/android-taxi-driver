package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sconnecting.driverapp.data.entity.BaseModel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by TrungDao on 8/2/16.
 */

@RealmClass
public class Driver extends RealmObject implements BaseModel {

    public Driver(){

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

    @SerializedName("Name")
    @Expose
    public String Name;

    @SerializedName("Birthday")
    @Expose
    public Date Birthday;

    @SerializedName("CitizenID")
    @Expose
    public String CitizenID;

    @SerializedName("CitizenIDDate")
    @Expose
    public Date CitizenIDDate;

    @SerializedName("CountryCode")
    @Expose
    public String Country;

    @SerializedName("Province")
    @Expose
    public String Province;

    @SerializedName("Gender")
    @Expose
    public String Gender;

    @SerializedName("PhoneNo")
    @Expose
    public String PhoneNo;

    @SerializedName("EmailAddr")
    @Expose
    public String EmailAddr;

    @SerializedName("DriverSetting")
    @Expose
    public String DriverSetting;

    @SerializedName("DriverStatus")
    @Expose
    public String DriverStatus;


}
