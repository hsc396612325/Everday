package com.example.heshu.everyday.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.util.App;

/**
 * Created by heshu on 2018/5/9.
 */

public class MovieFragment extends Fragment implements View.OnTouchListener {
    //定义手势检测器实例
    GestureDetector mGestureDetector;
    GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.e("<--滑动测试-->", "开始滑动");
            float x = e1.getX()-e2.getX();
            float x2 = e2.getX()-e1.getX();
            if(x>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                Toast.makeText(App.getContext(), "向左手势", Toast.LENGTH_SHORT).show();


            }else if(x2>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                Toast.makeText(App.getContext(), "向右手势", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    };


    private static final int FLING_MIN_DISTANCE = 50;   //最小距离
    private static final int FLING_MIN_VELOCITY = 0;  //最小速度
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        mGestureDetector = new GestureDetector(App.getContext(), mOnGestureListener);

        RelativeLayout mRelativeLayout = (RelativeLayout)view.findViewById(R.id.id_testRelative);//布局的主容器
        mRelativeLayout.setOnTouchListener(this);//将主容器的监听交给本activity，本activity再交给mGestureDetector
        mRelativeLayout.setLongClickable(true);   //必需设置这为true 否则也监听不到手势

        return view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return  mGestureDetector.onTouchEvent(motionEvent);
    }
}
