package com.ggec.uitest.ui.nsd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ggec.uitest.R;

/**
 * NSD服务介绍 https://www.jianshu.com/p/2b8a0156bdc0
 * NSD服务Google官方文档：https://developer.android.com/training/connect-devices-wirelessly/nsd.html#java
 * NSD（NsdManager）是Android SDK中自带的类库，可以集成直接使用,采用组播方式。使用 NSD服务需要minSdkVersion >16
 * */
public class NsdActivity extends FragmentActivity {
    private static final String TAG = "NsdActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsd);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart().");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume().");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause().");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop().");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy().");
    }

    private void initView() {
        findViewById(R.id.btn_act_nsd_client).setOnClickListener(v -> {
            Intent intent = new Intent(NsdActivity.this, NsdClientActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_act_nsd_server).setOnClickListener(v -> {
            Intent intent = new Intent(NsdActivity.this, NsdServerActivity.class);
            startActivity(intent);
        });
    }
}
