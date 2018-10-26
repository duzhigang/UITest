package com.ggec.uitest.ui.activitymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;
import com.ggec.uitest.ui.MainActivity;

/**
 * Created by ggec on 2018/10/22.
 */

public class CActivity extends FragmentActivity {
    private static final String TAG = "CActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().Object = " + this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        TextView tvTitle = findViewById(R.id.tv_activity_a_title);
        tvTitle.setText("C Activity");
        Button btnStart = findViewById(R.id.btn_activity_a_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CActivity.this, TActivity.class);
                startActivity(intent);
            }
        });
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
