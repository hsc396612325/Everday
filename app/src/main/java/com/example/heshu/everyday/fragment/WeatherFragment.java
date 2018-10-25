package com.example.heshu.everyday.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.activity.MainActivity;
import com.example.heshu.everyday.gson.weather.Air;
import com.example.heshu.everyday.gson.weather.Weather;
import com.example.heshu.everyday.mvp.contract.WeatherContract;
import com.example.heshu.everyday.mvp.presenter.WeatherPresenter;
import com.example.heshu.everyday.util.App;
import com.example.heshu.everyday.util.Utility;
import com.example.heshu.everyday.view.IWeatherFragment;

import java.util.Date;

/**
 * Created by heshu on 2018/5/9.
 */

public class WeatherFragment extends Fragment implements WeatherContract.View{
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfText;
    private TextView drsgText;
    private TextView sportText;
    private ImageView bingPicImg;
    private SwipeRefreshLayout swipeRefresh;
    private EditText mEditText;


    private WeatherContract.Presenter mPresenter;
    private String mWeatherId;


    private static final String TAG = "WeatherFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        initView(view);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mWeatherId = prefs.getString("WeatherId", null);
        if(mWeatherId==null){
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.replaceFragment(new ChooseAreaFragment());
        }else {
            upload();
        }
        return view;
    }

    private void initView(View view) {
        weatherLayout = (ScrollView) view.findViewById(R.id.weather_layout);
        titleCity = (TextView) view.findViewById(R.id.weather_title_city);
        titleUpdateTime = (TextView) view.findViewById(R.id.title_update_time);
        degreeText = (TextView) view.findViewById(R.id.weather_degree_text);
        weatherInfoText = (TextView) view.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.weather_forecast_layout);
        aqiText = (TextView) view.findViewById(R.id.aqi_text);
        pm25Text = (TextView) view.findViewById(R.id.pm25_text);
        comfText = (TextView) view.findViewById(R.id.comf_text);
        drsgText = (TextView) view.findViewById(R.id.drsg_text);
        sportText = (TextView) view.findViewById(R.id.sport_text);
        bingPicImg = (ImageView) view.findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestWeather(mWeatherId);
                mPresenter.requestAir(mWeatherId);
                mPresenter.requestImage();
            }
        });

        titleCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.replaceFragment(new ChooseAreaFragment());
            }
        });
    }

    private void upload() {
        //加载天气信息
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Date date = new Date();
        long date1 =  prefs.getLong("date",0);
        if(date.getTime() - date1 > 8*60*60*1000){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
            editor.putLong("date",date.getTime());
            editor.apply();
            swipeRefresh.setRefreshing(true);
            mPresenter.requestWeather(mWeatherId);
            mPresenter.requestAir(mWeatherId);
            mPresenter.requestImage();
        }else {

            String weatherString = prefs.getString("weather", null);
            Log.d(TAG, "upload: weather"+weatherString);
            if (weatherString != null) {
                Log.d(TAG, "weatherString upload: if");
                //有缓存时直接查询天气数据
                Weather weather = Utility.handleWeatherResponse(weatherString);
                if(weather !=null){
                    showWeatherInfo(weather);
                }else {
                    //无缓存去服务器上查询
                    weatherLayout.setVisibility(View.INVISIBLE);
                    mPresenter.requestWeather(mWeatherId);
                }
            } else {
                //无缓存去服务器上查询
                weatherLayout.setVisibility(View.INVISIBLE);
                mPresenter.requestWeather(mWeatherId);
            }

            //加载空气质量信息
            String airString = prefs.getString("air", null);
            Log.d(TAG, "upload:  air " +airString);
            if (airString != null) {
                Log.d(TAG, "upload:  air " +airString);
                Air air = Utility.handleAirResponse(airString);
                if(air!=null) {
                    showAirInfo(air);
                }else {
                    //无缓存去服务器上查询
                    weatherLayout.setVisibility(View.INVISIBLE);
                    mPresenter.requestAir(mWeatherId);
                }
            } else {
                //无缓存去服务器上查询
                weatherLayout.setVisibility(View.INVISIBLE);
                mPresenter.requestAir(mWeatherId);
            }

            //加载图片
            String bingPic = prefs.getString("bing_pic", null);
            if (bingPic != null) {
                Glide.with(this).load(bingPic).into(bingPicImg);
            } else {
                mPresenter.requestImage();
            }
        }

    }


    @Override
    public void showWeather(final Weather weather) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showWeatherInfo(weather);
            }
        });
    }

    @Override
    public void showAir(final Air air) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAirInfo(air);
            }
        });
    }

    @Override
    public void showImage(final String imageUri) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getActivity()).load(imageUri).into(bingPicImg);
            }
        });
    }

    @Override
    public void closeswipeRefresh() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
    }


    private void showWeatherInfo(final Weather weather) {

        titleCity.setText(weather.basic.cityName);
        titleUpdateTime.setText("更新时间:"+weather.update.updateTime.split(" ")[1]);
        degreeText.setText(weather.now.temperature + "℃");
        weatherInfoText.setText(weather.now.more);
        forecastLayout.removeAllViews();
        for (Weather.Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_weather_forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.weather_date_text);
            TextView infoText = (TextView) view.findViewById(R.id.weather_info_text);
            TextView maxText = (TextView) view.findViewById(R.id.weather_max_text);
            TextView minText = (TextView) view.findViewById(R.id.weather_min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.moreDaytime);
            maxText.setText(forecast.tmpMax);
            minText.setText(forecast.tmpMin);
            forecastLayout.addView(view);
        }
        for (Weather.Lifestyle lifestyle : weather.lifestyleList) {
            if (lifestyle.type.equals("comf")) {
                comfText.setText("舒适度:" + lifestyle.txt);
            } else if (lifestyle.type.equals("drsg")) {
                drsgText.setText("穿衣指数:" + lifestyle.txt);

            } else if (lifestyle.type.equals("sport")) {
                sportText.setText("运动指数:" + lifestyle.txt);

            }
        }
        weatherLayout.setVisibility(View.VISIBLE);

    }

    private void showAirInfo(final Air air) {
        Log.d("111", "showAirInfo:"+air);
        aqiText.setText(air.aqi);
        pm25Text.setText(air.pm25);

    }
    @Override
    public void setPresenter(@NonNull WeatherContract.Presenter presenter ) {
        mPresenter = presenter;
    }
}
