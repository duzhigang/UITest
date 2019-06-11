package com.ggec.uitest.ui.activitymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/10/22.
 * activity四种加载模式：https://www.cnblogs.com/androidWuYou/p/5887807.html
 * 1. standard
 * A—>T—>B—>C—>T    ==> 不变
 * 2.singleTop
 * A—>T—>T    ==> A—>T
 * 如果T包含多个Fragment，如果启动T之前处于MFragment，则启动T后仍然处于MFragment页面，执行的流程如下：MonPause()—>TonPause—>TonNewIntent—>TonResume—>MonResume
 * A—>T—>B—>C—>T    ==> 不变
 * 3.singleTask
 * A—>T—>B—>C—>T    ==> A—>T
 * 4.singleInstance
 * A—>T—>B—>C—>T    ==> A—>B—>C—>T
 *
 * 在其他Activity页面第二次启动TActivity的流程：onPause(C)、onNewIntent(T)、onStart(T)、onResume(T)、onStop(C)
 * 如果是A—>T—>T则在上面的生命周期去掉onStart(T)
 */

public class TActivity extends FragmentActivity {
    private static final String TAG = "TActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().Object = " + this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t);
        Button btnStart = findViewById(R.id.btn_activity_t_start);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(TActivity.this, BActivity.class);
            startActivity(intent);
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        TOneFragment fragment = new TOneFragment();
        ft.addToBackStack(null);
        ft.replace(R.id.activity_t_frame, fragment).commit();
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

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG,"onNewIntent().");
        super.onNewIntent(intent);
    }
}
