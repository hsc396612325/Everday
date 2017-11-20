package com.example.heshu.everyday.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2017/11/18.
 */

public class Basic {
    //城市名
    @SerializedName("location")
    public String cityName;

    //城市对应的天气id
    @SerializedName("cid")
    public String WeatherId;

}
