package com.sconnecting.driverapp.data.controllers;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import com.sconnecting.driverapp.base.GsonHelper;
import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.GetStringValueListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;
import cz.msebera.android.httpclient.Header;

import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.base.CryptoHelper;
import com.sconnecting.driverapp.data.models.Driver;
import com.sconnecting.driverapp.data.models.DriverSetting;
import com.sconnecting.driverapp.data.models.DriverStatus;
import com.sconnecting.driverapp.data.models.WorkingPlan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TrungDao on 8/2/16.
 */

public class DriverController extends BaseController<Driver> {


    public DriverController()
    {
        super(Driver.class);
    }



    public static void RequestForActivationCode(String CitizenID  , String countryCode, final GetStringValueListener listener ){

        new BaseController<>(DriverSetting.class).getString("Authenticate/SendVerifyCode", "country=" + countryCode + "&CitizenID=" + CitizenID, new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success,String value) {

                try {
                    JSONObject reader = new JSONObject(value);
                    String  result = null;

                    if(reader.has("request_id"))
                        result = reader.getString("request_id");

                    else if(reader.has("message"))
                        result = reader.getString("message");

                    if(listener != null)
                        listener.onCompleted(true,result);


                } catch (JSONException e) {
                    e.printStackTrace();

                    if(listener != null)
                        listener.onCompleted(false,null);
                }
            }
        });
    }


    public static void CheckForActivationCode(String request_id, String code, String CitizenID, final GetStringValueListener listener ){

        new BaseController<>(DriverSetting.class).getString("Authenticate/CheckVerifyCode", "request_id=" + request_id + "&code=" + code + "&CitizenID=" + CitizenID, new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success,String value) {

                String driverId = null;

                try {
                    JSONObject reader = new JSONObject(value);
                    driverId = reader.getString("message");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(listener != null)
                    listener.onCompleted(true,driverId);

            }
        });


    }

    public static void ActivateDriverAccount(String driverId , final Completion listener){

        new DriverController().getString("ActivateDriverAccount/" + driverId, null, new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success,String value) {

                if(listener != null)
                    listener.onCompleted();
            }

        });

    }

    public static void GetDefaultWorkingPlan(String driverId , final GetOneListener listener){

        new BaseController<>(WorkingPlan.class).getOne("GetDefaultWorkingPlan/" + driverId, null, listener);

    }

    public static void GetDriversUsingMyVehicle(String driverId , final GetItemsListener listener){

        new BaseController<>(DriverStatus.class).get("GetDriversUsingMyVehicle/" + driverId, null, listener);

    }


    public static void TakeDefaultVehicle(String driverId , final GetOneListener listener){

        new BaseController<>(DriverStatus.class).getOne("TakeDefaultVehicle/" + driverId, null, listener);

    }


    public static void HandOverVehicle(String driverId , final GetOneListener listener){

        new BaseController<>(DriverStatus.class).getOne("HandOverVehicle/" + driverId, null, listener);

    }

    public static void ChangeDriverReadyStatus(String driverId, final GetOneListener listener){

        new BaseController<>(DriverStatus.class).getOne("ChangeReadyStatus","driverId=" + driverId ,listener);

    }

    public static void ChangeDriverStatusToReadyIfFree(String driverId, final GetOneListener listener){

        new BaseController<>(DriverStatus.class).getOne("ChangeDriverStatusToReadyIfFree","driverId=" + driverId ,listener);

    }


    public static void GetNearestDrivers(LatLng coordinate,String vehicleType,String quality, Integer page,Integer pagesize, final GetItemsListener listener){

        String filter = "longtitude=" +  Location.convert(coordinate.longitude, Location.FORMAT_DEGREES).replace(",",".")
                + "&latitude=" + Location.convert(coordinate.latitude, Location.FORMAT_DEGREES).replace(",",".");


        filter =  filter   + ((vehicleType != null ) ? ("&vehicletype=" +  vehicleType) : "");
        filter =  filter   + ((quality != null ) ? ("&quality=" +  quality) : "");

        if(page != null){
            filter = filter + "&page=" + page.toString();
        }

        if(pagesize != null){
            filter = filter + "&pagesize=" + pagesize.toString();
        }

        String url = ServerStorage.ServerURL + "/DriverStatus/GetNearestDrivers?" + filter;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {


                List<DriverStatus> list = GsonHelper.toList(response, new TypeToken<ArrayList<DriverStatus>>(){}.getType());

                if(listener != null)
                    listener.onGetItems(true,list);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                if(listener != null)
                    listener.onGetItems(false,null);
            }


        });

    }



    public static void UpdateLocation(String driverId,Location location , final GetOneListener listener) {

        new BaseController<>(DriverStatus.class).getOne("UpdateLocation","driverId=" + driverId + "&lat=" + location.getLatitude()+ "&long=" + location.getLongitude()+ "&degree=" + location.getBearing() + "&speed=" + location.getSpeed() ,listener);

    }

}