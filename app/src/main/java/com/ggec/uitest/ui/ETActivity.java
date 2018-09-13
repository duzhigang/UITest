package com.ggec.uitest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/8/28.
 * 测试EditText相关类
 */

public class ETActivity extends FragmentActivity{
    private static final String TAG = "ETActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_et);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ETFragment fragment = new ETFragment();
        ft.replace(R.id.et_frame, fragment).commit();
    }
}
