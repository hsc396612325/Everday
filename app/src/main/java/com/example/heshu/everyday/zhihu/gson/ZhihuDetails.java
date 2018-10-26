package com.example.heshu.everyday.zhihu.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by heshu on 2017/12/12.
 */

public class ZhihuDetails {
    @SerializedName("body")
    public String body;

    @SerializedName("title")
    public String detailsTitle;

    @SerializedName("image")
    public String detailsImage;


    @SerializedName("css")
    public List<String> css;
}
