package com.sconnecting.driverapp.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import com.sconnecting.driverapp.data.entity.BaseModel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


/**
 * Created by TrungDao on 7/26/16.
 */

@RealmClass
public class Business extends RealmObject implements BaseModel{

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

    @SerializedName("CorpName")
    @Expose
    public String  CorpName;

    @SerializedName("CorpAddress")
    @Expose
    public String  CorpAddress;

    @SerializedName("CorpCountry")
    @Expose
    public String  CorpCountry;

    @SerializedName("CorpProvince")
    @Expose
    public String  CorpProvince;

    @SerializedName("CorpHotline")
    @Expose
    public String  CorpHotline;

    @SerializedName("CorpHRHotline")
    @Expose
    public String  CorpHRHotline;

    @SerializedName("CorpHRContact")
    @Expose
    public String  CorpHRContact;

    @SerializedName("CorpFIHotline")
    @Expose
    public String  CorpFIHotline;

    @SerializedName("CorpFIContact")
    @Expose
    public String  CorpFIContact;

    @SerializedName("CorpEmailAddr")
    @Expose
    public String  CorpEmailAddr;

    @SerializedName("CorpHREmailAddr")
    @Expose
    public String  CorpHREmailAddr;

    @SerializedName("CorpTaxNo")
    @Expose
    public String  CorpFIEmailAddr;

    @SerializedName("CorpTaxNo")
    @Expose
    public String  CorpTaxNo;

    @SerializedName("PersonName")
    @Expose
    public String  PersonName;

    @SerializedName("PersonBirthday")
    @Expose
    public String  PersonBirthday;

    @SerializedName("PersonCitizenID")
    @Expose
    public String  PersonCitizenID;

    @SerializedName("PersonCitizenIDDate")
    @Expose
    public Date    PersonCitizenIDDate;

    @SerializedName("PersonCountry")
    @Expose
    public String  PersonCountry;

    @SerializedName("PersonProvince")
    @Expose
    public String  PersonProvince;

    @SerializedName("PersonGender")
    @Expose
    public String  PersonGender;

    @SerializedName("PersonPhoneNo")
    @Expose
    public String  PersonPhoneNo;

    @SerializedName("PersonEmailAddr")
    @Expose
    public String  PersonEmailAddr;

    @SerializedName("PersonTaxNo")
    @Expose
    public String  PersonTaxNo;

    @SerializedName("Represent")
    @Expose
    public String  Represent;
    


    public Business(){

    }
}
