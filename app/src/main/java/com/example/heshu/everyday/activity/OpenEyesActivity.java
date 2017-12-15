package com.example.heshu.everyday.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshu.everyday.Adapter.BaseViewPagerAdapter;
import com.example.heshu.everyday.Adapter.VideoAdapter;
import com.example.heshu.everyday.Data.Video;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.gson.openeyes.ItemList;
import com.example.heshu.everyday.util.HttpUtil;
import com.example.heshu.everyday.util.Utility;
import com.example.heshu.everyday.view.AutoScrollViewPager;
import com.example.heshu.everyday.view.VerticalScrollView;
import com.example.heshu.everyday.view.VerticalSwipeRefreshLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OpenEyesActivity extends BaseActivity {
    private AutoScrollViewPager mViewPager;
    private List<Video> videoList = new ArrayList<>();
    private List<Video> mAutoViewList = new ArrayList<>();
    private VideoAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private VerticalScrollView openEyesLayout;
    private VerticalSwipeRefreshLayout swipeRefresh;
    private DrawerLayout mDrawerLayout;
    private NavigationView mMenuNv;
    private String nextUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_eyes);

        //初始化
        initUI();
        upload();
    }

    private void initUI() {
        //初始化瀑布流
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( mLinearLayoutManager);
        adapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);


        //刷新
        swipeRefresh = (VerticalSwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestHomeItemList();
                requestHotItemList();
            }
        });
        openEyesLayout = (VerticalScrollView )findViewById(R.id.openeyes_layout);
        openEyesLayout.setScrollViewListener(new VerticalScrollView.IScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                Log.d("onScrolledToBottom()","达到底部");
                requestAddHotItemList(nextUrl);
            }
        });
        //抽屉视图
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

        //初始化AutoScrollViewPager对象
        mViewPager = (AutoScrollViewPager) findViewById(R.id.viewPager);
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
        mViewPager.setFocusable(true);
        mViewPager.setFocusableInTouchMode(true);
        mViewPager.requestFocus();
    }

    private void upload(){
        //查看本地是否有缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String homeItemListString = prefs.getString("homeItemList",null);
        String hotItemListString = prefs.getString("hotItemList",null);
        if (homeItemListString != null){
            ItemList homeItemList = Utility.handleItemListResponse(homeItemListString);
            showHomeUI(homeItemList);
        }else {
            openEyesLayout.setVisibility(View.INVISIBLE);
            requestHomeItemList();
        }

        if(hotItemListString != null){
            ItemList hotItemList = Utility.handleItemListResponse(hotItemListString);
            nextUrl = hotItemList.nextPageUrl;
            showHotUI(hotItemList);
        }else {
            openEyesLayout.setVisibility(View.INVISIBLE);
            requestHotItemList();
        }
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
            case R.id.navigation_article:
                intent = new Intent(OpenEyesActivity.this,ArticleActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_zhuhu:
                intent = new Intent(OpenEyesActivity.this,ZhiHuActivity.class);
                startActivity(intent);
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
                final ItemList homeItemList = Utility.handleItemListResponse(responseString);
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

                        }else{
                            Toast.makeText(OpenEyesActivity.this,"获取信息失败",Toast.LENGTH_SHORT);
                        }
                        openEyesLayout.setVisibility(View.VISIBLE);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void requestHotItemList() {
        final String hotItemListUrl = "http://baobab.kaiyanapp.com/api/v4/discovery/hot";

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
                final ItemList hotItemList = Utility.handleItemListResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hotItemList != null) {
                            //缓存
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(OpenEyesActivity.this).edit();
                            editor.putString("hotItemList", responseString);
                            editor.apply();
                            //更新UI
                            showHotUI(hotItemList);
                            nextUrl = hotItemList.nextPageUrl;
                            openEyesLayout.setVisibility(View.VISIBLE);
                            swipeRefresh.setRefreshing(false);
                        } else {
                            Toast.makeText(OpenEyesActivity.this, "获取信息失败", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });
    }

    private void  requestAddHotItemList(String homeItemListUrl) {

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
                final ItemList addHomeItemList = Utility.handleItemListResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(addHomeItemList != null){
                            //更新UI
                            showAddHotUI(addHomeItemList);
                            nextUrl = addHomeItemList.nextPageUrl;

                        }else{
                            Toast.makeText(OpenEyesActivity.this,"获取信息失败",Toast.LENGTH_SHORT);
                        }
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e) {}
                        openEyesLayout.setVisibility(View.VISIBLE);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showHomeUI(ItemList homeItemList) {

        int i,j;
        for(i=mAutoViewList.size()-1;i>=0;i--){
            mAutoViewList.remove(i);
            Log.d("videoList",""+i);
        }
        for(i=0,j=0;j < 5;i++){
            if(homeItemList.itemList.get(i).type.equals("video")){
                Video video = new Video();
                video.setVideoText(homeItemList.itemList.get(i).data.title);
                video.setVideoImageUrl(homeItemList.itemList.get(i).data.cover.feed);
                video.setVideoUrl(homeItemList.itemList.get(i).data.playUrl);
                video.setVideoDetails(homeItemList.itemList.get(i).data.description);
                video.setVideoWeb(homeItemList.itemList.get(i).data.webUrl.raw);
                mAutoViewList.add(video);
                j++;
            }
        }
        openEyesLayout.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void showHotUI(ItemList hotItemList) {

        for(int i=videoList.size()-1;i>=0;i--){
            videoList.remove(i);
        }
        for(int i=0;i<hotItemList.itemList.size();i++){

            if(hotItemList.itemList.get(i).type.equals("video")){
                Video video = new Video();
                video.setVideoText(hotItemList.itemList.get(i).data.title);
                video.setVideoImageUrl(hotItemList.itemList.get(i).data.cover.feed);
                video.setVideoUrl(hotItemList.itemList.get(i).data.playUrl);
                video.setVideoDetails(hotItemList.itemList.get(i).data.description);
                video.setVideoWeb(hotItemList.itemList.get(i).data.webUrl.raw);
                videoList.add(video);
            }
        }
    }

    private void showAddHotUI(ItemList addHomeItemList) {

        for(int i=0;i<addHomeItemList.itemList.size();i++){

            if(addHomeItemList.itemList.get(i).type.equals("video")){
                Video video = new Video();
                video.setVideoText(addHomeItemList.itemList.get(i).data.title);
                video.setVideoImageUrl(addHomeItemList.itemList.get(i).data.cover.feed);
                video.setVideoUrl(addHomeItemList.itemList.get(i).data.playUrl);
                video.setVideoDetails(addHomeItemList.itemList.get(i).data.description);
                video.setVideoWeb(addHomeItemList.itemList.get(i).data.webUrl.raw);
                videoList.add(video);
            }
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得在销毁的时候调用onDestroy()方法。用来销毁定时器。
        mViewPager.onDestroy();
    }

    //定义轮播图的点击事件
    private BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener = new BaseViewPagerAdapter.
            OnAutoViewPagerItemClickListener<Video>() {

        @Override
        public void onItemClick(int position, Video video) {
            Intent intent = new Intent(OpenEyesActivity.this,PlayActivity.class);
            intent.putExtra("Video_data",video);
            startActivity(intent);
        }
    };
}
