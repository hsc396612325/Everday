package com.example.heshu.everyday.gson.zhihu;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heshu on 2017/12/10.
 */

public class Zitems {

        @SerializedName("images")
        public String[] images;

        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

}
