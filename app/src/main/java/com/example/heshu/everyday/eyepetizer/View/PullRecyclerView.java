package com.example.heshu.everyday.eyepetizer.View;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.eyepetizer.View.HomeBanner;

/**
 * Created by heshu on 2018/10/26.
 */

public class PullRecyclerView extends RecyclerView {

    private int pullDistance = 300;  //下拉高度达到这个的时候，松开手才会刷新
    private int originalFirstItemHeight = 0;
    private int originalFirstItemWeight = 0;
    private int downY = -1;

    //down之后下次up之前，这个值不变，用来实现loading的缩放比例
    private int constDownY = -1;
    private float constUpY = -1f;
    private boolean canRefresh = false; //是否刷新
    private boolean isFirstMove = true; //是否为第一次滑动
    private int tempWidth = -1;
    private int dx = 0;
    //homeBanner: HomeBanner? = null 轮播图

    private boolean willRefresh = false;//松手后可刷新

    private float mLastMotionY = 0f;
    private float mLastMotionX = 0f;
    private float deleaY = 0f;
    private float deleaX = 0f;


    private RelativeLayout loadingView;
    private ImageView loading;

    public PullRecyclerView(Context context) {
        this(context, null);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public PullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        loading = new ImageView(context);
        loading.setImageResource(R.mipmap.eye_loading_progress);

        loadingView = new RelativeLayout(context);
        loadingView.setBackgroundColor(0xaa000000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            loadingView.setGravity(Gravity.CENTER);
        }
        loadingView.addView(loading);
        loadingView.setLayoutParams(new ViewGroup.LayoutParams(getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean resume = super.onInterceptTouchEvent(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //发生down事件时，记录y坐标
                mLastMotionX = e.getX();
                mLastMotionY = e.getY();
                downY = (int) e.getY();
                constDownY = (int) e.getY();
                resume = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //deltaY > 0 是向下运动,< 0是向上运动
                deleaY = e.getY() - mLastMotionY;
                deleaX = e.getX() - mLastMotionX;
                if (Math.abs(deleaX) > Math.abs(deleaY)) {
                    resume = false; //左右滑动
                } else {
                    if (canScrollVertically(-1) && willRefresh) {
                        canRefresh = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                resume = true;
                break;
        }

        return resume;

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                constDownY = (int) e.getY();
                if (!canScrollVertically(-1) && willRefresh) {
                    canRefresh = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (isFirstMove) { //是否是第一次滑动
                    isFirstMove = false;

                    if (canRefresh) {
                        canRefresh = e.getY() - downY > 0; //是否向下滑
                    }
                }

                if (canRefresh) { //允许滑动
                    if (getChildAt(0) instanceof HomeBanner) {  //第一个是不轮播图
                        HomeBanner firstView = (HomeBanner) getChildAt(0);

                        if (!hasShow) {
                            showLoading(firstView); //添加firstview
                        }

                        float fl = e.getY() - constDownY;//fl从1-pullDistance   缩放比例从0-1
                        if ((fl <= 0)) {
                            return true;
                        }

                        setLoadingScale(fl); //缩放view

                        ViewGroup.LayoutParams layoutParams = firstView.getLayoutParams();
                        if (layoutParams.height < 0 || tempWidth < 0) {
//                            originalFirstItemHeight = getChildViewHolder(firstView)
                        }

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 根据距离原始位置的高度，计算loasing的缩放值
     * 并对ImageView进行缩放
     */
    private void setLoadingScale(float distanceY) {
        float distance = (distanceY - 150) / pullDistance;//下拉超过150之后开始逐渐出现loading
        if (distance >= 1) {
            distance = 1f;
        } else if (distance < 0) {
            distance = 0f;
        }
        loading.setScaleX(distance);
        loading.setScaleY(distance);
    }


    private boolean hasShow = false;

    private void showLoading(ViewGroup viewGroup) {
        hasShow = true;
        viewGroup.addView(loadingView);
    }


}
