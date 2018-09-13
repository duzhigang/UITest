package com.ggec.uitest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/9/12.
 */

public class TwoLifeActivity extends FragmentActivity {
    private static final String TAG = "TwoLifeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_life);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        OneLifeFragment fragment = new OneLifeFragment();
        ft.replace(R.id.two_life_activity_frame, fragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy()");
    }
}
