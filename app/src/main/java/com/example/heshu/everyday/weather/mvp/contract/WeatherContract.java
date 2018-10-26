package com.example.heshu.everyday.weather.mvp.contract;

import com.example.heshu.everyday.weather.Gson.Air;
import com.example.heshu.everyday.weather.Gson.Weather;
import com.example.heshu.everyday.common.base.BasePresenter;
import com.example.heshu.everyday.common.base.BaseView;

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
