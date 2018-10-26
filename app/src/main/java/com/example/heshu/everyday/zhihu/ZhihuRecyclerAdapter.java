package com.example.heshu.everyday.zhihu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshu.everyday.R;
import com.example.heshu.everyday.zhihu.gson.Zhihu;

import java.util.List;

/**
 * Created by heshu on 2017/12/9.
 */

public class ZhihuRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Zhihu> mZhihuList;
    //建立枚举 3个item 类型
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2,
        ITEM3
    }

    public ZhihuRecyclerAdapter(Context context, List<Zhihu> zhihuList){
        mContext = context;
        mZhihuList = zhihuList;
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE.ITEM2.ordinal()){
            return new Item2ViewHolder(mLayoutInflater.inflate(R.layout.fragment_zhihu_item, parent, false));
        }else if(viewType == ITEM_TYPE.ITEM3.ordinal()){
            return new Item3ViewHolder(mLayoutInflater.inflate(R.layout.fragment_zhihu_item2, parent, false));
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Item2ViewHolder) {
            ((Item2ViewHolder) holder).zhihuTitle.setText(mZhihuList.get(position).getDate());
        }else  if (holder instanceof Item3ViewHolder){
            ((Item3ViewHolder) holder).ZhihuText.setText(mZhihuList.get(position).getTitle());
            Glide.with(mContext).load(mZhihuList.get(position)
                    .getImagesUrl()).into(((Item3ViewHolder) holder).ZHihuImage);

            ((Item3ViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener(){  //点击事件
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,ZhihuDetailsActivity.class);
                    intent.putExtra("ZhihuId",mZhihuList.get(position).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
        if(mZhihuList.get(position).getType() == Zhihu.ITEM_TYPE_Z.ITEM2){
            return ITEM_TYPE.ITEM2.ordinal();
        }else{
            return ITEM_TYPE.ITEM3.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return mZhihuList.size();
    }

    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder{
        TextView zhihuTitle;
        public Item2ViewHolder(View itemView) {
            super(itemView);
            zhihuTitle = (TextView) itemView.findViewById(R.id.zhihu_title);
        }
    }

    public static class Item3ViewHolder extends RecyclerView.ViewHolder{
        TextView ZhihuText;
        ImageView ZHihuImage;
        CardView cardView;

        public Item3ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            ZhihuText = (TextView)itemView.findViewById(R.id.zhihu_text);
            ZHihuImage = (ImageView) itemView.findViewById(R.id.zhihu_image);
        }
    }
}
