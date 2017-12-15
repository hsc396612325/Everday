package com.example.heshu.everyday.gson.zhihu;

import com.google.gson.annotations.SerializedName;

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

}
