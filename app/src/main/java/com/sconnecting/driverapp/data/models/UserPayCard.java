

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
public class UserPayCard extends RealmObject implements BaseModel {

    public UserPayCard(){

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

    @SerializedName("Currency")
    @Expose
    public String Currency;

    @SerializedName("Bank")
    @Expose
    public String Bank;

    @SerializedName("BankAcc")
    @Expose
    public String BankAcc;

    @SerializedName("BankAccOwner")
    @Expose
    public String BankAccOwner;

    @SerializedName("CardType")
    @Expose
    public String CardType;

    @SerializedName("CardNo")
    @Expose
    public String CardNo;

    @SerializedName("CardExpireDate")
    @Expose
    public Date CardExpireDate;

    @SerializedName("SecurityCode")
    @Expose
    public String SecurityCode;

    @SerializedName("IsVerified")
    @Expose
    public Integer IsVerified = 0;

    @SerializedName("IsExpired")
    @Expose
    public Integer IsExpired = 0;

    @SerializedName("IsLocked")
    @Expose
    public Integer IsLocked = 0;


}
