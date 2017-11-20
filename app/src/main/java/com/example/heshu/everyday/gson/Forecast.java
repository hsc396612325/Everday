package com.example.heshu.everyday.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2017/11/18.
 */

public class Forecast {
    //预报时间
    @SerializedName("date")
    public String date;

    //最高温度
    @SerializedName("tmp_max")
    public String tmpMax;

    //最低温度
    @SerializedName("tmp_min")
    public String tmpMin;

    //白天天气
    @SerializedName("cond_txt_d")
    public String moreDaytime;

    //晚上天气
    @SerializedName("code_txt_n")
    public String moreNight;
}
