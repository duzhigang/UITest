package com.ggec.uitest.ui.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ggec.uitest.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/*
* 用于测试EventBus的使用
* 1. EventBus的四种线程模型（ThreadMode）：
* (1). POSTING（默认）：发布事件和接收事件在同一个线程,此事件处理函数中尽量避免执行耗时操作，因为它会阻塞事件的传递，甚至有可能会引起应用程序无响应（ANR）。
* (2). MAIN：事件的处理会在UI线程中执行。事件处理时间不能太长，长了会ANR的。
* (3). BACKGROUND：如果事件是在UI线程中发布出来的，那么该事件处理函数就会在新的线程中运行，如果事件本来就是子线程中发布出来的，那么该事件处理函数直接在发布事件的线程中执行。在此事件处理函数中禁止进行UI更新操作。
* (4). ASYNC：无论事件在哪个线程发布，该事件处理函数都会在新建的子线程中执行，此事件处理函数中禁止进行UI更新操作。
* 2. 粘性事件：指的就是事件发布之后再订阅该事件，仍然可以收到该事件，普通事件是先注册再绑定；如果是先发布后订阅，针对同一个粘性事件，如果多次发送，订阅的过程中只能收到最后一次的事件；如果是先订阅后发布与普通事件效果一样
* 3. 普通的事件类似于广播，如果未注册/解注册，事件发布后，再注册，则无法收到该事件。
* 4. 发送时发送的是这个类的实例，接收时参数就是这个类实例。当发过来一个消息的时候，EventBus通过判断哪个函数传进去的参数是这个类的实例，则调用该函数，如果有两个是，则两个都会被调用。
* */
public class EventBusActivity extends FragmentActivity {
    private static final String TAG = "EventBusActivity";
    private ProgressBar pbProgress;
    private int time = 0;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        pbProgress = findViewById(R.id.pb_act_event_bus);
        Button btnStartDownload = findViewById(R.id.btn_act_event_bus_start_download);
        btnStartDownload.setOnClickListener(v -> {
            updateProgress();
        });

        Button btnSendStickyMsg = findViewById(R.id.btn_act_event_bus_post_sticky_msg);
        btnSendStickyMsg.setOnClickListener(v -> {
            new Thread(() -> {
                while (count++ < 5) {
                    try {
                        Thread.sleep(1000);
                        EventBus.getDefault().post(new StrEvent("发送Normal消息"));
                        EventBus.getDefault().postSticky(new StrEvent("发送Sticky消息" + count));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });

        Button btnStartEBSecondAct = findViewById(R.id.btn_act_event_bus_start_eb_second_act);
        btnStartEBSecondAct.setOnClickListener(v -> {
            Intent intent = new Intent(EventBusActivity.this, EBSecondActivity.class);
            startActivity(intent);
        });
        // EventBus扫描当前类，将onEvent以键值对的形式进行存储，键为StrEvent.class ，值为包含该方法的对象
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ProgressEvent event) {
        int progress = event.getProgress();
        Log.i(TAG,"接收的进度为：" + progress);
        pbProgress.setProgress(progress);
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
        EventBus.getDefault().unregister(this);
        Log.v(TAG,"onDestroy().");
    }

    private void updateProgress() {
        new Thread(() -> {
            while (time < 100) {
                time += 5;
                EventBus.getDefault().post(new ProgressEvent(time));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
