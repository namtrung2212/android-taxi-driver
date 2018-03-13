package com.sconnecting.driverapp.data.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class LocationObjectSerializer implements JsonSerializer<LocationObject> {

    @Override
    public JsonElement serialize(LocationObject src, Type typeOfSrc, JsonSerializationContext context) {

        JsonArray jArray = new JsonArray();

        if(src == null)
            return jArray;

        jArray.add(new JsonPrimitive(src.latitude));
        jArray.add(new JsonPrimitive(src.longitude));

        return jArray;
    }
}
