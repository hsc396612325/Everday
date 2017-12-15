package com.example.heshu.everyday.gson.openeyes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by heshu on 2017/12/7.
 */

public class ItemList {
    @SerializedName("itemList")
    public List<Item> itemList;

    @SerializedName("nextPageUrl")
    public String nextPageUrl;
}
