package com.example.heshu.everyday.Data;

import java.io.Serializable;

/**
 * Created by heshu on 2017/11/30.
 */

public class Video implements Serializable{

    String VideoImageUrl;
    String VideoText;
    String VideoUrl;
    String videoDetails;
    String videoWeb;

    public String getVideoWeb() {
        return videoWeb;
    }

    public void setVideoWeb(String videoWeb) {
        this.videoWeb = videoWeb;
    }



    public String getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(String videoDetails) {
        this.videoDetails = videoDetails;
    }


    public String getVideoImageUrl() {
        return VideoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        VideoImageUrl = videoImageUrl;
    }

    public String getVideoText() {
        return VideoText;
    }

    public void setVideoText(String videoText) {
        VideoText = videoText;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }


}
