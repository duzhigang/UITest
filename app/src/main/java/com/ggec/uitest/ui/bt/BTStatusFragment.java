package com.ggec.uitest.ui.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ggec.uitest.R;

import java.util.List;

/**
 * Created by ggec on 2018/10/15.
 * 测试手机当前连接的蓝牙设备
 */

public class BTStatusFragment extends Fragment {
    private static final String TAG = "BTStatusFrag";

    private TextView tvBTStatus;
    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bt_status, container, false);
        tvBTStatus = (TextView) view.findViewById(R.id.tv_bt_status);
        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        getConnectBt(adapter);

        return view;
    }

    Handler timerHandler = new Handler();
    // 每1s发送一次进度更新
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count++;
            if (count < 100) {
                Log.e(TAG,"发送次数：" + count);
                timerHandler.postDelayed(this, 1000);
            } else {
                Log.e(TAG,"发送超时");
            }
        }
    };

    @Override
    public void onStart() {
        Log.v(TAG,"onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.v(TAG,"onResume()");
        timerHandler.postDelayed(runnable, 1000);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.v(TAG,"onPause()");
        timerHandler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.v(TAG,"onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG,"onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.v(TAG,"onDetach()");
        super.onDetach();
    }

    // 检查已连接的蓝牙设备
    private void getConnectBt(BluetoothAdapter bluetoothAdapter) {
        int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);
        int flag = -1;
        // STATE_DISCONNECTED = 0,STATE_CONNECTING = 1,STATE_CONNECTED = 2,STATE_DISCONNECTING = 3
        if (a2dp == BluetoothProfile.STATE_CONNECTED) {
            flag = a2dp;
        } else if (headset == BluetoothProfile.STATE_CONNECTED) {
            flag = headset;
        } else if (health == BluetoothProfile.STATE_CONNECTED) {
            flag = health;
        }
        if (flag != -1) {
            bluetoothAdapter.getProfileProxy(getActivity(), new BluetoothProfile.ServiceListener() {
                @Override
                public void onServiceDisconnected(int profile) {
                    Log.e(TAG,"profile = " + profile);
                }

                @Override
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                    if (mDevices != null && mDevices.size() > 0) {
                        for (BluetoothDevice device : mDevices) {
                            Log.e(TAG,"连接成功的设备名字为：" + device.getName());
                            tvBTStatus.setText("已连上设备的蓝牙");
                            break;
                        }
                    } else {
                        tvBTStatus.setText("未连上设备的蓝牙");
                        Log.e(TAG,"没有连接任何蓝牙设备");
                    }
                }
            }, flag);
        }
    }
}
