package com.ggec.uitest.ui.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.ggec.uitest.R;

import static android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION;

public class WifiStatusChangeActivity extends FragmentActivity {
    private static final String TAG = "TcpStatusActivity";

    private TextView tvWifiStatus;
    private TextView tvWifiSSID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_status_change);
        tvWifiStatus = findViewById(R.id.wifi_status_change_activity_wifi_status);
        tvWifiSSID = findViewById(R.id.wifi_status_change_activity_wifi_ssid);

        WifiChangeReceiver wifiChangeReceiver = new WifiChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiChangeReceiver, filter);
    }

    private class WifiChangeReceiver extends BroadcastReceiver {
        //检查当前手机是否自动切换到切换前的网络，如果切换成功，则显示"继续"按钮，反之不显示，默认不显示
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//                LogUtils.v("wifi连接状态改变");
                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    // 获取联网状态的NetWorkInfo对象
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    //获取的State对象则代表着连接成功与否等状态
                    NetworkInfo.State state = networkInfo.getState();
                    Log.i(TAG,"wifi连接状态：" + state);
                    String statusStr = "Wifi连接状态：".concat(state.name());
                    tvWifiStatus.setText(statusStr);
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    if (wifiManager != null) {
                        String ssid = wifiManager.getConnectionInfo().getSSID();
                        String ssidStr = "Wifi SSID：".concat(ssid);
                        tvWifiSSID.setText(ssidStr);
                        Log.i(TAG,"wifi ssid = ：" + ssid);
                    } else {
                        Log.e(TAG,"wifiManager为Null");
                    }

                }
            }
        }
    }
}
