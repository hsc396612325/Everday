package com.example.heshu.everyday.eyepetizer.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.eyepetizer.bean.Item;
import com.example.heshu.everyday.weather.mvp.contract.WeatherContract;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * Created by heshu on 2018/10/28.
 */

public class HomeBannerItem extends FrameLayout {

    private Item data;

    private boolean isVideo = false;
    private boolean isInitVideoView = false;

    private ImageView imageView;
    private EmptyControlVideo videoView;

    public HomeBannerItem(@NonNull Context context, Item data) {
        super(context);
        this.data = data;
        initView();
        setUpView();
    }

    public HomeBannerItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.eyepetizer_item_home_banner_item, this);

        imageView = view.findViewById(R.id.imageView);
        videoView = view.findViewById(R.id.videoView);
    }

    private void setUpView() {
        String thumbPlayUrl ="";
        String feedImgUrl ="";
        //String thumbPlayUrl = data.data?.thumbPlayUrl
        imageView.setVisibility(View.VISIBLE);
        // String feedImgUrl = data.data?.cover?.feed
        Glide.with(getContext()).load(feedImgUrl).centerCrop().into(imageView);

        if (thumbPlayUrl == null || thumbPlayUrl == "") {
            isVideo = false;
            videoView.setVisibility(View.GONE);
        } else {
            isVideo = true;
            videoView.setVisibility(View.VISIBLE);
            videoView.setUp(thumbPlayUrl, false, "");
            videoView.setLooping(true);
            GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        }
    }

    public void initVideoView() {
        isInitVideoView = true;
        videoView.setVideoAllCallBack(new EmptyControlVideo.EmptyControlVideoCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                imageView.setVisibility(View.VISIBLE);
                videoView.startPlayLogic();
            }

            @Override
            public void onPlayError(String url, Object... objects) {
                imageView.setVisibility(View.VISIBLE);
                videoView.startPlayLogic();
            }
        });
    }
    /**
     * 开始播放
     */
    public void play(){
        if(!isInitVideoView && videoView.getVisibility() == View.VISIBLE){
            videoView.startPlayLogic();
            initVideoView();
        }
    }
    /**
     * 释放播放器
     */
    public void releasePlayer() {
        isInitVideoView = false;
        if (videoView.getVisibility() == View.VISIBLE) {
//            videoView.setStandardVideoAllCallBack(null)
            GSYVideoPlayer.releaseAllVideos();
        }
    }
}
