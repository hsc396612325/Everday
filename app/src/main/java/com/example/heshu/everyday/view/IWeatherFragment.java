package com.example.heshu.everyday.view;

import com.example.heshu.everyday.gson.weather.Air;
import com.example.heshu.everyday.gson.weather.Weather;

/**
 * Created by heshu on 2018/5/9.
 */

public interface IWeatherFragment {
    void showWeather(Weather weather);
    void showAir(Air air);
    void showImage(String  imageUri);
    void closeswipeRefresh();
}
