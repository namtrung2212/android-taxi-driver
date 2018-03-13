package com.sconnecting.driverapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetBoolValueListener;
import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.GetStringValueListener;
import com.sconnecting.driverapp.base.listener.PostListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.controllers.DriverController;
import com.sconnecting.driverapp.data.models.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by TrungDao on 8/2/16.
 */

public class DriverManager {


    public Driver CurrentDriver;
    public DriverSetting CurrentDriverSetting;
    public DriverStatus CurrentDriverStatus;
    public WorkingPlan CurrentWorkingPlan;


    public static  String Token(){

        return  AppDelegate.getContext().getSharedPreferences("SCONNECTING", Context.MODE_PRIVATE).getString("Token", null);
    }


    public static  String DefaultDriverID(){

       return  AppDelegate.getContext().getSharedPreferences("SCONNECTING", Context.MODE_PRIVATE).getString("DefaultDriverID", null);
    }


    public void login( String driverId, final GetBoolValueListener listener){

        registerNewDevice(driverId, new GetBoolValueListener() {
            @Override
            public void onCompleted(Boolean success, Boolean value) {

                if(success){

                    initCurrentDriver(listener);

                }else {
                    if(listener != null)
                        listener.onCompleted(false,false);
                }
            }

        });


    }

    public void registerNewDevice(final String driverId , final  GetBoolValueListener listener){

        requestNewToken(driverId, new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success, String token) {

                if(!success){
                    if(listener != null)
                        listener.onCompleted(false,false);
                    return;
                }

                new BaseController<>(DriverSetting.class).getOneByStringField(true,"Driver", driverId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success,BaseModel item) {

                        if(!success || item == null ){
                            if(listener != null)
                                listener.onCompleted(false,false);
                            return;
                        }

                        final DriverSetting newItem = (DriverSetting)item;
                        newItem.Device = Build.MODEL;
                        newItem.DeviceID = Build.SERIAL;

                        new BaseController<>(DriverSetting.class).update(newItem, new PostListener() {
                            @Override
                            public void onCompleted(Boolean success,BaseModel obj) {

                                if(success && obj != null){

                                    CurrentDriverSetting = (DriverSetting)obj;

                                    SharedPreferences preferences = AppDelegate.getContext().getSharedPreferences("SCONNECTING", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("DefaultDriverID", driverId);
                                    editor.commit();


                                    String FCMToken = preferences.getString("DriverFCMToken", null);
                                    if(FCMToken != null && FCMToken.isEmpty() == false) {

                                        String url = AppDelegate.ServerURL + "/FCM/updateToken?driverId=" + driverId + "&FCMToken=" + FCMToken;
                                        new AsyncHttpClient().get(url, new TextHttpResponseHandler() {

                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, String response) {


                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                                            }


                                        });

                                    }else {

                                        try {
                                            FirebaseInstanceId.getInstance().deleteInstanceId();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        FirebaseInstanceId.getInstance().getToken();
                                    }

                                }

                                if(listener != null)
                                    listener.onCompleted(success,obj != null);

                            }
                        });
                    }
                });
            }
        });




    }

    public static void isValidDevice(final GetBoolValueListener listener){

        final String driverId = DefaultDriverID();

        if (driverId == null || driverId.isEmpty() ) {
            if(listener != null)
                listener.onCompleted(true,false);

            return;
        }

        requestNewToken(driverId,new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success, String token) {

                if(!success){
                    if(listener != null)
                        listener.onCompleted(true,false);
                    return;
                }
                new BaseController<>(DriverSetting.class).getOneByStringField(true,"Driver", driverId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel item) {

                        DriverSetting setting = (DriverSetting) item;
                        if(setting != null){

                            Boolean isValid = setting.Device.equals(Build.MODEL)  &&  setting.DeviceID.equals(Build.SERIAL);
                            if(listener != null)
                                listener.onCompleted(true,isValid);
                        }else{

                            if(listener != null)
                                listener.onCompleted(true,false);
                        }

                    }
                });
            }
        });





    }

    public static void requestNewToken(final String driverId, final GetStringValueListener listener){

        String url = ServerStorage.ServerURL + "/Authenticate/Driver/GetNewToken";

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity entity = null;
        try {
            JSONObject params = new JSONObject();
            params.put("id", driverId);
            entity = new StringEntity(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        client.post(AppDelegate.getContext(),url,entity,"text/plain",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if( response.has("success")) {

                    Boolean success = null;
                    try {
                        success = response.getBoolean("success");
                        if (success) {

                            String token = response.getString("token");
                            SharedPreferences preferences = AppDelegate.getContext().getSharedPreferences("SCONNECTING", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Token", token);
                            editor.commit();

                            if(listener != null)
                                listener.onCompleted(true,token);

                        }else{

                            if(listener != null)
                                listener.onCompleted(false,null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(listener != null)
                            listener.onCompleted(false,null);
                    }
                }else{

                    if(listener != null)
                        listener.onCompleted(false,null);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                if(listener != null)
                    listener.onCompleted(false,null);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }



        });
    }
   
    public static void isValidToken(final String driverId, final GetBoolValueListener listener){

        final String token = Token();

        if (token == null || token.isEmpty() ) {
            if(listener != null)
                listener.onCompleted(false,false);
            return;
        }

        String url = ServerStorage.ServerURL + "/Authenticate/Driver/CheckToken";

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type" , "application/x-www-form-urlencoded");
        client.setURLEncodingEnabled(true);

        Map<String,String> params = new HashMap<String, String>();
        params.put("id",driverId);
        params.put("token",token);

        client.post(url, new RequestParams(params), new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response.has("success")) {

                    Boolean success = null;
                    try {
                        success = response.getBoolean("success");
                        if (listener != null)
                            listener.onCompleted(true, success);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (listener != null)
                            listener.onCompleted(false, false);
                    }
                } else {

                    if (listener != null)
                        listener.onCompleted(false, false);
                }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                if (listener != null)
                    listener.onCompleted(false, false);

            }

        });



    }

    public void initCurrentDriver(final GetBoolValueListener listener){

        final String driverId = DefaultDriverID();

        if (driverId == null || driverId.isEmpty() ){
            if(listener != null)
                listener.onCompleted(false, false);
            return;
        }


        new BaseController<>(Driver.class).getById(true,driverId, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success,BaseModel driver) {

                CurrentDriver = (Driver) driver;
                CurrentDriverStatus = null;
                CurrentWorkingPlan = null;

                if(driver == null) {

                    if(listener != null)
                        listener.onCompleted(true, false);
                }

                new BaseController<>(DriverSetting.class).getOneByStringField(true,"Driver", driverId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success,BaseModel obj) {
                        CurrentDriverSetting = (DriverSetting)obj;
                    }
                });


                new DriverController().GetDefaultWorkingPlan(driverId, new GetOneListener() {
                    @Override
                    public void onGetOne(Boolean success, BaseModel workingplan) {

                        CurrentWorkingPlan = (WorkingPlan)workingplan;

                        invalidateStatus(new Completion() {
                            @Override
                            public void onCompleted() {

                                initCurrentVehicle(new GetBoolValueListener() {
                                    @Override
                                    public void onCompleted(Boolean success, Boolean value) {


                                        if(CurrentWorkingPlan == null){

                                            new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Chưa được giao xe")
                                                    .setContentText("Vui lòng liên hệ quản lý để giao nhận xe. \r\n Bạn cũng có thể nhận xe từ tài khác.")
                                                    .setConfirmText("Đồng ý")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            if(listener != null)
                                                                listener.onCompleted(true,false);

                                                            sweetAlertDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .show();

                                            return;

                                        }

                                        if(listener != null)
                                            listener.onCompleted(true,CurrentDriverStatus != null);

                                    }
                                });

                            }
                        });

                    }
                });




            }
        });

    }

    public void initCurrentVehicle(final GetBoolValueListener listener){

        if( CurrentWorkingPlan == null || CurrentWorkingPlan.Vehicle == null){

            if(listener != null)
                listener.onCompleted(true,false);

            return;
        }

        new DriverController().GetDriversUsingMyVehicle(CurrentDriver.getId(), new GetItemsListener() {
            @Override
            public void onGetItems(Boolean success, List statuses) {

                List<DriverStatus> otherDrivers = (List<DriverStatus> ) statuses;
                if(otherDrivers != null && otherDrivers.size() > 0){

                        new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Xe " + CurrentWorkingPlan.VehicleNo + " đã giao cho tài khác")
                                .setContentText(String.format("Tài xế %s đang sử dụng xe. \r\n Bạn có thể theo dõi toạ độ xe hoặc đề nghị đổi xe.", otherDrivers.get(0).DriverName))
                                .setConfirmText("Theo dõi")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        if(listener != null)
                                            listener.onCompleted(true,false);

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .setCancelText("Gọi tài")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                       /*
                                        if let url = NSURL(string: "tel://\(otherDrivers![0].PhoneNo!)") {
                                            UIApplication.sharedApplication().openURL(url)
                                        }
                                        */

                                        if(listener != null)
                                            listener.onCompleted(true,false);

                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();


                }else{

                        if(CurrentDriverStatus.Vehicle == null || CurrentDriverStatus.IsVehicleTaken == 0){

                            new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Bạn đã nhận xe " + CurrentWorkingPlan.VehicleNo + " ?")
                                    .setContentText("Xác nhận để bắt đầu hoạt động. \r\n Bỏ qua để theo dõi toạ độ xe.")
                                    .setConfirmText("Đã nhận")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            new DriverController().TakeDefaultVehicle(CurrentDriver.getId(), new GetOneListener() {
                                                @Override
                                                public void onGetOne(Boolean success, BaseModel item) {

                                                    if(item != null){
                                                        CurrentDriverStatus = (DriverStatus)item;
                                                    }
                                                    if(listener != null)
                                                        listener.onCompleted(true,true);
                                                }
                                            });

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .setCancelText("Theo dõi")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            if(listener != null)
                                                listener.onCompleted(true,false);

                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();


                        }else{

                            if(listener != null)
                                listener.onCompleted(true,true);

                        }
                }

            }

        });


    }

    public void invalidateStatus(final Completion listener){

        if(CurrentDriver != null){
            new BaseController<>(DriverStatus.class).getOneByStringField(true, "Driver", CurrentDriver.getId(), new GetOneListener() {
                @Override
                public void onGetOne(Boolean success, BaseModel item) {

                    CurrentDriverStatus = (DriverStatus)item;

                    if(listener != null)
                        listener.onCompleted();

                }
            });

        }else{
            if(listener != null)
                listener.onCompleted();
        }

    }

    public void changeReadyStatus(final GetOneListener listener){

        DriverController.ChangeDriverReadyStatus(SCONNECTING.driverManager.CurrentDriver.getId(), new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                DriverStatus status =(DriverStatus)item;

                if(status != null){
                    CurrentDriverStatus = status;
                    SCONNECTING.orderScreen.mControlPanelView.invalidateUI(false,null);
                }

                SCONNECTING.driverManager.updatePositionHistory(new Completion() {
                    @Override
                    public void onCompleted() {

                        SCONNECTING.orderManager.invalidate(false,true,null);
                    }
                });

                if(listener != null)
                    listener.onGetOne(success,item);
            }
        });



    }
    public void ChangeDriverStatusToReadyIfFree(final GetOneListener listener){

        DriverController.ChangeDriverStatusToReadyIfFree(SCONNECTING.driverManager.CurrentDriver.getId(), new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                DriverStatus status =(DriverStatus)item;

                if(status != null){
                    CurrentDriverStatus = status;
                    SCONNECTING.orderScreen.mControlPanelView.invalidateUI(false,null);
                }

                SCONNECTING.driverManager.updatePositionHistory(new Completion() {
                    @Override
                    public void onCompleted() {

                        SCONNECTING.orderManager.invalidate(false,true,null);
                    }
                });

                if(listener != null)
                    listener.onGetOne(success,item);
            }
        });



    }

    public void updatePositionHistory( final Completion listener){

        String driverId = CurrentDriver.id;
        Location location = SCONNECTING.locationHelper.getLocation();

        if ( driverId != null && driverId.isEmpty() == false && location != null ) {

            DriverController.UpdateLocation(driverId, location, new GetOneListener() {
                @Override
                public void onGetOne(Boolean success, BaseModel item) {

                    DriverStatus status =(DriverStatus)item;

                    if(status != null){
                        CurrentDriverStatus = status;
                        SCONNECTING.orderManager.invalidateUI(false,null);
                    }

                    if(listener != null)
                        listener.onCompleted();

                }
            });

            if(SCONNECTING.orderManager.currentOrder != null && SCONNECTING.orderManager.currentOrder.IsMonitoring()){

                SCONNECTING.orderManager.actionHandler.nofifyLocationToUser(SCONNECTING.orderManager.currentOrder,location);
            }

        }else{

            if(listener != null)
                listener.onCompleted();

        }

    }

}
