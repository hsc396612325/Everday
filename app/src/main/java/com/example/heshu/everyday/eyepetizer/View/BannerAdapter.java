package com.example.heshu.everyday.eyepetizer.View;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.heshu.everyday.eyepetizer.bean.Item;
import com.example.heshu.everyday.weather.db.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshu on 2018/10/27.
 */

public class BannerAdapter extends PagerAdapter {
    private int itemCount = 1;
    private List<Item> dadas = new ArrayList();
    private List<HomeBannerItem> viewList = new ArrayList<>();

    @Override

    public int getCount() {
        if (dadas == null)
            return 0;
        else return Integer.MAX_VALUE;
    }

    //决定一个页面view是否与instantiateItem(ViewGroup, int)
    // 方法返回的具体key对象相关联。
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //移除给定位置的item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position % itemCount));
        viewList.get(position % itemCount).releasePlayer();
    }

    //创建指定位置的页面视图
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (viewList.size() <= itemCount) {
            for (int i = 0; i < itemCount;i++) {
                HomeBannerItem homeBannerItem =new HomeBannerItem(container.getContext(), dadas.get(i));
                viewList.add(homeBannerItem);
            }
        }
        HomeBannerItem view = viewList.get(position%itemCount);
        container.addView(view);
        viewList.get(position%itemCount).play();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("点击", "")
//                val intent = Intent(view.context, PlayActivity::class.java)
//                intent.putExtra("data", datas!![position % itemCount])
//                view.context.startActivity(intent)
            }
        });
        return view;
    }

}
