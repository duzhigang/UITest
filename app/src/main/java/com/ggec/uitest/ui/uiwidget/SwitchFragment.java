package com.ggec.uitest.ui.uiwidget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/8/31.
 * 测试自定义SwitchFragment控件
 */

public class SwitchFragment extends Fragment {
    private static final String TAG = "SwitchFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switch, container, false);
        Switch sw = view.findViewById(R.id.btn_switch);
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> Log.e(TAG,"开关状态发生变化,为：" + isChecked));
        return view;
    }
}
