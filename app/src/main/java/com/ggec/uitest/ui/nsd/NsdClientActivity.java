package com.ggec.uitest.ui.nsd;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.ggec.uitest.R;

import java.util.ArrayList;

public class NsdClientActivity extends FragmentActivity implements NsdClient.NSDServiceInterface {
    private static final String TAG = "NsdClientActivity";

    private static final int NSD_DEVICE_ADD = 0x01;
    private static final int NSD_DEVICE_LOST = 0x02;

    private ListView lvResults;
    private ArrayAdapter<String> adapter;

    private ArrayList<String> availableDevices =new ArrayList<>();
    private NsdClient nsdClient = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsd_client);
        initView();

        nsdClient = new NsdClient(this);
        nsdClient.setNsdServiceInterface(this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String serviceName = (String) msg.obj;
            if (msg.what == NSD_DEVICE_ADD) {
                if (!availableDevices.contains(serviceName)) {
                    availableDevices.add(serviceName);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } else if (msg.what == NSD_DEVICE_LOST){
                if (availableDevices.contains(serviceName)) {
                    availableDevices.remove(serviceName);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart().");
    }

    @Override
    protected void onResume() {
        super.onResume();
        nsdClient.startServiceDiscover();
        Log.v(TAG,"onResume().");
    }

    @Override
    protected void onPause() {
        super.onPause();
        nsdClient.stopServiceDiscover();
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

    private void initView() {
        Button btnStartScan = findViewById(R.id.btn_act_nsd_client_start_scan);
        btnStartScan.setOnClickListener(v -> {
            nsdClient.startServiceDiscover();
        });

        lvResults = findViewById(R.id.lv_act_nsd_client_results);
        adapter = new ArrayAdapter<String>(this, R.layout.one_item_left, R.id.tv_one_item_left_name, availableDevices);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onNsdServiceResolved(String serviceName) {
        handler.obtainMessage(NSD_DEVICE_ADD, serviceName).sendToTarget();
    }

    @Override
    public void onNsdServiceLost(String serviceName) {
        handler.obtainMessage(NSD_DEVICE_LOST, serviceName).sendToTarget();
    }
}
