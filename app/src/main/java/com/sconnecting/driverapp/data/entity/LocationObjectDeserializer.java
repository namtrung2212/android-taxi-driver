package com.sconnecting.driverapp.data.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by TrungDao on 8/9/16.
 */

public class LocationObjectDeserializer implements JsonDeserializer<LocationObject> {

    @Override
    public LocationObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonArray jsonArr = json.getAsJsonArray();

        if(jsonArr.size() != 2)
            return null;

        return new LocationObject(jsonArr.get(0).getAsDouble(),jsonArr.get(1).getAsDouble());

    }
}

