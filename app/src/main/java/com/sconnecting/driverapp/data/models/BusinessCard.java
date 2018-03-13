package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import com.sconnecting.driverapp.data.entity.BaseModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


/**
 * Created by TrungDao on 7/26/16.
 */

@RealmClass
public class BusinessCard extends RealmObject implements BaseModel {

    public BusinessCard(){

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

    @SerializedName("CardNo")
    @Expose
    public String CardNo;


    @SerializedName("CardOwner")
    @Expose
    public String CardOwner;


    @SerializedName("AccountOwner")
    @Expose
    public String AccountOwner;


    @SerializedName("BusinessName")
    @Expose
    public String BusinessName;


    @SerializedName("Account")
    @Expose
    public String Account;


    @SerializedName("AccountNo")
    @Expose
    public String AccountNo;


    @SerializedName("Currency")
    @Expose
    public String Currency;

    @SerializedName("AutoActivateDate")
    @Expose
    public Date AutoActivateDate;


    @SerializedName("IsActivated")
    @Expose
    public Integer IsActivated = 0;


    @SerializedName("ActivatedDate")
    @Expose
    public Date ActivatedDate;

    @SerializedName("WillExpireDate")
    @Expose
    public Date WillExpireDate;


    @SerializedName("IsExpired")
    @Expose
    public Integer IsExpired = 0;


    @SerializedName("ExpiredDate")
    @Expose
    public Date ExpiredDate;

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


    @SerializedName("PaymentTotal")
    @Expose
    public Double PaymentTotal = 0.0;




}
