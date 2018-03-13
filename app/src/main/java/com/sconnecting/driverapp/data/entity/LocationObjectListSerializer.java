package com.sconnecting.driverapp.data.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sconnecting.driverapp.data.storages.client.RealmDouble;

import java.lang.reflect.Type;

public class LocationObjectListSerializer implements JsonSerializer<LocationObjectList> {

    @Override
    public JsonElement serialize(LocationObjectList src, Type typeOfSrc, JsonSerializationContext context) {

        JsonArray jArray = new JsonArray();

        if(src == null)
            return jArray;

        for (RealmDouble obj : src.getList()) {

            jArray.add(new JsonPrimitive(obj.getDoubleValue()));

        }

        return jArray;

    }
}
