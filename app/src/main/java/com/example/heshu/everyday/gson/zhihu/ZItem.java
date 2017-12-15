package com.example.heshu.everyday.gson.zhihu;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2017/12/10.
 */

public class ZItem {
    @SerializedName("image")
    public String image;

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;
}
