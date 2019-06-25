package com.ggec.uitest.ui.measureScreen;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Button;

import com.ggec.uitest.R;

/*
* 这个Activity用于测试当前手机屏幕的宽度和高度
* fun1、fun2、fun3用于测量应用程序的显示区域
* fun4、fun5用于测量实际显示区域(包含系统装饰的内容的显示部分，故大于或等于应用程序的显示区域)
* */
public class MeasureActivity extends FragmentActivity {
    private static final String TAG = "MeasureActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        Button btnStart = findViewById(R.id.btn_act_measure_start);
        btnStart.setOnClickListener(v-> {
            Log.i(TAG,"开始测量屏幕的宽度和高度");
            fun1();
            fun2();
            fun3();
            fun4();
            fun5();
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

    private void fun1() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int widthPixels = point.x;
        int heightPixels = point.y;
        Log.i(TAG, "fun1(),widthPixels = " + widthPixels + ",heightPixels = " + heightPixels);
    }

    // 与fun3的源码实现是一样的
    private void fun2() {
        Rect outSize = new Rect();
        getWindowManager().getDefaultDisplay().getRectSize(outSize);
        int left = outSize.left;
        int top = outSize.top;
        int right = outSize.right;
        int bottom = outSize.bottom;
        Log.i(TAG, "fun2(),left = " + left + ",top = " + top + ",right = " + right + ",bottom = " + bottom);
    }

    private void fun3() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        Log.i(TAG, "fun3(),widthPixels = " + widthPixels + ",heightPixels = " + heightPixels);
    }

    private void fun4() {
        Point outSize = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(outSize);
        int widthPixels = outSize.x;
        int heightPixels = outSize.y;
        Log.i(TAG, "fun4(),widthPixels = " + widthPixels + ",heightPixels = " + heightPixels);
    }

    private void fun5() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        Log.w(TAG, "fun5(),widthPixel = " + widthPixel + ",heightPixel = " + heightPixel);
    }
}
