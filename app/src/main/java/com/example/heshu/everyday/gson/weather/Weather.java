package com.example.heshu.everyday.gson.weather;

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


    public class Basic {
        //城市名
        @SerializedName("location")
        public String cityName;

        //城市对应的天气id
        @SerializedName("cid")
        public String WeatherId;

    }

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }

    public class Now {

        //温度
        @SerializedName("tmp")
        public String temperature;

        //实况天气
        @SerializedName("cond_txt")
        public String more;

    }

    public class Lifestyle {
        //内容
        @SerializedName("txt")
        public String txt;

        //类型
        @SerializedName("type")
        public String type;

    }

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
}
