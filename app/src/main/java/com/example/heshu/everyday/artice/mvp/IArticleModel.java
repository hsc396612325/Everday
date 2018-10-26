package com.example.heshu.everyday.artice.mvp;

import com.example.heshu.everyday.artice.ArticleFragment;

/**
 * Created by heshu on 2018/5/10.
 */

public interface IArticleModel {
    void gainData(ArticleFragment.ArticlePattern type ,String date);
}
