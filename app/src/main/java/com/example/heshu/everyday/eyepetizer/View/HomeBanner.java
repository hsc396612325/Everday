package com.example.heshu.everyday.eyepetizer.View;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.eyepetizer.View.BannerAdapter;

/**
 * Created by heshu on 2018/10/26.
 */

public class HomeBanner extends FrameLayout {
    private BannerAdapter bannerAdapter = new BannerAdapter();
    private int msgWhat = 0;
    private int bannerItemCount = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            this.sendEmptyMessageDelayed(msgWhat, 5000);//2S后在发送一条消息，由于在handleMessage()方法中，造成死循环。
        }
    };

    public HomeBanner(@NonNull Context context) {
        this(context,null);
    }

    public HomeBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
       this(context, attrs,0);
    }

    public HomeBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomeBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        initListener();
        handler.sendEmptyMessageDelayed(msgWhat, 5000);
    }

    private void  initView(){
        View.inflate(getContext(), R.layout.eyepetizer_item_home_standard, this);
    }

    private void  initListener(){

    }


}
