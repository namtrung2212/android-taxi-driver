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
public class UserSetting extends RealmObject implements BaseModel {

    public UserSetting(){

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

    @SerializedName("Language")
    @Expose
    public String Language;

    @SerializedName("Currency")
    @Expose
    public String Currency;

    @SerializedName("Device")
    @Expose
    public String Device;

    @SerializedName("DeviceID")
    @Expose
    public String DeviceID;

    @SerializedName("IsVerified")
    @Expose
    public Integer IsVerified = 0;

    @SerializedName("VerifiedDate")
    @Expose
    public Date VerifiedDate;

}
