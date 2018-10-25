package com.example.heshu.everyday.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.heshu.everyday.adapter.BaseViewPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by heshu on 2017/11/29.
 */

public class AutoViewPager extends ViewPager {
    private static final String TAG = "AutoViewPager";

    private  int currentItem;
    private Timer mTimer;
    private AutoTask mTask;

    public AutoViewPager(Context context) {
        super(context);
    }
    public AutoViewPager(Context context, AttributeSet attra){
        super(context,attra);
    }

    public void init(AutoViewPager viewPager, BaseViewPagerAdapter adapter){
        adapter.init(viewPager,adapter);
    }

    public  void onStop(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }
    public void start(){
        //先暂停
        onStop();
        if(mTimer == null){
            mTimer = new Timer();
        }
        //第一个3000表示3秒后开始执行，第二个3000表示每隔3秒执行一次
        mTimer.schedule(new AutoTask(),3000,3000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //这里开始滚动图片，先取到当前显示第几张图片。如果滚到最后一张时，回到第一张
            currentItem = getCurrentItem();
            if(currentItem == getAdapter().getCount()-1){
                currentItem = 0;
            }else {
                currentItem ++ ;
            }
            setCurrentItem(currentItem);
        }
    };

    private final static class AutoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
    private AutoHandler mHandler = new AutoHandler();

    public void updatePaintView(int size){
        if(getParent() instanceof AutoScrollViewPager){
            AutoScrollViewPager pager = (AutoScrollViewPager)getParent();
            pager.initPointView(size);
        }else {
            Log.e("TAG","Parent view not be AutoScrollViewPager");
        }
    }
    public void onPageSelected(int position) {
        AutoScrollViewPager pager = (AutoScrollViewPager) getParent();
        pager.updatePointView(position);
    }
    private class AutoTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(runnable);
        }
    }

    public void onDestroy(){ onStop();}

    public void onResume(){ start();}

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                onDestroy();
                break;
            case MotionEvent.ACTION_UP:
                onResume();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
