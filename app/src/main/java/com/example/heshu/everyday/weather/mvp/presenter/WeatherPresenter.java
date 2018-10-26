package com.example.heshu.everyday.weather.mvp.presenter;

import com.example.heshu.everyday.weather.Gson.Air;
import com.example.heshu.everyday.weather.Gson.Weather;
import com.example.heshu.everyday.weather.mvp.contract.WeatherContract;
import com.example.heshu.everyday.weather.mvp.model.IWeatherModel;
import com.example.heshu.everyday.weather.mvp.model.WeatherModel;

/**
 * Created by heshu on 2018/5/9.
 */

public class WeatherPresenter implements WeatherContract.Presenter {
    WeatherContract.View mView;
    IWeatherModel mModel;

    public WeatherPresenter(WeatherContract.View view){
        this.mView = view;
        mModel = new WeatherModel(this);
    }


    public void showWeather(Weather weather){
        mView.showWeather(weather);
    }


    public void showAir(Air air){
        mView.showAir(air);
    }


    public void showImage(String imageUri){
        mView.showImage(imageUri);
    }

    public void closeswipeRefresh(){
        mView.closeswipeRefresh();
    }


    @Override
    public void requestWeather(String weatherId) {
        mModel.gainWeather(weatherId);
    }

    @Override
    public void requestAir(String weatherId) {
        mModel.gainAir(weatherId);
    }

    @Override
    public void requestImage() {
        mModel.gainImage();
    }
}
