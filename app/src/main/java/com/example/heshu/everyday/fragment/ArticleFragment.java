package com.example.heshu.everyday.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.activity.MainActivity;
import com.example.heshu.everyday.gson.article.Data;
import com.example.heshu.everyday.mvp.presenter.ArticlePresenter;
import com.example.heshu.everyday.util.App;
import com.example.heshu.everyday.util.Utility;
import com.example.heshu.everyday.view.IArticleFragment;
import com.example.heshu.everyday.view.VerticalScrollView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Date;

/**
 * Created by heshu on 2018/5/9.
 */

public class ArticleFragment extends Fragment implements IArticleFragment, View.OnTouchListener {
    private SwipeRefreshLayout swipeRefresh;
    private VerticalScrollView articleLayout;
    private TextView articleContentText;
    private String yesterday;
    private String tomorrow;
    private String today;

    private ArticlePattern pattern;
    private TextView articleContentTitleText;
    private TextView articleAuthor;
    private TextView articleWc;
    private FloatingActionButton todayFab;
    private FloatingActionButton randomFab;
    private FloatingActionButton tomorrowFab;
    private FloatingActionButton yesterdayFab;

    private ArticlePresenter mPresenter;

    //定义手势检测器实例
    private static final int FLING_MIN_DISTANCE = 30;   //最小距离
    private static final int FLING_MIN_VELOCITY = 10;  //最小速度
    GestureDetector mGestureDetector;
    GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.e("<--滑动测试-->", "开始滑动");
            float x = e1.getX() - e2.getX();
            float x2 = e2.getX() - e1.getX();
            MainActivity mainActivity = (MainActivity) getActivity();
            if (x > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                Toast.makeText(App.getContext(), "后一天", Toast.LENGTH_SHORT).show();
                pattern = ArticlePattern.YESTERDAY;
                mPresenter.gainData(ArticlePattern.YESTERDAY, yesterday);

                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            } else if (x2 > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {

                if (tomorrow.compareTo(today) <= 0) {
                    Toast.makeText(App.getContext(), "前一天", Toast.LENGTH_SHORT).show();
                    pattern = ArticlePattern.TOMORROW;
                    mPresenter.gainData(ArticlePattern.TOMORROW, tomorrow);

                } else {
                    mainActivity.mDrawerLayout.openDrawer(Gravity.LEFT);
                    mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }

            return false;
        }


    };

    public enum ArticlePattern {
        TODAY,
        YESTERDAY,
        TOMORROW,
        RANDOMDAY
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        mPresenter = new ArticlePresenter(this);
        initView(view);
        upload();
        return view;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {

        pattern = ArticlePattern.TODAY;

        //滑动
        mGestureDetector = new GestureDetector(App.getContext(), mOnGestureListener);
        LinearLayout mRelativeLayout = (LinearLayout) view.findViewById(R.id.fragment_article);//布局的主容器
        mRelativeLayout.setOnTouchListener(this);//将主容器的监听交给本activity，本activity再交给mGestureDetector
        mRelativeLayout.setLongClickable(true);   //必需设置这为true 否则也监听不到手势

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        articleLayout = (VerticalScrollView) view.findViewById(R.id.article_layout);
        articleContentText = (TextView) view.findViewById(R.id.article_content);

        articleContentTitleText = (TextView) view.findViewById(R.id.article_content_title);
        articleAuthor = (TextView) view.findViewById(R.id.article_author);
        articleWc = (TextView) view.findViewById(R.id.article_wc);

        todayFab = (FloatingActionButton) view.findViewById(R.id.today_fab);
        randomFab = (FloatingActionButton) view.findViewById(R.id.random_fab);
        tomorrowFab = (FloatingActionButton) view.findViewById(R.id.tomorrow_fab);
        yesterdayFab = (FloatingActionButton) view.findViewById(R.id.yesterday_fab);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.gainData(ArticlePattern.TODAY, today);
            }
        });
        todayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pattern = ArticlePattern.TODAY;
                mPresenter.gainData(ArticlePattern.TODAY, today);
            }
        });
        randomFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pattern = ArticlePattern.RANDOMDAY;
                mPresenter.gainData(ArticlePattern.RANDOMDAY, today);
            }
        });
        yesterdayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pattern = ArticlePattern.YESTERDAY;
                mPresenter.gainData(ArticlePattern.YESTERDAY, yesterday);
            }
        });
        tomorrowFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tomorrow.compareTo(today) <= 0) {
                    pattern = ArticlePattern.TOMORROW;
                    mPresenter.gainData(ArticlePattern.TOMORROW, tomorrow);
                }
            }
        });


    }

    private void upload() {
        //本地缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        String articleString = prefs.getString("article", null);

        Date date = new Date();
        long date1 = prefs.getLong("date", 0);
        if (date.getTime() - date1 > 8 * 60 * 60 * 1000) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
            editor.putLong("date", date.getTime());
            editor.apply();
            swipeRefresh.setRefreshing(true);
            mPresenter.gainData(ArticlePattern.TODAY, today);
        } else {
            if (articleString != null) {
                Data data = Utility.handleArticleResponse(articleString);
                yesterday = data.article.date.prev;
                tomorrow = data.article.date.next;
                if (pattern == ArticlePattern.TODAY) {
                    today = data.article.date.curr;
                }
                showUIInfo(data);
            } else {
                articleLayout.setVisibility(View.INVISIBLE);
                mPresenter.gainData(ArticlePattern.TODAY, today);
            }
        }
    }

    @Override
    public void showUI(final Data data) {

        if (pattern == ArticleFragment.ArticlePattern.TODAY) {
            today = data.article.date.curr;
        }
        yesterday = data.article.date.prev;
        tomorrow = data.article.date.next;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                showUIInfo(data);
            }
        });

    }

    private void showUIInfo(Data data) {
        String text = data.article.articleContent.replaceAll("<p>", "        ");
        articleContentText.setText(text.replaceAll("</p>", "\n\n"));
        articleContentTitleText.setText(data.article.title);
        articleAuthor.setText(data.article.author);
        articleWc.setText("全文完  ，共" + data.article.wc + "字");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }
}

