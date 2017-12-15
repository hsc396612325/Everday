package com.example.heshu.everyday.gson.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by heshu on 2017/12/10.
 */

public class News {
    @SerializedName("date")
    public String date;

    @SerializedName("stories")
    public List<Zitems> stories;

    @SerializedName("top_stories")
    public List<ZItem> topStories;
}
