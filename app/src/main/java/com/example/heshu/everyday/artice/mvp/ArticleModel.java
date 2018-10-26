package com.example.heshu.everyday.artice.mvp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.heshu.everyday.artice.ArticleFragment;
import com.example.heshu.everyday.artice.gson.Data;
import com.example.heshu.everyday.common.util.App;
import com.example.heshu.everyday.common.util.HttpUtil;
import com.example.heshu.everyday.common.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by heshu on 2018/5/10.
 */

public class ArticleModel implements IArticleModel {

    ArticlePresenter mPresenter;

    public ArticleModel(ArticlePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void gainData(final ArticleFragment.ArticlePattern type,String date) {
        final String ArticleURL = "https://interface.meiriyiwen.com/article/";
        String uPURL = "";
        if (type == ArticleFragment.ArticlePattern.TODAY) {
            uPURL = "today?dev=1";
        } else if (type == ArticleFragment.ArticlePattern.YESTERDAY) {
            uPURL = "day?dev=1&date=" + date;
        } else if (type == ArticleFragment.ArticlePattern.TOMORROW) {
            uPURL = "day?dev=1&date=" + date;
        } else if (type == ArticleFragment.ArticlePattern.RANDOMDAY) {
            uPURL = "random?dev=1";
        }

        Log.d("URL", ArticleURL + uPURL);
        HttpUtil.sendOKHttpRequest(ArticleURL + uPURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] b = response.body().bytes();
                final String responseText = new String(b, "utf-8");
                Log.d("JSON", responseText);
                final Data data = Utility.handleArticleResponse(responseText);

                if(type == ArticleFragment.ArticlePattern.TODAY) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
                    editor.putString("article", responseText);
                    editor.apply();
                }
                //更新界面
                mPresenter.showUI(data);
                Log.d("JSON", responseText);

            }
        });
    }
}
