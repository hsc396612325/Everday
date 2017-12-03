package com.example.heshu.everyday.gson.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2017/11/18.
 */

public class Now {

    //温度
    @SerializedName("tmp")
    public String temperature;

    //实况天气
    @SerializedName("cond_txt")
    public String more;

}
