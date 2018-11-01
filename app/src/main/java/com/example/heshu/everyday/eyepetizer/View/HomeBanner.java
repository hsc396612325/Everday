package com.example.heshu.everyday.eyepetizer.View;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.eyepetizer.Util.DisplayManager;
import com.example.heshu.everyday.eyepetizer.View.BannerAdapter;
import com.example.heshu.everyday.eyepetizer.bean.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshu on 2018/10/26.
 */

public class HomeBanner extends FrameLayout {

    private int msgWhat = 0;
    private int bannerItemCount = 1;

    private ViewPager bannerViewPager;
    private BannerAdapter bannerAdapter = new BannerAdapter();
    private LinearLayout bannerIndicators;

    private TextView bannerTitle;
    private TextView bannerSlogan;
    private TextView bannerInvisibleTitle;
    private TextView bannerInvisibleSlogan;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            this.sendEmptyMessageDelayed(msgWhat, 5000);//2S后在发送一条消息，由于在handleMessage()方法中，造成死循环。
        }
    };

    public HomeBanner(@NonNull Context context) {
        this(context, null);
    }

    public HomeBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomeBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        initListener();
        handler.sendEmptyMessageDelayed(msgWhat, 5000);
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.eyepetizer_item_home_banner, this);

        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        bannerViewPager.setAdapter(bannerAdapter);
        //自定义页面跳转
        bannerViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int width = page.getWidth();
                page.setScrollX((int) ((position * width) / 4 * 3));
            }
        });
        bannerIndicators = view.findViewById(R.id.bannerIndicators);

        bannerTitle = view.findViewById(R.id.bannerTitle);
        bannerSlogan = view.findViewById(R.id.bannerSlogan);
        bannerInvisibleTitle = view.findViewById(R.id.bannerInvisibleTitle);
        bannerInvisibleSlogan = view.findViewById(R.id.bannerInvisibleSlogan);

    }

    private void initListener() {
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            //position是被选中页面的索引，该方法在页面被选中或页面滑动足够距离切换到该页手指抬起时调用。
            @Override
            public void onPageScrollStateChanged(int state) {
                setTitleSlogan(state);
                for (int i = 0; i < bannerIndicators.getChildCount(); i++) {
                    if (i == state % bannerItemCount) {
                        ((Indicator) bannerIndicators.getChildAt(i)).setState(true);
                    } else {
                        ((Indicator) bannerIndicators.getChildAt(i)).setState(false);
                    }
                }
            }
        });
    }

    private void setTitleSlogan(int position) {
        Item bannerItemData = bannerAdapter.datas.get(position % bannerItemCount);
        TiaoZiUtil(bannerTitle, bannerInvisibleTitle, bannerItemData.data.title);
        TiaoZiUtil(bannerSlogan, bannerInvisibleSlogan, bannerItemData.data.sloga);
    }

    public void setData(List<Item> itemList) {
        bannerAdapter.datas = itemList;
        bannerItemCount = itemList.size();
        bannerAdapter.itemCount = bannerItemCount;
        bannerViewPager.setCurrentItem(bannerItemCount * 1000);
        bannerAdapter.notifyDataSetChanged();
        setIndicators(itemList);
        setTitleSlogan(0);
    }

    private void setIndicators(List<Item> bannerDatas) {
        bannerIndicators.removeAllViews();
        List<Item> bannerDatas1 = bannerDatas;
        for(Item bann :bannerDatas){
            Indicator indicator = new Indicator(getContext());
            MarginLayoutParams  layoutParams  = new LinearLayout.LayoutParams(DisplayManager.getRealHeight(20),DisplayManager.getRealHeight(20));
            layoutParams.leftMargin = DisplayManager.getRealWidth(10);
            layoutParams.rightMargin = DisplayManager.getRealWidth(10);
            indicator.setLayoutParams(layoutParams) ;

            bannerIndicators.addView(indicator);
        }

        ((Indicator)bannerIndicators.getChildAt(0)).setState(true);
    }
}
