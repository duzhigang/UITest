package com.ggec.uitest.ui.startotherapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ggec.uitest.R;

/*
* 这个例子用于测试在APP启动其他应用，包括启动系统应用以及用户安装应用，并且启动包括是在本应用中启动
* 还是另外启动新的进程
* */

public class StartOtherAppActivity extends FragmentActivity {
    private static final String TAG = "StartOtherAppActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_other_app);
        Button btnStartWifi = findViewById(R.id.btn_start_other_app_wifi);
        btnStartWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐式Intent,这种方式容易被其他应用劫持
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
/*                Intent intent = new Intent();
                // SDK小于11
//                intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                // SDK大于等于11
                intent.setClassName("com.android.settings", "com.android.settings.Settings$WifiSettingsActivity");*/
                // 不加下面这句话会导致在本身应用中启动新的APP
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.i(TAG,"启动Wifi设置页面");
            }
        });

        Button btnStartBrowser = findViewById(R.id.btn_start_other_app_browser);
        btnStartBrowser.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.baidu.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            Log.i(TAG,"启动系统浏览器");
        });

        Button btnStartQQ = findViewById(R.id.btn_start_other_app_qq);
        btnStartQQ.setOnClickListener(v -> {
            String pkgName = "com.netease.cloudmusic";
            if (checkPackInfo(pkgName)) {
                Log.i(TAG,"启动QQ音乐");
            } else {
                Log.i(TAG,"不存在该应用：" + pkgName);
            }
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName comp = new ComponentName("com.tencent.qqmusic", "com.tencent.qqmusic.activity.AppStarterActivity");
            intent.setComponent(comp);
//            intent.setClassName("com.tencent.qqmusic", "com.tencent.qqmusic.activity.AppStarterActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        Button btnStartWY = findViewById(R.id.btn_start_other_app_wy);
        btnStartWY.setOnClickListener(v -> {
            String pkgName = "com.netease.cloudmusic";
            if (checkPackInfo(pkgName)) {
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName comp = new ComponentName(pkgName, "com.netease.cloudmusic.activity.LoadingActivity");
                intent.setComponent(comp);
                Log.i(TAG,"启动网易云音乐");
            } else {
                Log.i(TAG,"不存在该应用：" + pkgName);
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

    /**
     * 检查包是否存在
     *
     * @param packname 应用包名
     * @return boolean true表示手机中存在该应用
     */
    private boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }
}
