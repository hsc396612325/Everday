package com.example.heshu.everyday.util;

import android.content.Context;

/**
 * Created by heshu on 2017/12/1.
 */

public class DensityUtil {
    public static final float getHeightInPx(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }
    public static final float getWidthInPx(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

}
