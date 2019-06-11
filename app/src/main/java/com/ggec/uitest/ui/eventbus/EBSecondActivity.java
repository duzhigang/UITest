package com.ggec.uitest.ui.eventbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.ggec.uitest.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EBSecondActivity extends FragmentActivity {
    private static final String TAG = "EBSecondActivity";
    private TextView tvRec;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eb_second);
        tvRec = findViewById(R.id.tv_act_eb_second_rec);
        // 注册
        EventBus.getDefault().register(EBSecondActivity.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ProgressEvent event) {
        int progress = event.getProgress();
        Log.i(TAG,"接收的进度为：" + progress);
        tvRec.setText(String.valueOf(progress));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(StrEvent event) {
        String msg = event.getMsg();
        Log.i(TAG,"接收普通消息为：" + msg);
        tvRec.setText(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventStickyMainThread(StrEvent event) {
        String msg = event.getMsg();
        Log.i(TAG,"接收粘性消息为：" + msg);
        tvRec.setText(msg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart().");
//        EventBus.getDefault().register(EBSecondActivity.this);
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
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解注册
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
        Log.v(TAG,"onDestroy().");
    }
}
