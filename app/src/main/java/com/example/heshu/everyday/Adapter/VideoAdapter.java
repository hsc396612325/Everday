package com.example.heshu.everyday.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshu.everyday.Data.Video;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.activity.OpenEyesActivity;
import com.example.heshu.everyday.activity.PlayActivity;

import java.util.List;

/**
 * Created by heshu on 2017/11/30.
 */

public class VideoAdapter extends RecyclerView.Adapter <VideoAdapter.ViewHolder> {
    private Context mContext;

    private List<Video> mVideoList;


    static  class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView videoImage;
        TextView videoText;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            videoImage = (ImageView)view.findViewById(R.id.video_image);
            videoText = (TextView)view.findViewById(R.id.video_text);
        }
    }

    public VideoAdapter(List<Video> videoList){
        mVideoList = videoList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.activity_open_eyes_video_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Video video = mVideoList.get(position);
        holder.videoText.setText(video.getVideoText());
        Glide.with(mContext).load(video.getVideoImageUrl()).into(holder.videoImage);

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PlayActivity.class);
                intent.putExtra("Video_data",video);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }
}
