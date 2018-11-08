package com.ggec.uitest.ui.uiwidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/8/31.
 * 测试一些自定义的UI控件
 */

public class UIActivity extends FragmentActivity {
    private static final String TAG = "UIActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SwitchFragment fragment = new SwitchFragment();
        ft.replace(R.id.ui_frame, fragment).commit();
    }
}
