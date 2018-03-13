package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sconnecting.driverapp.data.entity.BaseModel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by TrungDao on 8/5/16.
 */


@RealmClass
public class BusinessCardBudget extends RealmObject implements BaseModel {

    public BusinessCardBudget(){

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


    @SerializedName("Card")
    @Expose
    public String Card;

    @SerializedName("CardNo")
    @Expose
    public String CardNo;

    @SerializedName("Account")
    @Expose
    public String Account;

    @SerializedName("AccountNo")
    @Expose
    public String AccountNo;

    @SerializedName("AccountOwner")
    @Expose
    public String AccountOwner;

    @SerializedName("BusinessName")
    @Expose
    public String BusinessName;

    @SerializedName("Currency")
    @Expose
    public String Currency;

    @SerializedName("AssignedUser")
    @Expose
    public String AssignedUser;

    @SerializedName("IsOverBudget")
    @Expose
    public Integer IsOverBudget = 0;

    @SerializedName("PayableAmount")
    @Expose
    public Double PayableAmount = 0.0;

    @SerializedName("WeeklyBudget")
    @Expose
    public Double WeeklyBudget = 0.0;

    @SerializedName("MonthlyBudget")
    @Expose
    public Double MonthlyBudget = 0.0;

    @SerializedName("QuarterlyBudget")
    @Expose
    public Double QuarterlyBudget = 0.0;

    @SerializedName("YearlyBudget")
    @Expose
    public Double YearlyBudget = 0.0;

    @SerializedName("CurrentWeekFrom")
    @Expose
    public Date CurrentWeekFrom;

    @SerializedName("CurrentWeekTo")
    @Expose
    public Date CurrentWeekTo;

    @SerializedName("CurrentMonth")
    @Expose
    public Double CurrentMonth = 0.0;

    @SerializedName("CurrentQuarter")
    @Expose
    public Double CurrentQuarter = 0.0;

    @SerializedName("CurrentYear")
    @Expose
    public Double CurrentYear = 0.0;

    @SerializedName("PaidCurrentWeek")
    @Expose
    public Double PaidCurrentWeek = 0.0;

    @SerializedName("PaidCurrentMonth")
    @Expose
    public Double PaidCurrentMonth = 0.0;

    @SerializedName("PaidCurrentQuarter")
    @Expose
    public Double PaidCurrentQuarter = 0.0;

    @SerializedName("PaidCurrentYear")
    @Expose
    public Double PaidCurrentYear = 0.0;

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


}
