package com.example.heshu.everyday.weather.mvp.model;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.heshu.everyday.weather.Gson.Air;
import com.example.heshu.everyday.weather.Gson.Weather;
import com.example.heshu.everyday.weather.mvp.presenter.WeatherPresenter;
import com.example.heshu.everyday.common.util.App;
import com.example.heshu.everyday.common.util.HttpUtil;
import com.example.heshu.everyday.common.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by heshu on 2018/5/9.
 */

public class WeatherModel implements IWeatherModel {

    private WeatherPresenter mPresenter;
    private static final String TAG = "WeatherModel";

    public WeatherModel(WeatherPresenter weatherPresenter) {
        mPresenter = weatherPresenter;
    }




    @Override
    public void gainWeather(String weatherId) {
        String key = "baccef9080f24fb895c350b8cdd72c6a";
        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" +
                weatherId + "&key=baccef9080f24fb895c350b8cdd72c6a";

        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override//失败
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mPresenter.closeswipeRefresh();
        }

            @Override//成功
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的数据
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
                editor.putString("weather",responseText);
                editor.apply();

                mPresenter.showWeather(weather);
                mPresenter.closeswipeRefresh();
            }
        });
    }

    @Override
    public void gainAir(String weatherId) {

        String airUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId+"&key=baccef9080f24fb895c350b8cdd72c6a";

        HttpUtil.sendOKHttpRequest(airUrl, new Callback() {
            @Override//失败
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mPresenter.closeswipeRefresh();
            }

            @Override//成功
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的数据
                final String responseText = response.body().string();
                final Air air = Utility.handleAirResponse(responseText);
                Log.d(TAG, "onResponse: "+responseText);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
                editor.putString("air",responseText);
                editor.apply();

                mPresenter.showAir(air);
                mPresenter.closeswipeRefresh();
            }
        });
    }

    @Override
    public void gainImage() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                mPresenter.showImage(bingPic);
                mPresenter.closeswipeRefresh();
            }
        });
    }
}
