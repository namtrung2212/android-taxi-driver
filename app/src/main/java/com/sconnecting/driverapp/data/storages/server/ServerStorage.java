package com.sconnecting.driverapp.data.storages.server;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sconnecting.driverapp.AppDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.sconnecting.driverapp.base.GsonHelper;
import com.sconnecting.driverapp.base.listener.DeleteListener;
import com.sconnecting.driverapp.base.listener.GetBoolValueListener;
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.GetStringValueListener;
import com.sconnecting.driverapp.base.listener.PostListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.entity.LocationObject;
import com.sconnecting.driverapp.data.entity.LocationObjectList;
import com.sconnecting.driverapp.base.GenericHelper;
import com.sconnecting.driverapp.data.storages.client.RealmDouble;
import com.sconnecting.driverapp.base.CryptoHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


/**
 * Created by TrungDao on 7/26/16.
 */


public class ServerStorage<T extends BaseModel> {

    public static  String Token(){

        return  AppDelegate.getContext().getSharedPreferences("SCONNECTING", Context.MODE_PRIVATE).getString("Token", null);
    }


    public static String ServerURL = "";
    public Class<T> clazz;

    public ServerStorage(Class<T> clazz)
    {
        this.clazz = clazz;
    }



    public static void TestConnection(final GetBoolValueListener listener){


        String url = ServerURL+ "/TestConnection" ;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                if(listener != null)
                    listener.onCompleted(true,true);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {


                new SweetAlertDialog(AppDelegate.CurrentActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(" ")
                        .setContentText(throwable.getMessage())
                        .setConfirmText("Đồng ý")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog dialog) {

                                dialog.dismissWithAnimation();
                            }
                        })
                        .show();
                if(listener != null)
                    listener.onCompleted(false,false);
            }


        });

    }


    @NonNull
    protected StringEntity generateJSONHeader(T obj) {
        JSONObject params = new JSONObject();
        Field[] fields = this.clazz.getDeclaredFields();

        for (Field field : fields) {

            String fieldName = field.getName();

            if(fieldName.equals(obj.getId()) || fieldName.equals(obj.getCreatedAt()) || fieldName.equals(obj.getUpdatedAt())
                    || fieldName.equals(obj.getRetrieveAt()) || fieldName.equals(obj.getUsedAt())){
                continue;
            }
            try {

                Object value = field.get(obj);

                if(value == null){

                    params.put(field.getName(), JSONObject.NULL);

                }else {

                    if (value instanceof List) {

                        JSONArray lstItems = new JSONArray();

                        for (Object item : (List) value) {
                            lstItems.put(item);
                        }

                        params.put(field.getName(), lstItems);


                    } else if (value instanceof Date) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        params.put(field.getName(), sdf.format(value).toString());

                    } else if (value instanceof LocationObject) {

                        LatLng loc = ((LocationObject)value).getLatLng();

                        if(loc != null) {
                            JSONArray lstItems = new JSONArray();
                            lstItems.put(loc.latitude);
                            lstItems.put(loc.longitude);

                            params.put(field.getName(), lstItems);
                        }else {

                            params.put(field.getName(), JSONObject.NULL);
                        }

                    } else if (value instanceof LocationObjectList) {

                        List<RealmDouble> list = ((LocationObjectList)value).getList();

                        if(list != null) {
                            JSONArray lstItems = new JSONArray();
                            for (int i = 0; i < list.size(); i++) {

                                lstItems.put(list.get(i).getDoubleValue());
                            }

                            params.put(field.getName(), lstItems);
                        }else {

                            params.put(field.getName(), JSONObject.NULL);
                        }
                    } else {

                        params.put(field.getName(), value);
                    }
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        StringEntity entity = new StringEntity(params.toString(), ContentType.APPLICATION_JSON);

        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        return entity;
    }


    public void queryAll(final GetItemsListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String url = ServerURL + "/" + generic.getClassName() + "/selectall?page=1&pagesize=2";

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                List<T> list = GsonHelper.toList(response, generic.getArrayType());

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



    public void  query(String filter,final GetItemsListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String url = ServerURL + "/" + generic.getClassName();

        if (filter != null && filter.isEmpty() == false ){
            url = url + "/selectall?" + filter;
        }else{
            url = url + "/selectall";
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                List<T> list = GsonHelper.toList(response, generic.getArrayType());

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


    public void  query(String type,String filter,final GetItemsListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String url = ServerURL + "/" + generic.getClassName();

        if (filter != null && filter.isEmpty() == false ){
            url = url + "/" + type +"?" + filter;
        }else{
            url = url + "/"+ type ;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                List<T> list = GsonHelper.toList(response, generic.getArrayType());

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


    public void  queryString(String type,String filter,final GetStringValueListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String url = ServerURL+ "/"  + generic.getClassName();

        if (filter != null && filter.isEmpty() == false ){
            url = url + "/" + type +"?" + filter;
        }else{
            url = url + "/"+ type ;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                if(listener != null)
                    listener.onCompleted(true,response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                if(listener != null)
                    listener.onCompleted(false,null);
            }


        });

    }






    public void queryByURL(String url,final GetItemsListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                List<T> list = GsonHelper.toList(response, generic.getArrayType());
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

    public void  queryById(String id,final GetOneListener listener){


        if (id == null || id.isEmpty()) {
            listener.onGetOne(false,null);
            return;
        }

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String url = ServerURL + "/" + generic.getClassName() + "/ID/" + id;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                T item = GsonHelper.toModel(response, generic.getGenericClass());

                if(listener != null)
                    listener.onGetOne(true,item);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                if(listener != null)
                    listener.onGetOne(false,null);
            }

        });

    }

    public void  queryOne(String filter,final GetOneListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String url = ServerURL + "/" + generic.getClassName();

        if (filter != null && filter.isEmpty() == false ){

            url = url + "/selectall?" + filter + "&page=1&pagesize=1";
        }else{
            url = url + "/selectall?page=1&pagesize=1";
        }


        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                List<T> list = GsonHelper.toList(response, generic.getArrayType());

                if(listener != null) {
                    if (list.isEmpty() == false)
                        listener.onGetOne(true,(T) list.toArray()[0]);
                    else
                        listener.onGetOne(true,null);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                if(listener != null)
                    listener.onGetOne(false,null);
            }

        });

    }

    public void  queryOne(String type,String filter,final GetOneListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String url = ServerURL + "/" + generic.getClassName();

        if (filter != null && filter.isEmpty() == false ){

            url = url + "/" + type +"?" + filter + "&page=1&pagesize=1";
        }else{
            url = url + "/"+ type +"?page=1&pagesize=1";
        }


        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                T item = GsonHelper.toModel(response, generic.getGenericClass());

                if(listener !=null)
                    listener.onGetOne(true,item);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                if(listener != null)
                    listener.onGetOne(false,null);
            }

        });

    }


    public void  queryDouble(String type,String filter,final GetDoubleValueListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String url = ServerURL + "/" + generic.getClassName();

        if (filter != null && filter.isEmpty() == false ){

            url = url + "/" + type +"?" + filter;
        }else{
            url = url + "/"+ type ;
        }


        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                Double value = null;

                try {

                    value = Double.parseDouble(response);

                }catch (NumberFormatException ex){

                }

                if(listener != null)
                    listener.onCompleted(true,value);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                if(listener != null)
                    listener.onCompleted(false,null);
            }

        });

    }


    public void  queryOneByField(String field, String value,final GetOneListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        String filter = field + "=" + value;

        String url = ServerURL + "/" + generic.getClassName();
        url = url + "/selectall?" + filter + "&page=1&pagesize=1";


        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                List<T> list = GsonHelper.toList(response, generic.getArrayType());

                if(listener != null) {
                    if (list.isEmpty() == false)
                        listener.onGetOne(true,(T) list.toArray()[0]);
                    else
                        listener.onGetOne(true,null);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                if(listener != null)
                    listener.onGetOne(false,null);
            }

        });

    }

    public void create(T obj, final PostListener listener) {

        create(obj,null, listener);

    }
    public void create(T obj, String routeName, final PostListener listener){

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        StringEntity entity = generateJSONHeader(obj);
        entity.setContentType("text/plain");

        String url =  ServerURL + "/" + generic.getClassName() + "/" + (routeName != null ? routeName : "create");

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.post(AppDelegate.getContext(),url, entity, "text/plain",
                new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {

                        T item = GsonHelper.toModel(response, generic.getGenericClass());

                        if(listener != null)
                            listener.onCompleted(true,item);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                        if(listener != null)
                            listener.onCompleted(false,null);
                    }
                });



    }


    public void update(T obj, final PostListener listener) {

        update(obj,null, listener);

    }
    public void update(T obj, String routeName, final PostListener listener){

        if(obj.getId() == null || obj.getId().isEmpty()){

            listener.onCompleted(false,null);
            return;
        }

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        StringEntity entity = generateJSONHeader(obj);
        entity.setContentType("text/plain");

        String url = ServerURL + "/" + generic.getClassName() + "/" +  (routeName != null ? routeName : "ID") + "/" + obj.getId();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.patch(AppDelegate.getContext(), url, entity, "text/plain",
                new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {

                        T item = GsonHelper.toModel(response, generic.getGenericClass());

                        if(listener != null)
                            listener.onCompleted(true,item);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                        if(listener != null)
                            listener.onCompleted(false,null);
                    }
                });


    }


    public void delete(T obj, final DeleteListener listener){

        if(obj.getId() == null || obj.getId().isEmpty()){

            listener.onDeleted(false,0);
            return;
        }

        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        StringEntity entity = null;
        try {
            JSONObject params = new JSONObject();
            params.put("Token", Token());
            entity = new StringEntity(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        entity.setContentType("text/plain");

        String url = ServerURL + "/"  + generic.getClassName() + "/ID/" + obj.getId();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.delete(AppDelegate.getContext(), url,entity,"text/plain",
                new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {

                        if(listener != null)
                            listener.onDeleted(true,1);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                        if(listener != null)
                            listener.onDeleted(false,0);
                    }
                });

    }


    public void delete(String filter, final DeleteListener listener){


        final GenericHelper<T> generic =  new GenericHelper<T>(this.clazz);

        StringEntity entity = null;
        try {
            JSONObject params = new JSONObject();
            params.put("Token", Token());
            entity = new StringEntity(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        entity.setContentType("text/plain");

        String url = ServerURL + "/" + generic.getClassName() + "/delete?" + filter;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.delete(AppDelegate.getContext(),url ,entity,"text/plain",
                new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {

                        if(listener != null)
                            listener.onDeleted(true,Integer.getInteger(response.toString()));

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                        if(listener != null)
                            listener.onDeleted(false,0);
                    }
                });

    }
}

