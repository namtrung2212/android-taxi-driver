package com.sconnecting.driverapp.base;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.sconnecting.driverapp.data.entity.BaseModel;

/**
 * Created by TrungDao on 7/28/16.
 */
public class GenericHelper <T extends BaseModel> {

    public Class<T> clazz;

    public GenericHelper(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    public Class getGenericClass(){

        return this.clazz;
/*
        Type type = this.clazz.getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) type;

        Type modelType = pt.getActualTypeArguments()[0];

        return  (Class)modelType;
        */
    }

    public String getClassName(){

        return getGenericClass().getSimpleName();

    }
    public Type getArrayType(){

        //Class clazz = ((Class)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, clazz);


        return type;
    }
}
