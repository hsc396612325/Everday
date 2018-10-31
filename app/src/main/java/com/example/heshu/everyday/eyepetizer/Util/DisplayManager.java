package com.example.heshu.everyday.eyepetizer.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by heshu on 2018/10/31.
 */

public class DisplayManager {
    private static  DisplayMetrics displayMetrics;

    private static int screenWidth;

    private  static int screenHeight;

    private static  int screenDpi;

    public DisplayManager(Context context) {
        displayMetrics = context.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        screenDpi = displayMetrics.densityDpi;
    }

    //UI图的大小
    private  static int STANDARD_WIDTH = 1080;
    private  static int STANDARD_HEIGHT = 1920;

    public static  int getScreenWidth() {
        return screenWidth;
    }

    public static  int getScreenHeight() {
        return screenHeight;
    }


    /**
     * 传入UI图中问题的高度，单位像素
     *
     * @param size
     * @return
     */
    public  static int getPaintSize(int size) {
        return getRealHeight(size);
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     *
     * @param px ui图中的大小
     * @return
     */
    public  static int getRealWidth(int px) {
        //ui图的宽度
        return getRealWidth(px, (float) STANDARD_WIDTH);
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px          ui图中的大小
     * @param parentWidth 父view在ui图中的高度
     * @return
     */
    public static  int getRealWidth(int px, float parentWidth) {
        return (int) (px / parentWidth * getScreenWidth());
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     *
     * @param px ui图中的大小
     * @return
     */
    public  static  int getRealHeight(int px) {
        //ui图的宽度
        return getRealHeight(px, (float) STANDARD_HEIGHT);
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px           ui图中的大小
     * @param parentHeight 父view在ui图中的高度
     * @return
     */
    public static  int getRealHeight(int px, float parentHeight) {
        Log.e("getScreenHeight()", "" + getScreenHeight());
        return (int) (px / parentHeight * getScreenHeight());
    }

    /**
     * dip转px
     *
     * @param dipValue
     * @return int
     */
    public static  int dip2px(float dipValue) {
        float scale = displayMetrics.density;
        return (int) (dipValue * scale + 0.5f);
    }
}
