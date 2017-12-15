package com.example.heshu.everyday.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by heshu on 2017/11/11.
 */

public class HttpUtil {
    public static void sendOKHttpRequest(String address ,okhttp3.Callback callback){
        Log.e("asd",address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address)
                .addHeader("Content-Type"," application/json;charset=utf-8")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                //.addHeader("Accept-Encoding","gzip, deflate, br")
                .addHeader("Accept-Language","zh-CN,zh;q=0.9")
                .addHeader("Cache-Control","max-age=0")
                .addHeader("Connection","keep-alive")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                .build();
        client.newCall(request).enqueue(callback);
    }
}
