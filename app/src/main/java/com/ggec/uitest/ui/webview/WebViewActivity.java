package com.ggec.uitest.ui.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/10/17.
 */

public class WebViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new WVTestFragment();
        ft.replace(R.id.webview_frame, fragment).commit();
    }
}
