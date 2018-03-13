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
public class BusinessAccount extends RealmObject implements BaseModel {

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


    @SerializedName("AccountNo")
    @Expose
    public String AccountNo;

    @SerializedName("AccountOwner")
    @Expose
    public String AccountOwner;

    @SerializedName("BusinessName")
    @Expose
    public String BusinessName;

    @SerializedName("IsPersonal")
    @Expose
    public Integer IsPersonal;

    @SerializedName("Currency")
    @Expose
    public String Currency;

    @SerializedName("Manager")
    @Expose
    public String Manager;


    @SerializedName("AutoActivateDate")
    @Expose
    public Date AutoActivateDate;

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

    @SerializedName("CashInTotal")
    @Expose
    public Double CashInTotal;

    @SerializedName("CashOutTotal")
    @Expose
    public Double CashOutTotal;

    @SerializedName("CashTransferInTotal")
    @Expose
    public Double CashTransferInTotal;

    @SerializedName("CashTransferOutTotal")
    @Expose
    public Double CashTransferOutTotal;

    @SerializedName("Balance")
    @Expose
    public Double Balance = 0.0;


    public BusinessAccount(){

    }

}
