package com.vjtech.coin168.utils;

import java.io.StringReader;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class GsonUtil {

    public static final Gson createGson(final boolean serializeNulls) {
        final GsonBuilder builder = new GsonBuilder();
        if (serializeNulls) {
            builder.serializeNulls();
        }
        return builder.create();
    }
    
    /**
     * <pre>
     * test data:
     * "[0,1]" 
     * "[\"{}\",\"{}\"]"
     * "[\"0\",\"1\"]"
     * </pre>
     * 
     * @param jsonStringArray
     * @return List of String
     */
    public static List<String> jsonToStringList(String jsonStringArray) {
        return jsonToObj(jsonStringArray);
    }

    /**
     * <pre>
     * test data:
     * [{a:1, b: 2},{a:10, b:20}]
     * </pre>
     * 
     * @param jsonObjectArray
     * @return List of Object
     */
    public static List<Object> jsonToObjectList(Object jsonObjectArray) {
        return objToObj(jsonObjectArray);
    }

    /**
     * <pre>
     * test data:
     * "{a: \"1\", b: \"2\"}"
     * "{a: 1, b: 2}"
     * </pre>
     * 
     * @param jsonString
     * @return Map
     */
    public static Map<String, Object> jsonToMap(String jsonString) {
        Gson gson = createGson(true);
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        reader.setLenient(true);
        return gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static String mapToJson(Map<String, Object> map) {
        Gson gson = createGson(true);
        return gson.toJson(map, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * <pre>
     * test data:
     * "{}"
     * "{a: \"1\", b: \"2\"}"
     * "{a: 1, b: 2}"
     * "[]"
     * "[0,1]"
     * "[\"{}\",\"{}\"]"
     * "[{},{}]"
     * </pre>
     * 
     * @param jsonString
     * @return <T>
     */
    public static <T> T jsonToObj(String jsonString) {
        Gson gson = createGson(true);
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        reader.setLenient(true);
        return gson.fromJson(reader, new TypeToken<T>() {
        }.getType());
    }

    public static <T> T jsonToObj(String jsonString, Class<T> c) {
        Gson gson = createGson(true);
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        reader.setLenient(true);
        return gson.fromJson(reader, c);
    }
    public static String objToJson(Object obj) {
        Gson gson = createGson(true);
        return gson.toJson(obj);
    }
    
    public static Map<String, Object> objToMap(Object obj) {
        if (obj instanceof String) {
            return jsonToMap((String) obj);
        }
        return jsonToMap(objToJson(obj));
    }

    public static <T> T objToObj(Object obj) {
        if (obj instanceof String) {
            return jsonToObj((String) obj);
        }
        return jsonToObj(objToJson(obj));
    }
}
