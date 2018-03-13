package com.sconnecting.driverapp.data.models;

import java.util.Date;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.sconnecting.driverapp.data.entity.BaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


/**
 * Created by TrungDao on 7/26/16.
 */

@RealmClass
public class User extends RealmObject implements BaseModel {

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

    @SerializedName("UserStatus")
    @Expose
    public String UserStatus;

    @SerializedName("UserSetting")
    @Expose
    public String UserSetting;

    @SerializedName("EmailAddr")
    @Expose
    public String EmailAddr;

    @SerializedName("PhoneNo")
    @Expose
    public String PhoneNo;

    @SerializedName("Gender")
    @Expose
    public String Gender;

    @SerializedName("Province")
    @Expose
    public String Province;

    @SerializedName("CountryCode")
    @Expose
    public String Country;

    @SerializedName("CitizenIDDate")
    @Expose
    public Date CitizenIDDate;


    @SerializedName("CitizenID")
    @Expose
    public String CitizenID;


    @SerializedName("Birthday")
    @Expose
    public Date Birthday;


    @SerializedName("Name")
    @Expose
    public String Name;



}
