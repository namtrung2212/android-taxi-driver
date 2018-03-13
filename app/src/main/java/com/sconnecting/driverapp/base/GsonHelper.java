package com.sconnecting.driverapp.base;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.entity.DateDeserializer;
import com.sconnecting.driverapp.data.entity.DateSerializer;
import com.sconnecting.driverapp.data.entity.LocationObjectDeserializer;
import com.sconnecting.driverapp.data.entity.LocationObjectList;
import com.sconnecting.driverapp.data.entity.LocationObjectListDeserializer;
import com.sconnecting.driverapp.data.entity.LocationObjectListSerializer;
import com.sconnecting.driverapp.data.entity.LocationObjectSerializer;
import com.sconnecting.driverapp.data.entity.LocationObject;

import io.realm.RealmObject;

/**
 * Created by TrungDao on 7/28/16.
 */
public class GsonHelper {

    public static  Gson getGson(){

        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                });

        builder.registerTypeAdapter(new TypeToken<LocationObject>() {}.getType(), new LocationObjectDeserializer());
        builder.registerTypeAdapter(new TypeToken<LocationObject>() {}.getType(), new LocationObjectSerializer());
        builder.registerTypeAdapter(new TypeToken<LocationObjectList>() {}.getType(), new LocationObjectListDeserializer());
        builder.registerTypeAdapter(new TypeToken<LocationObjectList>() {}.getType(), new LocationObjectListSerializer());
        builder.registerTypeAdapter(Date.class, new DateDeserializer());
        builder.registerTypeAdapter(Date.class, new DateSerializer());

        return builder.create();
    }
    public static <T  extends BaseModel> List<T> toList(final String json, Type arrayType)
    {
        try{

            return getGson().fromJson(json, arrayType);

        }catch (Exception e){

        }

        return new ArrayList<>();
    }


    public static <T  extends BaseModel> T toModel(final String json, Type modelType)
    {
        try{

            return getGson().fromJson(json, modelType);

        }catch (Exception e){

        }

        return null;
    }
}
