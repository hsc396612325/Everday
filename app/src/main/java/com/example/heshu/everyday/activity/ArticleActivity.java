package com.example.heshu.everyday.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.gson.article.Data;
import com.example.heshu.everyday.util.HttpUtil;
import com.example.heshu.everyday.util.Utility;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by heshu on 2017/12/3.
 */

public class ArticleActivity extends  BaseActivity {
    private SwipeRefreshLayout swipeRefresh;
    private ScrollView articleLayout;
    private TextView articleContentText;
    private String yesterday;
    private String tomorrow;
    private String today;
    private DrawerLayout mDrawerLayout;
    private NavigationView mMenuNv;
    private ArticlePattern pattern;
    private TextView articleContentTitleText;
    private TextView articleAuthor;
    private TextView articleWc;
    private FloatingActionButton todayFab;
    private FloatingActionButton randomFab;
    private FloatingActionButton tomorrowFab;
    private FloatingActionButton yesterdayFab;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Log.d("ArticleActivity","ArticleActivity");
        initUI();
        upload();

    }

    private void initUI() {
        pattern =ArticlePattern.TODAY;
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        articleLayout = (ScrollView)findViewById(R.id.article_layout);
        articleContentText = (TextView)findViewById(R.id.article_content);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        articleContentTitleText = (TextView)findViewById(R.id.article_content_title);
        articleAuthor = (TextView)findViewById(R.id.article_author);
        articleWc = (TextView)findViewById(R.id.article_wc);
        mMenuNv=(NavigationView) findViewById(R.id.nv_layout);
        todayFab = (FloatingActionButton)findViewById(R.id.today_fab);
        randomFab = (FloatingActionButton)findViewById(R.id.random_fab);
        tomorrowFab = (FloatingActionButton)findViewById(R.id.tomorrow_fab);
        yesterdayFab = (FloatingActionButton)findViewById(R.id.yesterday_fab);
        mMenuNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectItem(item.getItemId());
                // 关闭侧滑菜单
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestArticle();
            }
        });
        todayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pattern =ArticlePattern.TODAY;
                requestArticle();
            }
        });
        randomFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pattern =ArticlePattern.RANDOMDAY;
                requestArticle();
            }
        });
        yesterdayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pattern =ArticlePattern.YESTERDAY;
                requestArticle();
            }
        });
        tomorrowFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tomorrow.compareTo(today)<=0) {
                    pattern = ArticlePattern.TOMORROW;
                    requestArticle();
                }
            }
        });
    }


    private void selectItem(int itemid) {
        Intent intent;
        switch (itemid) {
            case R.id.navigation_weather:
                intent = new Intent(ArticleActivity.this,WeatherActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_world:
                //intent = new Intent(ArticleActivity.this,WorldActivity.class);
                //startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_open_eyes:
                intent = new Intent(ArticleActivity.this,OpenEyesActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_article:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.navigation_zhuhu:
                intent = new Intent(ArticleActivity.this,ZhiHuActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void upload(){
        //本地缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String articleString = prefs.getString("article",null);
        if(articleString != null){
            Data data  = Utility.handleArticleResponse(articleString);
            yesterday = data.article.date.prev;
            tomorrow = data.article.date.next;
            if(pattern == ArticlePattern.TODAY){
                today = data.article.date.curr;
            }
            showUI(data);
        }else {
            articleLayout.setVisibility(View.INVISIBLE);
            requestArticle();
        }
    }



    private void requestArticle() {
        String ArticleURL = "https://interface.meiriyiwen.com/article/";
        String uPURL="" ;
        if(pattern == ArticlePattern.TODAY){
            uPURL = "today?dev=1";
        }else if(pattern == ArticlePattern.YESTERDAY){
            uPURL = "day?dev=1&date=" + yesterday;
        }else if(pattern == ArticlePattern.TOMORROW) {
            uPURL = "day?dev=1&date=" + tomorrow;
        }else if(pattern == ArticlePattern.RANDOMDAY){
            uPURL = "random?dev=1";
        }
        Log.d("URL",ArticleURL + uPURL);
        HttpUtil.sendOKHttpRequest(ArticleURL + uPURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ArticleActivity.this,"天气获取失败",Toast.LENGTH_SHORT);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] b = response.body().bytes();
                final String responseText = new String(b,"utf-8");
                Log.d("JSON",responseText);
                final Data data = Utility.handleArticleResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(true){
                            yesterday = data.article.date.prev;
                            tomorrow = data.article.date.next;
                            if(pattern == ArticlePattern.TODAY){
                                today = data.article.date.curr;
                            }
                             SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ArticleActivity.this).edit();
                            editor.putString("article",responseText);
                            editor.apply();
                            //更新界面
                            articleLayout.setVisibility(View.VISIBLE);
                            showUI(data);
                               Log.d("JSON",responseText);

                        }else {
                            Toast.makeText(ArticleActivity.this,"天气获取失败",Toast.LENGTH_SHORT);
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private enum ArticlePattern {
        TODAY,
        YESTERDAY,
        TOMORROW,
        RANDOMDAY
    }

    private void showUI(Data data) {

        String text=data.article.articleContent.replaceAll("<p>","        ");
        articleContentText.setText(text.replaceAll("</p>","\n\n"));
        articleContentTitleText.setText(data.article.title);
        articleAuthor.setText(data.article.author);
        articleWc.setText("全文完  ，共"+data.article.wc+"字");
    }
}
