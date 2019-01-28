package com.ggec.uitest.ui.wifi;

import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/9/14.
 * 测试手机自动切换网络，目前连开放网络都无法自动切换
 */

public class WifiManagerActivity extends FragmentActivity {
    private static final String TAG = "WifiActivity";

    private WifiAdmin wifiAdmin;
    private String currentSsid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_manager);
        wifiAdmin = new WifiAdmin(this);
        wifiAdmin.openWifi();
        wifiAdmin.startScan();
        currentSsid = wifiAdmin.getSSID();

        final TextView tvSsid = findViewById(R.id.tv_wifi_activity_ssid);
        tvSsid.setText(currentSsid);

        Button btnRefresh = findViewById(R.id.btn_wifi_activity_refresh);
        btnRefresh.setOnClickListener(v -> {
            currentSsid = wifiAdmin.getSSID();
            tvSsid.setText(currentSsid);
        });

        Button btnChange = findViewById(R.id.btn_wifi_activity_change);
        btnChange.setOnClickListener(v -> {
            String ssid = "\"" + "minote3" + "\"";
            String pwd = "";
            connect(ssid, pwd);
        });
    }

    private void connect(String ssid, String pwd) {
        WifiConfiguration tempConfig = wifiAdmin.IsExists(ssid);
        if(tempConfig == null) {
            WifiConfiguration wifiConfiguration = wifiAdmin.CreateWifiInfo(ssid, pwd, 1);
            Boolean flag = wifiAdmin.addNetwork(wifiConfiguration);
            Log.v(TAG,"该热点以前没有连接过,需要新建一个对象，flag = " + flag + "wifiConfiguration = " + wifiConfiguration.toString());
        } else {
            wifiAdmin.connectConfigurationById(tempConfig.networkId);
            Log.v(TAG,"该热点以前连接过，直接让手机重新连接该热点");
        }
    }

/*    String str = "\"" + "TEST" + "\"";
    // 网络连接列表
    List<WifiConfiguration> mWifiConfiguration = wifiAdmin.getConfiguration();
                for (int i = 0; i < mWifiConfiguration.size(); i++) {
        if (mWifiConfiguration.get(i).SSID.equals(lastSSID)) {
            Log.v("MainActivity","该热点已经连接过");
            int networkId = mWifiConfiguration.get(i).networkId;
            wifiAdmin.connectConfigurationById(networkId);
//                        wifiAdmin.addNetwork(lastWifiConfiguration);
            break;
        }
    }*/
}
