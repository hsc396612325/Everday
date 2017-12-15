package com.example.heshu.everyday.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshu.everyday.Adapter.BaseViewPagerAdapter;
import com.example.heshu.everyday.Adapter.ZhihuRecyclerAdapter;
import com.example.heshu.everyday.Data.Video;
import com.example.heshu.everyday.Data.Zhihu;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.gson.zhihu.Before;
import com.example.heshu.everyday.gson.zhihu.News;
import com.example.heshu.everyday.util.HttpUtil;
import com.example.heshu.everyday.util.Utility;
import com.example.heshu.everyday.view.AutoScrollViewPager;
import com.example.heshu.everyday.view.VerticalScrollView;
import com.example.heshu.everyday.view.VerticalSwipeRefreshLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by heshu on 2017/12/9.
 */

public class ZhiHuActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mMenuNv;

    private RecyclerView mRecyclerView;
    private ZhihuRecyclerAdapter mZhihuRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Zhihu> mZhihuList = new ArrayList<>();

    private AutoScrollViewPager mViewPager;
    private List<Zhihu> mZhihuViewPagerList = new ArrayList<>();

    private VerticalSwipeRefreshLayout swipeRefresh;
    private VerticalScrollView mZhihuLayout;
    private String nextUrl;
    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_eyes);

        date = new Date();
        Log.d("data",date.getYear()+1900+"");
        Log.d("data",date.getMonth()+1+"");
        Log.d("data",date.getDate()+"");
        //初始化
        initUI();
        upload();

    }
    private void initUI() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mMenuNv=(NavigationView) findViewById(R.id.nv_layout);
        mMenuNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectItem(item.getItemId());
                // 关闭侧滑菜单
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        initData();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager( mLinearLayoutManager);
        mZhihuRecyclerAdapter = new ZhihuRecyclerAdapter(this,mZhihuList);
        mRecyclerView.setAdapter(mZhihuRecyclerAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        mViewPager = (AutoScrollViewPager) findViewById(R.id.viewPager);
        mViewPager .setAdapter(new BaseViewPagerAdapter<Zhihu>(this,mZhihuViewPagerList,listener) {

            @Override
            public void loadImage(View view, int position, Zhihu zhihu) {
                ImageView imageView = (ImageView)view.findViewById(R.id.head_img) ;
                TextView textView = (TextView)view.findViewById(R.id.head_text);
                Picasso.with(ZhiHuActivity.this).load(Uri.parse(zhihu.getImagesUrl())).error(R.mipmap.ic_launcher).into(imageView);
                textView.setText(zhihu.getTitle());
            }
        });
        mViewPager.setFocusable(true);
        mViewPager.setFocusableInTouchMode(true);
        mViewPager.requestFocus();

        mZhihuLayout = (VerticalScrollView )findViewById(R.id.openeyes_layout);
        mZhihuLayout.setScrollViewListener(new VerticalScrollView.IScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                Log.d("onScrolledToBottom()","达到底部");
                //更新
                date.setTime(date.getTime()-24*60*60*1000);
                requestBefore();
            }
        });
        //刷新
        swipeRefresh = (VerticalSwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestNews();
            }
        });

    }

    private void initData() {
        for(int i=0;i < 5;i++){
            Zhihu zhihuViewPager = new Zhihu();
            zhihuViewPager.setImagesUrl("http://img.kaiyanapp.com/a5539e675d8d29a3776c1f9d955d5423.jpeg?imageMogr2/quality/60/format/jpg");
            zhihuViewPager.setTitle("俄罗斯创意广告：深夜狂奔，她在躲谁？");
            mZhihuViewPagerList.add(zhihuViewPager);
        }
    }

    private void upload() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String zhihuNews = prefs.getString("ZHihuNews",null);
        if(zhihuNews != null){
            News news = Utility.handleNewsResponse(zhihuNews);
            Log.d("11","22");
            showNewsUI(news);
        }else {
            mZhihuLayout.setVisibility(View.INVISIBLE);
            requestNews();
        }
    }


    private void requestNews() {
        String NewsUrl = "https://news-at.zhihu.com/api/4/news/latest";

        HttpUtil.sendOKHttpRequest(NewsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ZhiHuActivity.this,"获取信息失败",Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                final News news = Utility.handleNewsResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ZhiHuActivity.this).edit();
                        editor.putString("ZHihuNews",responseString);
                        editor.apply();
                        showNewsUI(news);
                        mZhihuLayout.setVisibility(View.VISIBLE);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void  requestBefore(){

        String str = (date.getYear()+1900)+""+ (date.getMonth()+1>10?(date.getMonth()+1):"0"+(date.getMonth()+1))
                +(date.getDate()>=10?(date.getDate()):("0"+date.getDate()));
        Log.d("str",str);
        String beforeUrl = "https://news-at.zhihu.com/api/4/news/before/"+str;

        HttpUtil.sendOKHttpRequest(beforeUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ZhiHuActivity.this,"获取信息失败",Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                final Before before = Utility.handleBeforeResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        showBeforeUI(before);
                        Log.d("news",before.date);
                        mZhihuLayout.setVisibility(View.VISIBLE);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showNewsUI(News news) {
        for(int i = mZhihuList.size()-1;i>=0;i--){
            mZhihuList.remove(i);
        }

        for(int i =mZhihuViewPagerList.size()-1;i>=0;i--){
            mZhihuViewPagerList.remove(i);
        }

        Zhihu zhihu2 = new Zhihu();
        zhihu2.setType(Zhihu.ITEM_TYPE_Z.ITEM2);
        zhihu2.setDate("今日热闻");
        mZhihuList.add(zhihu2);

        for (int i = 0 ; i < news.stories.size() ; i++){
            Zhihu zhihu3 = new Zhihu();
            zhihu3.setType(Zhihu.ITEM_TYPE_Z.ITEM3);
            zhihu3.setTitle(news.stories.get(i).title);
            zhihu3.setImagesUrl(news.stories.get(i).images[0]);
            zhihu3.setId(news.stories.get(i).id);
            mZhihuList.add(zhihu3);
        }

        for(int i =0 ;i<news.topStories.size();i++){
            Zhihu zhihuViewPager = new Zhihu();
            zhihuViewPager.setTitle(news.topStories.get(i).title);
            zhihuViewPager.setImagesUrl(news.topStories.get(i).image);
            zhihuViewPager.setId(news.topStories.get(i).id);
            mZhihuViewPagerList.add(zhihuViewPager);
        }
    }

    private void showBeforeUI(Before before) {

        Zhihu zhihu2 = new Zhihu();
        zhihu2.setType(Zhihu.ITEM_TYPE_Z.ITEM2);
        zhihu2.setDate(before.date.substring(0,4)+"年"+before.date.substring(4,6)+"月"+before.date.substring(6,8)+"日");
        mZhihuList.add(zhihu2);

        for (int i = 0 ; i < before.stories.size() ; i++){
            Zhihu zhihu3 = new Zhihu();
            zhihu3.setType(Zhihu.ITEM_TYPE_Z.ITEM3);
            zhihu3.setTitle(before.stories.get(i).title);
            zhihu3.setImagesUrl(before.stories.get(i).images[0]);
            zhihu3.setId(before.stories.get(i).id);
            mZhihuList.add(zhihu3);
        }
        mZhihuRecyclerAdapter.notifyDataSetChanged();
    }

    private void selectItem(int itemid) {
        Intent intent;
        switch (itemid) {
            case R.id.navigation_weather:
                intent = new Intent(ZhiHuActivity.this,WeatherActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_world:
                //intent = new Intent(OpenEyesActivity.this,WorldActivity.class);
                //startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_open_eyes:
                intent = new Intent(ZhiHuActivity.this,OpenEyesActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_article:
                intent = new Intent(ZhiHuActivity.this,ArticleActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_zhuhu:
                mDrawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    //定义轮播图的点击事件
    private BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener = new BaseViewPagerAdapter.
            OnAutoViewPagerItemClickListener<Zhihu>() {
        @Override
        public void onItemClick(int position, Zhihu zhihu) {
            Intent intent = new Intent(ZhiHuActivity.this,ZhihuDetailsActivity.class);
            intent.putExtra("ZhihuId",zhihu.getId());
            startActivity(intent);
        }
    };
}
