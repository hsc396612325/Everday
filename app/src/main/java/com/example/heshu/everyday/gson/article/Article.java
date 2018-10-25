package com.example.heshu.everyday.gson.article;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2017/12/4.
 */

public class Article {

    @SerializedName("date")
    public Date date;

    public class Date{
        @SerializedName("curr")
        public String curr;

        @SerializedName("prev")
        public String prev;

        @SerializedName("next")
        public String next;
    }

    @SerializedName("author")
    public String author;

    @SerializedName("title")
    public String title;

    @SerializedName("content")
    public String articleContent;

    @SerializedName("wc")
    public String wc;
}
