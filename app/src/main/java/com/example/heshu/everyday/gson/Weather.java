package com.example.heshu.everyday.gson;

import com.example.heshu.everyday.db.Province;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by heshu on 2017/11/18.
 */

public class Weather {

    //接口状态
    @SerializedName("status")
    public String status;
    //城市
    @SerializedName("basic")
    public Basic basic;
    //更新时间
    @SerializedName("update")
    public Update update;
    //实况天气
    @SerializedName("now")
    public Now now;
    //生活指数
    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
