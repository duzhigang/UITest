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

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ggec on 2018/10/22.
 */

public class BActivity extends FragmentActivity {
    private static final String TAG = "BActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().Object = " + this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        TextView tvTitle = findViewById(R.id.tv_activity_a_title);
        tvTitle.setText("B Activity");
        Button btnStart = findViewById(R.id.btn_activity_a_start);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(BActivity.this, CActivity.class);
            startActivity(intent);
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Log.i(TAG,"第一次结束");
            }
        },10000);

/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Log.i(TAG,"第二次结束");
            }
        },10200);*/

/*        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //To-do
                finish();
                Log.i(TAG,"结束");
            }
        },10000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //To-do
                finish();
                Log.i(TAG,"结束");
            }
        },10200);*/
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
        Log.e(TAG,"onNewIntent().");
        super.onNewIntent(intent);
    }
}
