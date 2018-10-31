package com.example.heshu.everyday.eyepetizer.View;

import android.widget.TextView;


/**
 * Created by heshu on 2018/10/31.
 */

public class TiaoZiUtil {
    private TextView tv;
    private TextView invisiableTv;
    private String s;
    private Long time = 700L;

    private int length;
    private int n = 0;
    private int nn = 0;

    public TiaoZiUtil(TextView tv, TextView invisiableTv, String s) {
        this.tv = tv;
        this.invisiableTv = invisiableTv;
        this.s = s;
    }

    public TiaoZiUtil(TextView tv, TextView invisiableTv, String s, Long time) {
        this.tv = tv;
        this.invisiableTv = invisiableTv;
        this.s = s;
        this.time = time;
    }

    private void startTv(final int n){
       new Thread(new Runnable() {
            @Override
            public void run() {
                //截取要填充的字符串
                String stv =  s.substring(0, n);
                String sitv = s.substring(n);

                tv.setText (stv) ;
                invisiableTv.setText(sitv);
//                tv.post(Runnable action){
//
//                }

                try {
                    Thread.sleep(time/length);//休息片刻
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                nn = n + 1;//n+1；多截取一个
                if (nn <= length) {//如果还有汉字，那么继续开启线程，相当于递归的感觉
                    startTv(nn);
                }
            }
        }).start();
    }

}
