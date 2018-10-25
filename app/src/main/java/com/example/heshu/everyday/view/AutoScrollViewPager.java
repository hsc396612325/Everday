package com.example.heshu.everyday.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.adapter.BaseViewPagerAdapter;

/**
 * Created by heshu on 2017/11/29.
 */

 public class AutoScrollViewPager extends RelativeLayout {
    private AutoViewPager mViewPager;

    private Context mContext;

    private LinearLayout layout;

    public AutoScrollViewPager(Context context) {
        super(context);
        init(context);
    }
    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mViewPager = new AutoViewPager(context);
        layout = new LinearLayout(mContext);
        addView(mViewPager);
    }

    public  void setAdapter(BaseViewPagerAdapter adapter){
        if(mViewPager != null){
            mViewPager.init(mViewPager,adapter);
        }
    }

    public AutoViewPager getViewPager(){return  mViewPager;}

    public void initPointView(int size){
        layout = new LinearLayout(mContext);
        for(int i =0 ;i < size ; i++){
            ImageView imageView = new ImageView(mContext);
            //LayoutParams类是用于child view（子视图） 向 parent view（父视图）传达自己的意愿的一个东西
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);
            params.leftMargin = 8;
            params.gravity = Gravity.CENTER;
            imageView.setLayoutParams(params);
            if(i == 0){
                imageView.setBackgroundResource(R.drawable.point_checked);
            }else {
                imageView.setBackgroundResource(R.drawable.point_normal);
            }
            layout.addView(imageView);
        }

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(12, 20, 12, 20);
        layout.setLayoutParams(layoutParams);
        addView(layout);
    }

    public void updatePointView(int position){
        int size = layout.getChildCount();
        for(int i = 0 ; i < size ; i++){
            ImageView imageView = (ImageView)layout.getChildAt(i);
            if (i == position) {
                imageView.setBackgroundResource(R.drawable.point_checked);
            } else {
                imageView.setBackgroundResource(R.drawable.point_normal);
            }
        }
    }

    public void onDestroy() {
        if (mViewPager != null) {
            mViewPager.onDestroy();
        }
    }


}
