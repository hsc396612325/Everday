package com.example.heshu.everyday.mvp.presenter;

import com.example.heshu.everyday.fragment.ArticleFragment;
import com.example.heshu.everyday.gson.article.Data;
import com.example.heshu.everyday.mvp.model.ArticleModel;
import com.example.heshu.everyday.mvp.model.IArticleModel;
import com.example.heshu.everyday.view.IArticleFragment;

/**
 * Created by heshu on 2018/5/10.
 */

public class ArticlePresenter {
    IArticleModel mModel;
    IArticleFragment mView;

    public ArticlePresenter(IArticleFragment view){
        mModel = new ArticleModel(this);
        mView = view;
    }

    public void gainData(ArticleFragment.ArticlePattern articlePattern,String date){
        mModel.gainData(articlePattern,date);
    }

    public void showUI(Data data){
        mView.showUI(data);
    }
}
