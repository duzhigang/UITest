package com.ggec.uitest.ui.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;
import com.ggec.uitest.application.MyApplication;

import java.util.List;

import static android.provider.Settings.ACTION_BLUETOOTH_SETTINGS;

/**
 * Created by ggec on 2018/10/15.
 * 测试手机当前连接的蓝牙设备，需要的权限：BLUETOOTH和BLUETOOTH_ADMIN
 */

public class BTStatusFragment extends Fragment {
    private static final String TAG = "BTStatusFrag";

    private TextView tvBTStatus;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bt_status, container, false);
        tvBTStatus = view.findViewById(R.id.tv_bt_status);
        tvBTStatus.setText("未连上设备的蓝牙");
        Button btnSet = view.findViewById(R.id.bt_bt_status);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Intent intent = new Intent();
//                intent.setClassName("com.android.settings","com.android.settings.bluetooth.BluetoothSettings");
//                intent.setClassName("com.android.settings","com.android.settings.Settings$BluetoothSettingsActivity");
                Intent intent = new Intent(ACTION_BLUETOOTH_SETTINGS);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        Log.v(TAG,"onStart()");
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    public void onResume() {
        Log.v(TAG,"onResume()");
        super.onResume();
        isConnectedBtDevice();
    }

    @Override
    public void onPause() {
        Log.v(TAG,"onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.v(TAG,"onStop()");
        super.onStop();
        mContext.unregisterReceiver(stateChangeReceiver);
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
    private void isConnectedBtDevice() {
        BluetoothAdapter bluetoothAdapter = null;
        BluetoothManager bluetoothManager = (BluetoothManager) MyApplication.getMyApplication().
                getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (bluetoothAdapter == null) {
            Log.w(TAG,"手机没有蓝牙模块");
            return;
        }
        // 可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
        int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        // 蓝牙头戴式耳机，支持语音输入输出
        int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        // 蓝牙穿戴式设备
        int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);
        int flag = -1;  // error
        //查看是否蓝牙是否连接到三种设备的一种，以此来判断是否处于连接状态还是打开并没有连接的状态
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
                    Log.i(TAG,"onServiceDisconnected：断开蓝牙" + profile);
                }

                @Override
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                    if (mDevices != null && mDevices.size() > 0) {
                        String btName = mDevices.get(0).getName();
                        Log.i(TAG,"onServiceConnected，连上蓝牙：" + btName);
                        tvBTStatus.setText("连上蓝牙：" + btName);
                    } else {
                        tvBTStatus.setText("未连上设备的蓝牙");
                        Log.i(TAG,"onServiceConnected,没有连接任何蓝牙设备");
                    }
                }
            }, flag);
        }
    }

    private void registerBroadcastReceiver() {
        //注册监听
        IntentFilter btFilter = new IntentFilter();
        btFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        btFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        btFilter.addAction(BluetoothDevice.ACTION_STATE_CHANGED);
        mContext.registerReceiver(stateChangeReceiver, btFilter);
    }

    private BroadcastReceiver stateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            BluetoothDevice device = null;
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String btName = device.getName();
                Log.i(TAG,"连上蓝牙：" + btName);
                tvBTStatus.setText("连上蓝牙：" + btName);
                //连接上了
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String btName = device.getName();
                //蓝牙连接被切断
                Log.i(TAG,"断开蓝牙：" + btName);
                tvBTStatus.setText("未连上设备的蓝牙");
            }
        }
    };
}
