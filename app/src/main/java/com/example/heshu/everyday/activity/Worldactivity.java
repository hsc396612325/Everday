package com.example.heshu.everyday.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.heshu.everyday.R;
import com.example.heshu.everyday.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Worldactivity extends BaseActivity {
    private ScrollView worldLayout;
    public SwipeRefreshLayout swipeRefresh;
    private VideoView videoView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);
        initUI();
    }

    private void initUI() {
        worldLayout = (ScrollView)findViewById(R.id.world_layout);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        videoView = (VideoView)this.findViewById(R.id.videoView );
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestJson();
            }
        });
        String videoUrl2 = "http://baobab.kaiyanapp.com/api/v1/playUrl?vid=61072&editionType=default&source=qcloud";
        Uri uri = Uri.parse( videoUrl2 );
        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());

        //设置视频路径
        videoView.setVideoURI(uri);

        //开始播放视频
        videoView.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

         @Override
            public void onCompletion(MediaPlayer mp) {
            Toast.makeText( Worldactivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
     }



    public void requestJson(){
        String JsonUrl ="http://is.snssdk.com/api/news/feed/v51/?category=news_hot&refer=1&count=20&" +
                "min_behot_time=1491981025&last_refresh_sub_entrance_interval=1491981165&loc_mode=&" +
                "loc_time=1491981000&latitude=&longitude=&city=&tt_from=pull&lac=&cid=&cp=&iid=0123456789" +
                "&device_id=12345678952&ac=wifi&channel=&aid=&app_name=&version_code=&version_name=&" +
                "device_platform=&ab_version=&ab_client=&ab_group=&ab_feature=&abflag=3&ssmix=a&" +
                "device_type=&device_brand=&language=zh&os_api=&os_version=&openudid=1b8d5bf69dc4a561&" +
                "manifest_version_code=&resolution=&dpi=&update_version_code=&_rticket= ";

        HttpUtil.sendOKHttpRequest(JsonUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Worldactivity.this,"数据获取失败",Toast.LENGTH_SHORT);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.d("World",responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Worldactivity.this,"数据获取失败",Toast.LENGTH_SHORT);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }
}
