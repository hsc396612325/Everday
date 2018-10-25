package com.example.heshu.everyday.mvp.model;

/**
 * Created by heshu on 2018/5/9.
 */

public interface IWeatherModel {
    //获取天气
    void gainWeather(String weatherId);

    //获取空气智质量
    void gainAir(String weatherId);

    //获取图片
    void gainImage();
}
