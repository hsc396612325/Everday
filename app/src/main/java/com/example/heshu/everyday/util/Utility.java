package com.example.heshu.everyday.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.heshu.everyday.db.City;
import com.example.heshu.everyday.db.County;
import com.example.heshu.everyday.db.Province;
import com.example.heshu.everyday.gson.openeyes.Item;
import com.example.heshu.everyday.gson.weather.Air;
import com.example.heshu.everyday.gson.weather.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshu on 2017/11/14.
 */

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces = new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provincesObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provincesObject.getString("name"));
                    province.setProvinceCode(provincesObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理返回的市级数据
     */
    public static boolean handleCityResponse(String response ,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public  static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的Json数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Json解析成Air类
     */
    public static Air handleAirResponse(String response){
        Air air  = new Air();
        try{;
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String HeWeather = jsonArray.getJSONObject(0).toString();
            String aqi = new JSONObject(HeWeather).getString("aqi");
            String city = new JSONObject(aqi).getString("city");
            air.aqi = new JSONObject(city).getString("aqi");
            air.pm25 = new JSONObject(city).getString("pm25");
            return air;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析开眼返回的JSON数据
     */
    public static List<Item> handleItemListResponse(String response){
        try {
            List itemList = new ArrayList<>();;
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("itemList");
            Gson gson = new Gson();

            //循环遍历
            for (JsonElement user : jsonArray) {
                //通过反射 得到UserBean.class
                Item item = gson.fromJson(user,Item.class);
                itemList.add(item);
            }
            return itemList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
