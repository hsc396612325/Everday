package com.example.heshu.everyday.gson.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by heshu on 2017/12/10.
 */

public class Before {
    @SerializedName("date")
    public String date;

    @SerializedName("stories")
    public List<Zitems> stories;
}
