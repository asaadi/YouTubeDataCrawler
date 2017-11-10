package com.github.khangnt.youtubecrawler.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class NaturalDeserializer {

    private NaturalDeserializer() {
    }

    public static Object deserialize(JsonElement json) {
        if (json.isJsonNull()) return null;
        else if (json.isJsonPrimitive()) return handlePrimitive(json.getAsJsonPrimitive());
        else if (json.isJsonArray()) return handleArray(json.getAsJsonArray());
        else return handleObject(json.getAsJsonObject());
    }

    private static Object handlePrimitive(JsonPrimitive json) {
        if (json.isBoolean())
            return json.getAsBoolean();
        else if (json.isString())
            return json.getAsString();
        else {
            BigDecimal bigDec = json.getAsBigDecimal();
            // Find out if it is an int type
            try {
                //noinspection ResultOfMethodCallIgnored
                bigDec.toBigIntegerExact();
                try {
                    return bigDec.intValueExact();
                } catch (ArithmeticException e) {
                }
                return bigDec.longValue();
            } catch (ArithmeticException e) {
            }
            // Just return it as a double
            return bigDec.doubleValue();
        }
    }

    private static Object handleArray(JsonArray json) {
        Object[] array = new Object[json.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = deserialize(json.get(i));
        return array;
    }

    private static Object handleObject(JsonObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet())
            map.put(entry.getKey(), deserialize(entry.getValue()));
        return map;
    }
}