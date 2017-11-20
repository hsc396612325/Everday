package com.example.heshu.everyday.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.fragment.ChooseAreaFragment;
import com.example.heshu.everyday.gson.Air;
import com.example.heshu.everyday.gson.Forecast;
import com.example.heshu.everyday.gson.Lifestyle;
import com.example.heshu.everyday.gson.Weather;
import com.example.heshu.everyday.util.HttpUtil;
import com.example.heshu.everyday.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

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
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initUi();
        upload();

    }
    private void initUi() {
        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.weather_title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.weather_degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.weather_forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text= (TextView)findViewById(R.id.pm25_text);
        comfText = (TextView)findViewById(R.id.comf_text);
        drsgText = (TextView)findViewById(R.id.drsg_text);
        sportText = (TextView)findViewById(R.id.sport_text);
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
                requestAir(mWeatherId);
                loadBingPic();
            }
        });
        titleCity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.activity_weather,new ChooseAreaFragment());
                transaction.commit();
            }
        });
    }
    private void upload() {
        //加载天气信息
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if(weatherString != null){
            //有缓存时直接查询天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.WeatherId;
            showWeatherInfo(weather);
        }else {
            //无缓存去服务器上查询
            mWeatherId  = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId );
        }

        //加载空气质量信息
        String airString = prefs.getString("air",null);
        if(airString!=null){
            Air air = Utility.handleAirResponse(airString);
            showAirInfo(air);
        }else {
            //无缓存去服务器上查询
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestAir(weatherId);
        }
        //加载图片
        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }

    }



    public void requestWeather(String weatherId) {
        mWeatherId = weatherId;
        String weatherUrl = "https://free-api.heweather.com/s6/weather?location="+
                weatherId +"&key=baccef9080f24fb895c350b8cdd72c6a";
        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override//失败
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"天气获取失败",Toast.LENGTH_SHORT);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override//成功
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的数据
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather !=null && "ok".equals(weather.status)){
                            //缓存到本地
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"天气获取失败",Toast.LENGTH_SHORT);
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    //下载图片
    public void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
    //下载air数据
    public void requestAir(String weatherId) {
        mWeatherId = weatherId;
        String airUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId+"&key=baccef9080f24fb895c350b8cdd72c6a";

        HttpUtil.sendOKHttpRequest(airUrl, new Callback() {
            @Override//失败
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"空气质量获取失败",Toast.LENGTH_SHORT);
                    }
                });
                swipeRefresh.setRefreshing(false);
            }

            @Override//成功
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的数据
                final String responseText = response.body().string();
                final Air air = Utility.handleAirResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(air !=null){
                            //缓存到本地
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("air",responseText);
                            editor.apply();
                            showAirInfo(air);
                        }else {
                            Toast.makeText(WeatherActivity.this,"空气质量获取失败",Toast.LENGTH_SHORT);
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }



    //展示Weather数据
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.updateTime.split(" ")[1];//获得时间，舍弃年
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.activity_weather_forecast_item,forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.weather_date_text);
            TextView infoText = (TextView)view.findViewById(R.id.weather_info_text);
            TextView maxText = (TextView)view.findViewById(R.id.weather_max_text);
            TextView minText = (TextView)view.findViewById(R.id.weather_min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.moreDaytime);
            maxText.setText(forecast.tmpMax);
            minText.setText(forecast.tmpMin);
            forecastLayout.addView(view);
        }
        String comfort=null;
        String drsg=null;
        String sport =null;
        for(Lifestyle lifestyle : weather.lifestyleList){
            if(lifestyle.type.equals("comf")){
                comfort = "舒适度:"+ lifestyle.txt;
            }else if(lifestyle.type.equals("drsg")){
                drsg = "穿衣指数:"+lifestyle.txt;
            }else if(lifestyle.type.equals("sport")){
                sport = "运动指数:"+lifestyle.txt;
            }
        }
        comfText.setText(comfort);
        drsgText.setText(drsg);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    //展示air数据
    private void showAirInfo(Air air) {
        String aqi = air.aqi;
        String pm25 = air.pm25;

        aqiText.setText(aqi);
        pm25Text.setText(pm25);
    }

}
