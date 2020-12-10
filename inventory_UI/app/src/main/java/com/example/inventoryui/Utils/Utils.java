package com.example.inventoryui.Utils;

import android.util.Log;

import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.EnumAnnotation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Utils {

    final static String TAG = "Utils";
    final static SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");
    final static ObjectMapper mapper = new ObjectMapper();
   // protected SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");

    /***********************/
     //this.mapper.setDateFormat(df);

    public static Object getType(String from, Class to){

        mapper.setDateFormat(df);
        Object o = null;
        try {
            o = mapper.readValue(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;
    }

    public static ArrayList<Long> getListLong(String response) {

        mapper.setDateFormat(df);
        ArrayList<Long> list = null;
        try {
            list = mapper.readValue(response,new TypeReference<ArrayList<Long>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static JSONObject getJsonObject(Object object){

        mapper.setDateFormat(df);
        JSONObject json = null;
        try {
            json=new JSONObject(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public static String ListStringToUrlString(String listString){

        listString = (listString.substring(1, listString.length() - 1))
                .replaceAll("\\s", ""); //replace white spaces

        return listString ;
    }


    public static String getUrl(StringBuilder sb, Object obj, String prefix){
        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                Annotation[] annotations = f.getDeclaredAnnotations();

                Log.i(TAG,"f.name in get url = "+(f.getName()));
                Log.i(TAG,"annotations == null in get url = "+(annotations==null));

                boolean skip = false;
                for (Annotation annotation : annotations) {
                    if (annotation instanceof DropDownAnnotation || annotation instanceof EnumAnnotation) skip = true;
                }
                if(!skip) {
                    f.setAccessible(true);
                    if (f.get(obj) == null || f.getName().equals("Prefix")) {
                        continue;
                    }

                    /*sb.append(prefix);
                    sb.append(".");
                    sb.append(f.getName());
                    sb.append("=");*/
                    append(sb, prefix, f.getName());
                    if (f.getType().equals(List.class)) {
                        String listToString = ListStringToUrlString(f.get(obj).toString());
                        sb.append(listToString);
                       // sb.append(ListStringToUrlString(f.get(obj).toString()));

                    } else sb.append(f.get(obj));
                    sb.append("&");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(sb.length() > 0){
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static void append(StringBuilder sb, String prefix, String name) {
        sb.append(prefix);
        sb.append(".");
        sb.append(name);
        sb.append("=");
    }


    public static String getUrlFromMap(StringBuilder sb, Map<String, Object> parameters, String prefix) {

        Log.i(TAG,"urlLength = "+parameters.size());
        Log.i(TAG,"url = "+parameters);
        for(Map.Entry<String,Object> entry : parameters.entrySet()){
            /*sb.append(prefix);
            sb.append(".");
            sb.append(entry.getKey());
            sb.append("=");*/
            append(sb, prefix, entry.getKey());
            if (entry.getValue() instanceof List) {

                String listToString = ListStringToUrlString(entry.getValue().toString());
                sb.append(listToString);

            }else if(entry.getValue() instanceof Date){
                sb.append(df.format(entry.getValue()));
            }
            else sb.append(entry.getValue());
            sb.append("&");

        }
        Log.i(TAG,"url = "+sb.toString());
        return sb.toString();

    }
}
