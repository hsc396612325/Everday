package com.example.heshu.everyday.eyepetizer.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.example.heshu.everyday.R;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Created by heshu on 2018/10/28.
 */

public class EmptyControlVideo extends StandardGSYVideoPlayer {

    public EmptyControlVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EmptyControlVideo(Context context) {
        super(context);
    }

    public EmptyControlVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.eyepetizer_empty_control_video;
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);

        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void dismissVolumeDialog() {
        super.dismissVolumeDialog();
    }

    public static class  EmptyControlVideoCallBack implements VideoAllCallBack {

        @Override
        public void onPrepared(String url, Object... objects) {

        }

        @Override
        public void onClickStartIcon(String url, Object... objects) {

        }

        @Override
        public void onClickStartError(String url, Object... objects) {

        }

        @Override
        public void onClickStop(String url, Object... objects) {

        }

        @Override
        public void onClickStopFullscreen(String url, Object... objects) {

        }

        @Override
        public void onClickResume(String url, Object... objects) {

        }

        @Override
        public void onClickResumeFullscreen(String url, Object... objects) {

        }

        @Override
        public void onClickSeekbar(String url, Object... objects) {

        }

        @Override
        public void onClickSeekbarFullscreen(String url, Object... objects) {

        }

        @Override
        public void onAutoComplete(String url, Object... objects) {

        }

        @Override
        public void onEnterFullscreen(String url, Object... objects) {

        }

        @Override
        public void onQuitFullscreen(String url, Object... objects) {

        }

        @Override
        public void onQuitSmallWidget(String url, Object... objects) {

        }

        @Override
        public void onEnterSmallWidget(String url, Object... objects) {

        }

        @Override
        public void onTouchScreenSeekVolume(String url, Object... objects) {

        }

        @Override
        public void onTouchScreenSeekPosition(String url, Object... objects) {

        }

        @Override
        public void onTouchScreenSeekLight(String url, Object... objects) {

        }

        @Override
        public void onPlayError(String url, Object... objects) {

        }

        @Override
        public void onClickStartThumb(String url, Object... objects) {

        }

        @Override
        public void onClickBlank(String url, Object... objects) {

        }

        @Override
        public void onClickBlankFullscreen(String url, Object... objects) {

        }
    }
}
