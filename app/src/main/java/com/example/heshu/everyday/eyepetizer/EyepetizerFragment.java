package com.example.heshu.everyday.eyepetizer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heshu.everyday.R;

/**
 * Created by heshu on 2018/5/9.
 */

public class EyepetizerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eyepetizer_fragment_main,container,false);
        return  view;
    }
}
