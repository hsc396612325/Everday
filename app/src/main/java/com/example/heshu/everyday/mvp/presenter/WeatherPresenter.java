package com.example.heshu.everyday.mvp.presenter;

import com.example.heshu.everyday.gson.weather.Air;
import com.example.heshu.everyday.gson.weather.Weather;
import com.example.heshu.everyday.mvp.model.IWeatherModel;
import com.example.heshu.everyday.mvp.model.WeatherModel;
import com.example.heshu.everyday.view.IWeatherFragment;

/**
 * Created by heshu on 2018/5/9.
 */

public class WeatherPresenter {
    IWeatherFragment mView;
    IWeatherModel mModel;

    public WeatherPresenter(IWeatherFragment view){
        this.mView = view;
        mModel = new WeatherModel(this);
    }

    //天气数据
    public void Weather(String weatherId){
        mModel.gainWeather(weatherId);
    }
    public void showWeather(Weather weather){
        mView.showWeather(weather);
    }

    //air
    public void gainAir(String weatherId){
        mModel.gainAir(weatherId);
    }
    public void showAir(Air air){
        mView.showAir(air);
    }

    //image
    public void gainImage(){
        mModel.gainImage();
    }
    public void showImage(String imageUri){
        mView.showImage(imageUri);
    }

    public void closeswipeRefresh(){
        mView.closeswipeRefresh();
    }

}
