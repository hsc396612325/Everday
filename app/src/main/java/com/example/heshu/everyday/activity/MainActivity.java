package com.example.heshu.everyday.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.fragment.ArticleFragment;
import com.example.heshu.everyday.fragment.ChooseAreaFragment;
import com.example.heshu.everyday.fragment.MovieFragment;
import com.example.heshu.everyday.fragment.OpenEyesFragment;
import com.example.heshu.everyday.fragment.SettingFragment;
import com.example.heshu.everyday.fragment.WeatherFragment;
import com.example.heshu.everyday.fragment.WorldFragment;
import com.example.heshu.everyday.fragment.ZhihuFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mWeatherId = prefs.getString("WeatherId", null);
        Log.d("1111", "mWeatherId: "+mWeatherId);
        if(mWeatherId == null){
            replaceFragment(new ChooseAreaFragment());
        }else {
            replaceFragment(new WeatherFragment());
        }
    }

    private void initView(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton drawerButton = (ImageButton) findViewById(R.id.nav_button);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        drawerButton.getBackground().setAlpha(100);

    }
    @Override //回退是调用
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
           mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed(); //回退
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigation_weather) {  // 天气
            replaceFragment( new WeatherFragment());
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//打开手势滑动
        } else if (id == R.id.navigation_world) { //世界
            replaceFragment( new WorldFragment());
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//打开手势滑动
        } else if (id == R.id.navigation_open_eyes) { //开眼
            replaceFragment( new OpenEyesFragment());
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//打开手势滑动
        } else if (id == R.id.navigation_article) {  // 观止
            replaceFragment( new ArticleFragment());
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//打开手势滑动
        } else if (id == R.id.navigation_zhihu) {  //知乎
            replaceFragment( new ZhihuFragment());
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//打开手势滑动
        } else if (id == R.id.navigation_movie) { //电影
            replaceFragment( new MovieFragment());
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//打开手势滑动
        } else if (id == R.id.navigation_setting) { //设置
            replaceFragment( new SettingFragment());
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//打开手势滑动
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.search_edit_frame,fragment).commit();
    }
}
