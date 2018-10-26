package com.example.heshu.everyday.weather.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by heshu on 2018/5/10.
 */

public class BasicList {
    @SerializedName("basic")
    public List<Basic> asicList;

    public class Basic {
        //城市名
        @SerializedName("location")
        public String cityName;

        //城市对应的天气id
        @SerializedName("cid")
        public String WeatherId;

    }
}
