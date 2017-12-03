package com.example.heshu.everyday.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.heshu.everyday.activity.OpenEyesActivity;
import com.example.heshu.everyday.gson.openeyes.Item;
import com.example.heshu.everyday.gson.weather.Air;
import com.example.heshu.everyday.gson.weather.Weather;
import com.example.heshu.everyday.util.HttpUtil;
import com.example.heshu.everyday.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        updateAir();
        updateHomeVideo();
        updateHotVideo();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() +anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
    private void updateWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存直接解析天气数据
            final Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.WeatherId;
            String weatherUrl = "https://free-api.heweather.com/s6/weather?location="+
                    weatherId +"&key=baccef9080f24fb895c350b8cdd72c6a";
            HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if(weather!=null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }
    private void updateBingPic(){
        String requestBingPc =  "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPc, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }
    private void updateAir() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherId = prefs.getString("weather_id",null);
        if(weatherId!=null){
            String airUrl = "http://guolin.tech/api/weather?cityid=" +
                    weatherId+"&key=baccef9080f24fb895c350b8cdd72c6a";
            HttpUtil.sendOKHttpRequest(airUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Air air = Utility.handleAirResponse(responseText);
                    if(air!=null ){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("air",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    private void updateHomeVideo(){
        String homeItemListUrl = "http://baobab.kaiyanapp.com/api/v4/tabs/selected";
        HttpUtil.sendOKHttpRequest(homeItemListUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("homeItemList",responseString);
                editor.apply();
            }
        });
    }

    private void updateHotVideo(){
        String hotItemListUrl = "http://baobab.kaiyanapp.com/api/v4/discovery/hot";
        HttpUtil.sendOKHttpRequest(hotItemListUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("hotItemList",responseString);
                editor.apply();
            }
        });
    }
}
