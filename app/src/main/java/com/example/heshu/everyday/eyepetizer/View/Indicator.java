package com.example.heshu.everyday.eyepetizer.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by heshu on 2018/10/31.
 */

public class Indicator extends View {
    private Paint paint = new Paint();

    public Indicator(Context context) {
        this(context,null);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        setState(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2));
        canvas.drawCircle(0, 0, getMeasuredWidth() / 2, paint);
    }

    public void setState(boolean selected) {
        if (selected) {
            paint.setColor(0xffffffff);
        } else {
            paint.setColor(0x88ffffff);
        }
        invalidate();
    }
}
