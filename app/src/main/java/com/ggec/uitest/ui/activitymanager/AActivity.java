package com.ggec.uitest.ui.activitymanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/10/22.
 * A—>A，A采用默认的启动方式，则流程如下：onCreate、onStart、onResume、onPause、onCreate(新的对象)、onStart、onResume、onStop
 * A—>A，A采用FLAG_ACTIVITY_REORDER_TO_FRONT的启动方式，则流程如下：onCreate、onStart、onResume、onPause、onNewIntent、onResume
 */

public class AActivity extends FragmentActivity {
    private static final String TAG = "AActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().，Object = " + this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        TextView tvTitle = findViewById(R.id.tv_activity_a_title);
        tvTitle.setText("A Activity");
        Button btnStart = findViewById(R.id.btn_activity_a_start);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(AActivity.this, BActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Log.i(TAG,"第一次启动");
            timerHandler.postDelayed(runnable, 1000);
        });
    }

    private Handler timerHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent2 = new Intent(AActivity.this, BActivity.class);
//            intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            Log.i(TAG,"第二次启动");
        }
    };

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
        Log.e(TAG,"onNewIntent().");
        super.onNewIntent(intent);
    }
}
