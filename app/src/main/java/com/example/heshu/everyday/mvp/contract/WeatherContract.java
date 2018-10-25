package com.example.heshu.everyday.mvp.contract;

import com.example.heshu.everyday.gson.weather.Air;
import com.example.heshu.everyday.gson.weather.Weather;
import com.example.heshu.everyday.mvp.base.BasePresenter;
import com.example.heshu.everyday.mvp.base.BaseView;

/**
 * Created by heshu on 2018/10/25.
 */

public interface WeatherContract {

    interface View extends BaseView<Presenter> {
        void showWeather(Weather weather);

        void showAir(Air air);

        void showImage(String imageUri);

        void closeswipeRefresh();
    }

    interface Presenter extends BasePresenter{
        void requestWeather(String weatherId);

        void requestAir(String weatherId);

        void requestImage();

    }
}
