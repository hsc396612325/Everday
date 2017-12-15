package com.example.heshu.everyday.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heshu.everyday.Data.Video;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.gson.zhihu.ZhihuDetails;
import com.example.heshu.everyday.util.HttpUtil;
import com.example.heshu.everyday.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by heshu on 2017/12/10.
 */

public class ZhihuDetailsActivity  extends BaseActivity{
    private String ID ;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CollapsingToolbarLayoutState state;
    private AppBarLayout mAppBar;
    private ImageView mImageView;
    private TextView mTextTitle;
    private WebView mWebView;
    private String StrHtml ;
    private Button returnButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_datails);
        ID = getIntent().getStringExtra("ZhihuId");
        initUI();
        requestDetails();
    }
    private void initUI() {
        returnButton = (Button)findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        mAppBar = (AppBarLayout)findViewById(R.id.app_bar);
        mImageView = (ImageView)findViewById(R.id.imageview);
        mTextTitle = (TextView)findViewById(R.id.text_title);
        mWebView = (WebView)findViewById(R.id.web_view);

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset == 0){
                    if(state != CollapsingToolbarLayoutState.EXPANDED ){
                        state = CollapsingToolbarLayoutState.EXPANDED; //修改状态为展开
                        mTextTitle.setVisibility(View.VISIBLE);
                        returnButton.setVisibility(View.INVISIBLE);

                    }
                }else if(Math.abs(verticalOffset)>=appBarLayout.getTotalScrollRange()){
                    if(state != CollapsingToolbarLayoutState.COLLAPSED){
                        state = CollapsingToolbarLayoutState.COLLAPSED;//标记状态为折叠
                        mTextTitle.setVisibility(View.INVISIBLE);
                        returnButton.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(state != CollapsingToolbarLayoutState.INTERNEDIATE){

                        state = CollapsingToolbarLayoutState.INTERNEDIATE;
                        mTextTitle.setVisibility(View.VISIBLE);
                        returnButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void requestDetails() {
        String DetailsUrl = "https://news-at.zhihu.com/api/4/news/" +ID;

        HttpUtil.sendOKHttpRequest(DetailsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ZhihuDetailsActivity.this,"获取消息失败",Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                final ZhihuDetails zhihuDetails = Utility.handleZhihuDetailsResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showUI(zhihuDetails);
                        Log.d("news",zhihuDetails.detailsTitle);
                    }
                });
            }
        });
    }

    private void showUI(ZhihuDetails zhihuDetails){

        StrHtml = zhihuDetails.body.replaceFirst("<div class=\\\"img-place-holder\\\"></div>","");

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadDataWithBaseURL("file:///android_asset/","<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />"+StrHtml,"text/html","utf_8",null);

        Glide.with(ZhihuDetailsActivity.this).load(zhihuDetails.detailsImage).into(mImageView);
        mTextTitle.setText(zhihuDetails.detailsTitle);
    }
    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
}
