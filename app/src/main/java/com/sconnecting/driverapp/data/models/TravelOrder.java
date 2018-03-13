package com.sconnecting.driverapp.data.models;


import java.io.Serializable;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.entity.LocationObject;
import com.sconnecting.driverapp.data.entity.LocationObjectList;
import com.sconnecting.driverapp.base.DateTimeHelper;

import org.parceler.Parcel;
import io.realm.RealmObject;
import io.realm.TravelOrderRealmProxy;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = { TravelOrderRealmProxy.class },
        value = Parcel.Serialization.FIELD,
        analyze = { TravelOrder.class })
public class TravelOrder  extends RealmObject implements BaseModel, Serializable {

    public TravelOrder(){

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


    @SerializedName("UserName")
    @Expose
    public String UserName;

    @SerializedName("User")
    @Expose
    public String User;
    
    @SerializedName("OrderLoc")
    @Expose
    public LocationObject OrderLoc;

    @SerializedName("Device")
    @Expose
    public String Device;

    @SerializedName("DeviceID")
    @Expose
    public String DeviceID;

    @SerializedName("Status")
    @Expose
    public String Status = OrderStatus.Open;

    @SerializedName("Currency")
    @Expose
    public String Currency = "VND";

    @SerializedName("OrderPickupLoc")
    @Expose
    public LocationObject OrderPickupLoc;

    @SerializedName("OrderPickupPlace")
    @Expose
    public String OrderPickupPlace;

    @SerializedName("OrderPickupCountry")
    @Expose
    public String OrderPickupCountry;

    @SerializedName("OrderPickupTime")
    @Expose
    public Date OrderPickupTime;

    @SerializedName("OrderDropLoc")
    @Expose
    public LocationObject OrderDropLoc;

    @SerializedName("OrderDropPlace")
    @Expose
    public String OrderDropPlace;

    @SerializedName("OrderDuration")
    @Expose
    public Double OrderDuration = 0.0;

    @SerializedName("OrderDistance")
    @Expose
    public Double OrderDistance = 0.0;

    @SerializedName("OrderPolyline")
    @Expose
    public String OrderPolyline;

    @SerializedName("OrderCompanyPrice")
    @Expose
    public Double OrderCompanyPrice = 0.0;

    @SerializedName("OrderCompanyAdjust")
    @Expose
    public Double OrderCompanyAdjust = 0.0;

    @SerializedName("OrderCompanyProm")
    @Expose
    public Double OrderCompanyProm = 0.0;

    @SerializedName("OrderSysProm")
    @Expose
    public Double OrderSysProm = 0.0;

    @SerializedName("OrderPrice")
    @Expose
    public Double OrderPrice = 0.0;

    @SerializedName("OrderVehicleType")
    @Expose
    public String OrderVehicleType;

    @SerializedName("OrderQuality")
    @Expose
    public String OrderQuality;

    @SerializedName("OrderDropRestrict")
    @Expose
    public Integer OrderDropRestrict;




    @SerializedName("ActPickupLoc")
    @Expose
    public LocationObject ActPickupLoc;

    @SerializedName("ActPickupPlace")
    @Expose
    public String ActPickupPlace;

    @SerializedName("ActPickupCountry")
    @Expose
    public String ActPickupCountry;

    @SerializedName("ActPickupTime")
    @Expose
    public Date ActPickupTime;

    @SerializedName("ActDropLoc")
    @Expose
    public LocationObject ActDropLoc;

    @SerializedName("ActDropPlace")
    @Expose
    public String ActDropPlace;

    @SerializedName("ActDropTime")
    @Expose
    public Date ActDropTime;

    @SerializedName("ActDistance")
    @Expose
    public Double ActDistance = 0.0;

    @SerializedName("ActPolyline")
    @Expose
    public String ActPolyline;

    @SerializedName("ActCompanyPrice")
    @Expose
    public Double ActCompanyPrice = 0.0;

    @SerializedName("ActCompanyAdjust")
    @Expose
    public Double ActCompanyAdjust = 0.0;

    @SerializedName("ActCompanyProm")
    @Expose
    public Double ActCompanyProm = 0.0;

    @SerializedName("ActSysProm")
    @Expose
    public Double ActSysProm = 0.0;

    @SerializedName("ActPrice")
    @Expose
    public Double ActPrice = 0.0;




    @SerializedName("MembersQty")
    @Expose
    public Integer MembersQty = 1 ;




    @SerializedName("IsMateHost")
    @Expose
    public Integer IsMateHost = 0 ;

    @SerializedName("MateHostOrder")
    @Expose
    public String MateHostOrder;

    @SerializedName("MaxSubMembers")
    @Expose
    public Integer MaxSubMembers = 0 ;

    @SerializedName("MateSubMembers")
    @Expose
    public Integer MateSubMembers = 0 ;

    @SerializedName("MateSubWeight")
    @Expose
    public Integer MateSubWeight = 0 ;




    @SerializedName("HostPoints")
    @Expose
    public LocationObjectList HostPoints;

    @SerializedName("HostPolyline")
    @Expose
    public String HostPolyline;

    @SerializedName("HostOrderDistance")
    @Expose
    public Double HostOrderDistance = 0.0 ;

    @SerializedName("HostOrderPrice")
    @Expose
    public Double HostOrderPrice = 0.0 ;

    @SerializedName("MateOrderPrice")
    @Expose
    public Double MateOrderPrice = 0.0 ;

    @SerializedName("MateOrderDistance")
    @Expose
    public Double MateOrderDistance = 0.0 ;

    @SerializedName("MateActPrice")
    @Expose
    public Double MateActPrice = 0.0 ;
    
    

    @SerializedName("MustPay")
    @Expose
    public Double MustPay = 0.0 ;

    @SerializedName("PayMethod")
    @Expose
    public String PayMethod;

    @SerializedName("PayCurrency")
    @Expose
    public String PayCurrency;

    @SerializedName("PayAmount")
    @Expose
    public Double PayAmount = 0.0;

    @SerializedName("BusinessCard")
    @Expose
    public String BusinessCard;

    @SerializedName("UserPayCard")
    @Expose
    public String UserPayCard;

    @SerializedName("PayTransNo")
    @Expose
    public String PayTransNo;

    @SerializedName("PayTransDate")
    @Expose
    public Date PayTransDate;

    @SerializedName("PayVerifyCode")
    @Expose
    public String PayVerifyCode;

    @SerializedName("IsVerified")
    @Expose
    public Integer IsVerified = 0;

    @SerializedName("IsPayTransSucceed")
    @Expose
    public Integer IsPayTransSucceed = 0 ;

    @SerializedName("IsPaid")
    @Expose
    public Integer IsPaid = 0;

    @SerializedName("IsPaidReconcile")
    @Expose
    public Integer IsPaidReconcile;


    @SerializedName("Driver")
    @Expose
    public String Driver;

    @SerializedName("DriverName")
    @Expose
    public String DriverName;

    @SerializedName("WorkingPlan")
    @Expose
    public String WorkingPlan;

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

    @SerializedName("QualityService")
    @Expose
    public String QualityService;

    @SerializedName("Rating")
    @Expose
    public Integer Rating = 0;

    @SerializedName("Comment")
    @Expose
    public String Comment;




    public void resetToOpen(){

        this.Status = OrderStatus.Open;
        this.WorkingPlan = null;
        this.Driver = null;
        this.DriverName = null;
        this.CitizenID = null;
        this.Company = null;
        this.CompanyName = null;
        this.Team = null;
        this.TeamName = null;
        this.Vehicle = null;
        this.VehicleNo = null;
        this.VehicleType = null;
        this.VehicleBrand = null;
        this.QualityService = null;
    }


    public Boolean IsPickupNow(){
        return (this.OrderPickupTime == null) || (DateTimeHelper.isNow(this.OrderPickupTime,10));
    }


    public Boolean IsPickupFuture(){
        return (this.OrderPickupTime != null) && (this.OrderPickupTime.getTime() - new Date().getTime() > 60*1);
    }




    public Boolean IsNewOrder(){
        return this.id == null;
    }


    public Boolean IsChoosingLocation(){
        return this.IsNewOrder() &&  ( this.OrderDropLoc == null ||  this.OrderPickupLoc == null );
    }

    public Boolean IsChoosingPickupLocation(){
        return this.IsNewOrder() &&  ( this.OrderPickupLoc == null );
    }


    public Boolean IsChoosingDropLocation(){
        return this.IsNewOrder() &&  (this.OrderDropLoc == null  && this.OrderPickupLoc != null);
    }




    public Boolean IsNotYetChooseDriver(){
        return this.Status == null || (this.Status.equals(OrderStatus.Open) || this.Status.equals(OrderStatus.BiddingAccepted) || this.Status.equals(OrderStatus.Requested) || this.Status.equals(OrderStatus.DriverRejected)  );
    }

    public Boolean IsExpired(){
        return this.IsNotYetChooseDriver() && this.OrderPickupTime != null && DateTimeHelper.isExpired(this.OrderPickupTime,2);
    }


    public Boolean IsDriverRequested() {
        return  this.Status != null && (this.Status.equals(OrderStatus.Requested) || this.Status.equals(OrderStatus.BiddingAccepted) );
    }

    public Boolean IsDriverRejected() {
        return this.Status != null && (this.Status.equals(OrderStatus.DriverRejected)  );
    }


    public Boolean IsWaitingDriver(){
        return this.Status != null && (this.Driver != null) && (this.Status.equals(OrderStatus.DriverAccepted)  ||  this.Status.equals(OrderStatus.DriverPicking) );
    }


    public Boolean IsDriverAccepted(){
        return this.Status != null && (this.Driver != null) && (this.Status.equals(OrderStatus.DriverAccepted) );
    }

    public Boolean IsDriverPicking(){
        return this.Status != null && (this.Driver != null) && (this.Status.equals(OrderStatus.DriverPicking) );
    }

    public Boolean IsMonitoring(){
        return this.Status != null && (this.Driver != null) && (this.Status.equals(OrderStatus.DriverPicking) ||  this.Status.equals(OrderStatus.Pickuped) );
    }


    public Boolean IsStopped() {
        return this.Status != null && (this.Driver != null) &&
                (this.Status.equals(OrderStatus.VoidedBfPickupByUser)
                        ||  this.Status.equals(OrderStatus.VoidedBfPickupByDriver)
                        ||  this.Status.equals(OrderStatus.VoidedAfPickupByUser)
                        ||  this.Status.equals(OrderStatus.VoidedAfPickupByDriver)
                        ||  this.Status.equals(OrderStatus.Finished));
    }

    public Boolean IsVoided() {
        return this.Status != null && (this.Driver != null) &&
                (this.Status.equals(OrderStatus.VoidedBfPickupByUser)
                        ||  this.Status.equals(OrderStatus.VoidedBfPickupByDriver)
                        ||  this.Status.equals(OrderStatus.VoidedAfPickupByUser)
                        ||  this.Status.equals(OrderStatus.VoidedAfPickupByDriver ));
    }

    public Boolean isTripMateMember(){

        return (this.MateHostOrder != null &&
                this.Status.equals(OrderStatus.Open) == false &&
                this.Status.equals(OrderStatus.Requested) == false &&
                this.Status.equals(OrderStatus.BiddingAccepted) == false &&
                this.Status.equals(OrderStatus.DriverAccepted) == false &&
                this.Status.equals(OrderStatus.DriverRejected) == false &&
                this.Status.equals(OrderStatus.TripMateRequested) == false &&
                this.Status.equals(OrderStatus.TripMateRejected) == false &&
                this.Status.equals(OrderStatus.VoidedBfPickupByUser) == false &&
                this.Status.equals(OrderStatus.VoidedBfPickupByDriver) == false );

    }

    public Boolean IsVoidedByUser(){
        return this.Status != null && (this.Driver != null) &&  (this.Status.equals(OrderStatus.VoidedBfPickupByUser) || this.Status.equals(OrderStatus.VoidedAfPickupByUser));
    }


    public Boolean IsVoidedByDriver(){
        return this.Status != null && (this.Driver != null) &&  (this.Status.equals(OrderStatus.VoidedBfPickupByDriver) || this.Status.equals(OrderStatus.VoidedAfPickupByDriver));
    }


    public Boolean IsOnTheWay(){
        return this.Status != null && (this.Driver != null) &&  (this.Status.equals(OrderStatus.Pickuped));
    }

    public Boolean IsFinishedNotYetPaid(){
        return this.Status != null && (this.Driver != null) &&  (this.Status.equals(OrderStatus.Finished) || this.Status.equals(OrderStatus.VoidedAfPickupByUser) || this.Status.equals(OrderStatus.VoidedAfPickupByDriver) ) && this.IsPaid == 0;
    }

    public Boolean IsFinishedAndPaid(){
        return this.Status != null && (this.Driver != null) &&  (this.Status.equals(OrderStatus.Finished) || this.Status.equals(OrderStatus.VoidedAfPickupByUser) || this.Status.equals(OrderStatus.VoidedAfPickupByDriver) ) && this.IsPaid == 1;

    }




    public void  initRoadPath(Double distance , Double duration){

        this.OrderDistance = distance;
        this.OrderDuration = duration;

    }



    public void  initOrderLoc(LatLng target){

        this.OrderLoc = new LocationObject(target);

    }

    public void  initOrderPickupLoc(LatLng target){

        this.OrderPickupLoc = new LocationObject(target);

    }

    public void  initOrderPickupLoc(Double latitude ,Double longitude){

        this.OrderPickupLoc = new LocationObject(latitude,longitude);

    }

    public void  initActPickupLoc(LatLng target){

        this.ActPickupLoc = new LocationObject(target);
    }


    public void  initOrderDropLoc(LatLng target){

        this.OrderDropLoc = new LocationObject(target);
    }

    public void  initActDropLoc(LatLng target){

        this.ActDropLoc = new LocationObject(target);
    }

    public void  initOrderDropLoc(Double latitude ,Double longitude){

        this.OrderDropLoc = new LocationObject(latitude,longitude);
    }

    public void  initOrderPickupPlace(String address,String country){
        this.OrderPickupPlace = address;
        this.OrderPickupCountry = country;
    }

    public void  initOrderDropPlace(String target){
        this.OrderDropPlace = target;
    }

    public void  clearOrderPickupLoc(){

        this.OrderPickupLoc = null;
        this.OrderDistance = 0.0;
        this.OrderDuration = 0.0;
    }

    public void  clearOrderDropLoc(){
        this.OrderDropLoc = null;
        this.OrderDistance = 0.0;
        this.OrderDuration = 0.0;
    }



    public String getPickupTimeString() {

        Date date;
        if(this.ActPickupTime != null){

            date = this.ActPickupTime;

        }else if(this.OrderPickupTime != null){

            date = this.OrderPickupTime;

        }else{

            date = this.createdAt;

        }

        if(date == null){
            return "";
        }else{
            return DateTimeHelper.toVietnamese(date);
        }


    }


}