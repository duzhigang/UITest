package com.ggec.uitest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/8/31.
 * 测试ListView相关类
 */

public class LVActivity extends FragmentActivity {
    private static final String TAG = "LVActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ExpandableLVFragment fragment = new ExpandableLVFragment();
        ft.replace(R.id.lv_frame, fragment).commit();
    }
}
