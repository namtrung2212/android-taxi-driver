package com.sconnecting.driverapp.data.storages.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TrungDao on 8/9/16.
 */

public class ClientCachingConfig {


    public static ClientCachingConfig intance;

    public Map<String,Double> cachingMinutes;
    public Map<String,Double> cleanUpDays;

    public static void Init(){
        if( intance == null){
            intance = new ClientCachingConfig();
        }
    }

    public ClientCachingConfig()
    {

        cachingMinutes = new HashMap<>();
        cleanUpDays = new HashMap<>();
    }

    public static void register(String modelType , int cachingminutes,int cleanupdays) {
        register(modelType,(double)cachingminutes,(double)cleanupdays);
    }
    public static void register(String modelType , Double cachingminutes,Double cleanupdays){

        Init();

        if( cachingminutes != null)
            intance.cachingMinutes.put(modelType,cachingminutes );

        if( cleanupdays != null)
            intance.cleanUpDays.put(modelType,cleanupdays );

    }

    public static Double getCachingMinutes(String modelType){

        ClientCachingConfig.Init();
        Double minute = ClientCachingConfig.intance.cachingMinutes.get(modelType);

        if(minute != null){
            return minute;
        }else{
            return (Double)0.0;
        }
    }
    public static Double getCleanUpDays(String modelType){
        ClientCachingConfig.Init();
        Double days = ClientCachingConfig.intance.cleanUpDays.get(modelType);

        if(days != null){
            return days;
        }else{
            return (Double)0.0;
        }
    }




}
