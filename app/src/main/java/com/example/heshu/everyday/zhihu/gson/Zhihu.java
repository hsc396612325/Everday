package com.example.heshu.everyday.zhihu.gson;



/**
 * Created by heshu on 2017/12/9.
 */

public class Zhihu {
    private ITEM_TYPE_Z Type;
    private String title;
    private String imagesUrl;
    private String date;
    private String id;
    //建立枚举 3个item 类型
    public enum ITEM_TYPE_Z {
        ITEM1,
        ITEM2,
        ITEM3
    }

    public ITEM_TYPE_Z getType() {
        return Type;
    }

    public void setType(ITEM_TYPE_Z type) {
        Type = type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
