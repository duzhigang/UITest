package com.ggec.uitest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switch, container, false);
        Switch sw = (Switch) view.findViewById(R.id.btn_switch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG,"开关状态发生变化,为：" + isChecked);
            }
        });
        return view;
    }
}
