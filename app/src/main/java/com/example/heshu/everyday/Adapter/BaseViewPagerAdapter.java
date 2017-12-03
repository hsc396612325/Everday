package com.example.heshu.everyday.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.view.AutoViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshu on 2017/11/29.
 */

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener{
    private List<T> data = new ArrayList<>();

    private Context mContent;
    private AutoViewPager mView;
    private OnAutoViewPagerItemClickListener listener;



    public BaseViewPagerAdapter(Context context ,List<T> data,OnAutoViewPagerItemClickListener listener){
        this.mContent = context;
        this.data = data;
        this.listener = listener;
    }

    public void init(AutoViewPager viewPager,BaseViewPagerAdapter adapter){
        mView = viewPager;
        mView.setAdapter(this);
        mView.addOnPageChangeListener(this);

        if(data == null || data.size()==0){
            return;
        }
        //设置初始为中间，这样一开始就能够往左滑动
        int position = Integer.MAX_VALUE/2-(Integer.MAX_VALUE/2)%getRealCount();
        mView.setCurrentItem(position);

        mView.start();
        mView.updatePaintView(getRealCount());
    }

    public void setListener(OnAutoViewPagerItemClickListener listener){this.listener = listener;}

    public void add(T t){
        data.add(t);
        notifyDataSetChanged();
        mView.updatePaintView(getRealCount());
    }

    public int getRealCount(){ return data == null ? 0 : data.size(); }
    public int getCount(){ return data == null ? 0 : Integer.MAX_VALUE; }

    public  void destroyItem(ViewGroup container , int position , Object object){
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view =(View) LayoutInflater.from(mContent).inflate(R.layout.activity_open_eyes_head,container,false);

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(position%getRealCount(),data.get(position % getRealCount()));
                }
            }
        });

        //图片加载
        loadImage(view , position,data.get(position % getRealCount()));
        container.addView(view);
        return view;
    }
    //加载图片的方法也是一个抽象方法
    public abstract void loadImage(View view ,int position ,T t);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view == object;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mView.onPageSelected(position % getRealCount());
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //图片点击事件接口
    public interface OnAutoViewPagerItemClickListener<T> {
        void onItemClick(int position,T t);
    }
}
