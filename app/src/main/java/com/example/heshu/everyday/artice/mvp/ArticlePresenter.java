package com.example.heshu.everyday.artice.mvp;

import com.example.heshu.everyday.artice.ArticleFragment;
import com.example.heshu.everyday.artice.gson.Data;


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
