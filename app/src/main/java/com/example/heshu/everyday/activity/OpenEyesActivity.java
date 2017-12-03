package com.example.heshu.everyday.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Picture;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heshu.everyday.Adapter.BaseViewPagerAdapter;
import com.example.heshu.everyday.Adapter.VideoAdapter;
import com.example.heshu.everyday.Data.Video;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.gson.openeyes.Data;
import com.example.heshu.everyday.gson.openeyes.Item;
import com.example.heshu.everyday.util.HttpUtil;
import com.example.heshu.everyday.util.Utility;
import com.example.heshu.everyday.view.AutoScrollViewPager;
import com.example.heshu.everyday.view.AutoViewPager;
import com.example.heshu.everyday.view.VerticalScrollView;
import com.example.heshu.everyday.view.VerticalSwipeRefreshLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OpenEyesActivity extends AppCompatActivity {
    private AutoScrollViewPager mViewPager;

    private List<Video> videoList = new ArrayList<>();
    private List<Video> mAutoViewList = new ArrayList<>();
    private VideoAdapter adapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private VerticalScrollView openEyesLayout;
    private VerticalSwipeRefreshLayout swipeRefresh;
    private DrawerLayout mDrawerLayout;
    private NavigationView mMenuNv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_eyes);

        //初始化
        initUI();

        //查看本地是否有缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String homeItemListString = prefs.getString("homeItemList",null);
        String hotItemListString = prefs.getString("hotItemList",null);
        if (homeItemListString != null){
            List<Item> homeItemList = Utility.handleItemListResponse(homeItemListString);
            showHomeUI(homeItemList);
        }else {
            openEyesLayout.setVisibility(View.INVISIBLE);
            requestHomeItemList();
        }

        if(hotItemListString != null){
            List<Item>hotItemList = Utility.handleItemListResponse(hotItemListString);
            showHotUI(hotItemList);
        }else {
            openEyesLayout.setVisibility(View.INVISIBLE);
            requestHotItemList();
        }

    }

    private void initUI() {
        //初始化AutoScrollViewPager对象
        mViewPager = (AutoScrollViewPager) findViewById(R.id.viewPager);

        //初始化瀑布流
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        openEyesLayout = (VerticalScrollView )findViewById(R.id.openeyes_layout);
        swipeRefresh = (VerticalSwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
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
        //初始化
        for(int i=0;i < 5;i++){

            Video video = new Video();
            video.setVideoText("俄罗斯创意广告：深夜狂奔，她在躲谁？");
            video.setVideoUrl("http://baobab.kaiyanapp.com/api/v1/playUrl?vid=59555&editionType=default&source=aliyun");
            video.setVideoDetails("不知何时，互联网藏下了无数把「利剑」，片刻不停地向我们袭来。挣脱、奔跑，只为了对这些「围剿者」说不！From PM");
            video.setVideoWeb("http://www.eyepetizer.net/detail.html?vid=59555");
            video.setVideoImageUrl("http://img.kaiyanapp.com/a5539e675d8d29a3776c1f9d955d5423.jpeg?imageMogr2/quality/60/format/jpg");
            mAutoViewList.add(video);
        }

        //设置Adapter，这里需要重写loadImage方法，在里面加载图片，这里我使用的是Picasso框架，你可以换成你自己的。
        mViewPager.setAdapter(new BaseViewPagerAdapter<Video>(this,mAutoViewList,listener) {
            @Override
            public void loadImage(View view, int position, Video video) {
                ImageView imageView = (ImageView)view.findViewById(R.id.head_img) ;
                TextView textView = (TextView)view.findViewById(R.id.head_text);
                Picasso.with(OpenEyesActivity.this).load(Uri.parse(video.getVideoImageUrl())).error(R.mipmap.ic_launcher).into(imageView);
                textView.setText(video.getVideoText());
            }
        });

        //刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestHomeItemList();
                requestHotItemList();
            }
        });

        mViewPager.setFocusable(true);
        mViewPager.setFocusableInTouchMode(true);
        mViewPager.requestFocus();
    }

    private void selectItem(int itemid) {
        Intent intent;
        switch (itemid) {
            case R.id.navigation_weather:
                intent = new Intent(OpenEyesActivity.this,WeatherActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_world:
                //intent = new Intent(OpenEyesActivity.this,WorldActivity.class);
                //startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_open_eyes:
                mDrawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }
    private void  requestHomeItemList() {
        String homeItemListUrl = "http://baobab.kaiyanapp.com/api/v4/tabs/selected";

        HttpUtil.sendOKHttpRequest(homeItemListUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OpenEyesActivity.this,"获取信息失败",Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                final List<Item> homeItemList = Utility.handleItemListResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(homeItemList != null){
                            //缓存
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(OpenEyesActivity.this).edit();
                            editor.putString("homeItemList",responseString);
                            editor.apply();
                            //更新UI
                            showHomeUI(homeItemList);
                            openEyesLayout.setVisibility(View.VISIBLE);
                            swipeRefresh.setRefreshing(false);
                        }else{
                            Toast.makeText(OpenEyesActivity.this,"获取信息失败",Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });
    }

    private void requestHotItemList() {
        String hotItemListUrl = "http://baobab.kaiyanapp.com/api/v4/discovery/hot";

        HttpUtil.sendOKHttpRequest(hotItemListUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OpenEyesActivity.this,"获取信息失败",Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                final List<Item> hotItemList = Utility.handleItemListResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hotItemList != null) {
                            //缓存
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(OpenEyesActivity.this).edit();
                            editor.putString("hotItemList", responseString);
                            editor.apply();
                            swipeRefresh.setRefreshing(false);
                            //更新UI
                            showHotUI(hotItemList);
                        } else {
                            Toast.makeText(OpenEyesActivity.this, "获取信息失败", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });
    }


    private void showHomeUI(List<Item> homeItemList) {

        int i,j;
        for(i=mAutoViewList.size()-1;i>=0;i--){
            mAutoViewList.remove(i);
            Log.d("videoList",""+i);
        }
        for(i=0,j=0;j < 5;i++){
            if(homeItemList.get(i).type.equals("video")){
                Video video = new Video();
                video.setVideoText(homeItemList.get(i).data.title);
                video.setVideoImageUrl(homeItemList.get(i).data.cover.feed);
                video.setVideoUrl(homeItemList.get(i).data.playUrl);
                video.setVideoDetails(homeItemList.get(i).data.description);
                video.setVideoWeb(homeItemList.get(i).data.webUrl.raw);
                mAutoViewList.add(video);
                j++;
            }
        }
        openEyesLayout.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }
    private void showHotUI(List<Item> hotItemList) {

        for(int i=videoList.size()-1;i>=0;i--){
            videoList.remove(i);
        }
        for(int i=0;i<hotItemList.size();i++){

            if(hotItemList.get(i).type.equals("video")){
                Video video = new Video();
                video.setVideoText(hotItemList.get(i).data.title);
                video.setVideoImageUrl(hotItemList.get(i).data.cover.feed);
                video.setVideoUrl(hotItemList.get(i).data.playUrl);
                video.setVideoDetails(hotItemList.get(i).data.description);
                video.setVideoWeb(hotItemList.get(i).data.webUrl.raw);
                videoList.add(video);
            }
        }
    }
    //定义点击事件
    private BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener = new BaseViewPagerAdapter.
            OnAutoViewPagerItemClickListener<Video>() {

        @Override
        public void onItemClick(int position, Video video) {
            Intent intent = new Intent(OpenEyesActivity.this,PlayActivity.class);
            intent.putExtra("Video_data",video);
            startActivity(intent);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得在销毁的时候调用onDestroy()方法。用来销毁定时器。
        mViewPager.onDestroy();
    }




}
