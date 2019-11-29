package com.ggec.uitest.ui.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.ggec.uitest.R;

public class BindingActivity extends FragmentActivity {
    private static final String TAG = "BindingActivity";

    private LocalService mService;
    private boolean mBound = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        Button btnStart = findViewById(R.id.btn_act_binding_start);
        btnStart.setOnClickListener(v -> {
            if (mBound) {
                int num = mService.getRandomNumber();
                Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart().");
        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        // 与unbindService都是非阻塞的方法
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume().");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause().");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop().");
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
//            Intent intent = new Intent(this, LocalService.class);
//            stopService(intent);
            mBound = false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy().");
    }

    // Defines callbacks for service binding, passed to bindService()
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.v(TAG, "onServiceDisconnected");
            mBound = false;
        }
    };
}
