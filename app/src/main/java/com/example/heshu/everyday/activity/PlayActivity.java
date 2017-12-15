package com.example.heshu.everyday.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.heshu.everyday.Data.Video;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.util.DensityUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;


public class PlayActivity extends BaseActivity {
    private CollapsingToolbarLayoutState state;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ButtonBarLayout playButton;
    private android.support.design.widget.FloatingActionButton fab;
    private android.support.design.widget.FloatingActionButton pauseFab;
    private FloatingActionButton escFab;
    private FloatingActionButton skipFab;
    private AppBarLayout app_bar;
    private VideoView videoView;
    private TextView textView;
    private ImageView imageView;
    private Video video;
    private boolean playFlag = false;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        video=(Video) getIntent().getSerializableExtra("Video_data");
        initUI();

    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        playButton = (ButtonBarLayout) findViewById(R.id.playButton);
        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        pauseFab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.pause_fab);
        escFab = (FloatingActionButton) findViewById(R.id.esc_fab);
        skipFab = (FloatingActionButton) findViewById(R.id.skip_fab);
        app_bar=(AppBarLayout)findViewById(R.id.app_bar);
        videoView = (VideoView)findViewById(R.id.video_view);
        textView = (TextView)findViewById(R.id.play_text);
        imageView = (ImageView)findViewById(R.id.imageview);

        returnButton = (Button)findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView.setText(video.getVideoDetails());

        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playFlag == false) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setVisibility(View.INVISIBLE);
                    pauseFab.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    videoView.setVisibility(View.VISIBLE);

                    playFlag = true;
                    Uri uri = Uri.parse(video.getVideoUrl());
                    videoView.setVideoURI(uri);
                    videoView.start();

                    Log.d("Url", video.getVideoUrl());
                }else {
                    fab.setVisibility(View.INVISIBLE);
                    pauseFab.setVisibility(View.VISIBLE);
                    videoView.start();
                }
            }
        });

        pauseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.VISIBLE);
                pauseFab.setVisibility(View.INVISIBLE);
                videoView.pause();
            }
        });
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        collapsingToolbarLayout.setTitle(" ");//设置title为EXPANDED

                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        collapsingToolbarLayout.setTitle(video.getVideoText());//设置title不显示
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        fab.setVisibility(View.VISIBLE);
                        pauseFab.setVisibility(View.INVISIBLE);
                        videoView.pause();

                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if(state == CollapsingToolbarLayoutState.COLLAPSED){
                            playButton.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        collapsingToolbarLayout.setTitle(" ");//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间


                    }
                }
            }
        });



        skipFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayActivity.this,WebActivity.class);
                intent.putExtra("Video_data",video);
                startActivity(intent);
            }
        });
    }
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( PlayActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
}
