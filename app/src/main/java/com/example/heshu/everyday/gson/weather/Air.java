package com.example.heshu.everyday.gson.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2017/11/18.
 */

public class Air {
    @SerializedName("aqi")
    public String aqi;
    @SerializedName("pm25")
    public String pm25;
}
