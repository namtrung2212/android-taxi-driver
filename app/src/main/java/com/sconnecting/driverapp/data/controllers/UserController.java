package com.sconnecting.driverapp.data.controllers;

import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.GetStringValueListener;
import com.sconnecting.driverapp.base.listener.PostListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.models.*;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by TrungDao on 7/26/16.
 */


public class UserController extends BaseController<User> {


    public UserController()
    {
        super(User.class);
    }

    public void updateUserName(final String userName ,String userId,final Completion listener){

        new BaseController<>(User.class).getById(true,userId, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success,BaseModel item) {

                User user = (User) item;
                user.Name = userName;

                new BaseController<>(User.class).update(user, new PostListener() {
                    @Override
                    public void onCompleted(Boolean success,BaseModel item) {

                        if(listener != null)
                            listener.onCompleted();
                    }
                });
            }
        });

    }

    public static void ActivateUserAccount(String userId , final Completion listener){

        new UserController().getString("ActivateUserAccount/" + userId, null, new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success,String value) {

                if(listener != null)
                    listener.onCompleted();
            }

        });

    }



    public static void RequestForActivationCode(String phoneNo  , String countryCode, final GetStringValueListener listener ){

        new BaseController<>(UserSetting.class).getString("Authenticate/SendVerifyCode", "country=" + countryCode + "&PhoneNo=" + phoneNo, new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success,String value) {

                try {
                    JSONObject reader = new JSONObject(value);
                    String request_id = reader.getString("request_id");
                    if(listener != null)
                        listener.onCompleted(true,request_id);


                } catch (JSONException e) {
                    e.printStackTrace();

                    if(listener != null)
                        listener.onCompleted(false,null);
                }
            }
        });
    }



    public static void CheckForActivationCode(String request_id, String code, String phoneNo, final GetStringValueListener listener ){

        new BaseController<>(UserSetting.class).getString("Authenticate/CheckVerifyCode", "request_id=" + request_id + "&code=" + code + "&PhoneNo=" + phoneNo, new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success,String value) {

                String userId = null;

                try {
                    JSONObject reader = new JSONObject(value);
                    userId = reader.getString("message");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(listener != null)
                    listener.onCompleted(true,userId);

            }
        });


    }


}





