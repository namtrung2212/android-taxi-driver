package com.sconnecting.driverapp.data.entity;

import android.content.Context;

import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.base.listener.DeleteListener;
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.GetStringValueListener;
import com.sconnecting.driverapp.base.listener.PostListener;
import com.sconnecting.driverapp.data.storages.client.ClientCaching;
import com.sconnecting.driverapp.data.storages.client.ClientStorage;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;

import java.util.List;

import io.realm.RealmModel;


/**
 * Created by TrungDao on 7/26/16.
 */
public class BaseController<T extends BaseModel & RealmModel> {


    public static  String Token(){

        return  AppDelegate.getContext().getSharedPreferences("SCONNECTING", Context.MODE_PRIVATE).getString("Token", null);
    }


    public Class<T> clazz;

    public BaseController(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    public void getAll(Boolean isGetNow, final GetItemsListener listener){

        if( isGetNow == false && new ClientCaching(this.clazz).isExpired("") == false  ){ // not expired

            new ClientStorage<>(this.clazz).getAll(listener);

        }else {

            new ServerStorage(this.clazz).queryAll(new GetItemsListener() {
                @Override
                public void onGetItems(Boolean success, List list) {

                    if(success && list != null) {
                        new ClientStorage<>(clazz).save(list);
                        new ClientStorage<>(clazz).retrieve(list);

                        new ClientCaching<>(clazz).set("", null);
                    }
                    if(listener != null)
                        listener.onGetItems(success,list);
                }
            });
        }

    }

    public void get(final String filter, final GetItemsListener listener){

        new ServerStorage(this.clazz).query(filter, new GetItemsListener() {
            @Override
            public void onGetItems(Boolean success,List list) {

                if(success && list != null) {
                    new ClientStorage<>(clazz).save(list);
                    new ClientStorage<>(clazz).retrieve(list);
                }
                if(listener != null)
                    listener.onGetItems(success,list);
            }
        });

    }

    public void get(final String type,final String filter, final GetItemsListener listener){

        new ServerStorage(this.clazz).query(type,filter, new GetItemsListener() {
            @Override
            public void onGetItems(Boolean success,List list) {

                if(success && list !=null) {
                    new ClientStorage<>(clazz).save(list);
                    new ClientStorage<>(clazz).retrieve(list);
                }
                if(listener != null)
                    listener.onGetItems(success,list);
            }
        });

    }


    public void getString(String type,String filter, final GetStringValueListener listener){

        new ServerStorage(this.clazz).queryString(type,filter, listener);

    }


    public void getByField(Boolean isGetNow,final String field,final String value, final GetItemsListener listener){


        String filter = field + "=" + value;
        final String url = ServerStorage.ServerURL + "/" + this.clazz.getSimpleName() + "/selectall?" + filter;

        if(isGetNow == false &&  new ClientCaching(this.clazz).isExpired(url) == false ){ // not expired

            new ClientStorage<>(this.clazz).getByStringField(field,value,listener);

        }else {

            new ServerStorage(this.clazz).queryByURL(url , new GetItemsListener() {
                @Override
                public void onGetItems(Boolean success,List list) {

                    if(success && list !=null) {
                        new ClientStorage<>(clazz).save(list);
                        new ClientStorage<>(clazz).retrieve(list);

                        new ClientCaching<>(clazz).set(url, null);
                    }

                    if(listener != null)
                        listener.onGetItems(success,list);
                }
            });
        }


    }

    public void getById(Boolean isGetNow,final String id, final GetOneListener listener){

        if(isGetNow == false) {

            new ClientStorage<>(this.clazz).getById(id, new GetOneListener() {
                @Override
                public void onGetOne(Boolean success,BaseModel item) {

                    if (!success || item == null || new ClientCaching<T>(clazz).isExpired((T) item)) {

                        new ServerStorage(clazz).queryById(id, new GetOneListener() {
                            @Override
                            public void onGetOne(Boolean success,BaseModel item) {

                                if(success && item !=null) {
                                    new ClientStorage<>(clazz).save((T) item);
                                    new ClientStorage<>(clazz).retrieve((T) item);
                                }
                                if(listener != null)
                                    listener.onGetOne(success,item);
                            }
                        });

                    } else {

                        if (listener != null)
                            listener.onGetOne(success,item);
                    }

                }
            });

        }else{

            new ServerStorage(clazz).queryById(id, new GetOneListener() {
                @Override
                public void onGetOne(Boolean success,BaseModel item) {

                    if(success && item !=null) {
                        new ClientStorage<>(clazz).save((T) item);
                        new ClientStorage<>(clazz).retrieve((T) item);
                    }
                    if(listener != null)
                        listener.onGetOne(success,item);
                }
            });
        }

    }

    public void getOne(String filter, final GetOneListener listener){

        new ServerStorage(this.clazz).queryOne(filter, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success,BaseModel item) {

                if(success && item !=null) {
                    new ClientStorage<>(clazz).save((T) item);
                    new ClientStorage<>(clazz).retrieve((T) item);
                }

                if(listener != null)
                    listener.onGetOne(success,item);
            }
        });

    }

    public void getOne(String type,String filter, final GetOneListener listener){

        new ServerStorage(this.clazz).queryOne(type,filter, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success,BaseModel item) {

                if(success && item !=null) {
                    new ClientStorage<>(clazz).save((T) item);
                    new ClientStorage<>(clazz).retrieve((T) item);
                }

                if(listener != null)
                    listener.onGetOne(success,item);
            }
        });

    }

    public void getDouble(String type,String filter, final GetDoubleValueListener listener){

        new ServerStorage(this.clazz).queryDouble(type,filter, listener);

    }

    public void getOneByStringField(Boolean isGetNow, final String field,final String value, final GetOneListener listener){

        if(isGetNow == false) {

            new ClientStorage<>(this.clazz).getOneByStringField(field, value, new GetOneListener() {
                @Override
                public void onGetOne(Boolean success,BaseModel item) {

                    if (!success || item == null || new ClientCaching<T>(clazz).isExpired((T) item)) {

                        new ServerStorage(clazz).queryOneByField(field, value, new GetOneListener() {
                            @Override
                            public void onGetOne(Boolean success,BaseModel item) {

                                if(success && item !=null) {
                                    new ClientStorage<>(clazz).save((T) item);
                                    new ClientStorage<>(clazz).retrieve((T) item);
                                }
                                if(listener != null)
                                    listener.onGetOne(success,item);
                            }
                        });

                    } else {

                        if (listener != null)
                            listener.onGetOne(success,item);
                    }

                }
            });

        }else{

            new ServerStorage(clazz).queryOneByField(field, value, new GetOneListener() {
                @Override
                public void onGetOne(Boolean success,BaseModel item) {

                    if(success && item !=null) {
                        new ClientStorage<>(clazz).save((T) item);
                        new ClientStorage<>(clazz).retrieve((T) item);
                    }
                    if(listener != null)
                        listener.onGetOne(success,item);
                }
            });
        }

    }

    public void create(T obj, final PostListener listener) {
        create(obj,null,listener);
    }

    public void create(final T obj, String routeName, final PostListener listener){

        new ServerStorage(this.clazz).create(obj,routeName, new PostListener() {
            @Override
            public void onCompleted(Boolean success,BaseModel item) {

                if(success && item !=null) {

                    new ClientStorage<>(clazz).save((T)item);
                    new ClientStorage<>(clazz).retrieve((T)item);

                    if(obj.getId() != null)
                        new ClientStorage<>(clazz).deleteById(obj.getId());

                }

                if(listener != null)
                    listener.onCompleted(success,item);


            }
        });

    }

    public void update(T obj, final PostListener listener) {
        update(obj,null,listener);
    }


    public void update(final T obj, String routeName, final PostListener listener){

        new ServerStorage(this.clazz).update(obj,routeName, new PostListener() {
            @Override
            public void onCompleted(Boolean success,BaseModel item) {

                if(success && item !=null) {

                    new ClientStorage<>(clazz).save((T)item);
                    new ClientStorage<>(clazz).retrieve((T)item);

                    if(obj.getId() != null)
                        new ClientStorage<>(clazz).deleteById(obj.getId());

                }

                if(listener != null)
                    listener.onCompleted(success,item);


            }
        });

    }

    public void delete(final T obj, final DeleteListener listener){

        new ServerStorage(this.clazz).delete(obj, new DeleteListener() {
            @Override
            public void onDeleted(Boolean success,Integer number) {

                if(success && number > 0)
                    new ClientStorage<>(clazz).delete(obj);

                if(listener != null)
                    listener.onDeleted(success ,number);

            }
        });

    }

    public void delete(String filter, final DeleteListener listener){

        new ServerStorage(this.clazz).delete(filter, listener);

    }

}


