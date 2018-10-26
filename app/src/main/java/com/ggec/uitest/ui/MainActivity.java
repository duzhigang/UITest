package com.ggec.uitest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ggec.uitest.R;
import com.ggec.uitest.ui.activitymanager.AActivity;

public class MainActivity extends FragmentActivity {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate().");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStart = findViewById(R.id.btn_main_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AActivity.class);
                startActivity(intent);
            }
        });

    }

/*   // Android退出所有Activity最优雅的方式：https://www.cnblogs.com/caobotao/p/5127645.html
    // 设置BaseActivity的启动模式为singleTask
    android:launchMode="singleTask"

    // 声明一个静态常量，用作退出BaseActivity的Tag
    public static final String EXIST = "exist";

    // 退出APP
    @Override
    protected void onNewIntent(Intent intent) {
        Log.v(TAG,"onNewIntent().");
        super.onNewIntent(intent);
        if (intent != null) {//判断其他Activity启动本Activity时传递来的intent是否为空
            //获取intent中对应Tag的布尔值
            boolean isExist = intent.getBooleanExtra(EXIST, false);
            Log.e(TAG,"isExist = " + isExist);
            //如果为真则退出本Activity
            if (isExist) {
                this.finish();
            }
        }
    }
    // 在需要退出应用的Activity中启动MainActivity
    Intent intent = new Intent(this, MainActivity.class);
    //传递退出所有Activity的Tag对应的布尔值为true
    intent.putExtra(MainActivity.EXIST, true);
    //启动MainActivity
    startActivity(intent);
    */

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
}
