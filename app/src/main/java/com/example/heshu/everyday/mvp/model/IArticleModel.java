package com.example.heshu.everyday.mvp.model;

import com.example.heshu.everyday.fragment.ArticleFragment;

/**
 * Created by heshu on 2018/5/10.
 */

public interface IArticleModel {
    void gainData(ArticleFragment.ArticlePattern type ,String date);
}
