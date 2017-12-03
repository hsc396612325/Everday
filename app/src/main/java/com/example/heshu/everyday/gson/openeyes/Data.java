package com.example.heshu.everyday.gson.openeyes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2017/11/30.
 */

public class Data {
    //标题
    @SerializedName("title")
    public String title;

    //内容
    @SerializedName("description")
    public String description;

    @SerializedName("cover")
    public Cover cover;


    //视频URL
    @SerializedName("playUrl")
    public String playUrl;

    @SerializedName("webUrl")
    public WebUrl webUrl;

    //WebURL
    public class WebUrl{
        @SerializedName("raw")
        public String raw;
    }
    //背景图
    public class Cover{
        @SerializedName("feed")
        public String feed;
    }

}
