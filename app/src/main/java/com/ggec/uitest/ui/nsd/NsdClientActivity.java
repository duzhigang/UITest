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

import static com.ggec.uitest.ui.nsd.NsdClient.NSD_DEVICE_ADD;
import static com.ggec.uitest.ui.nsd.NsdClient.NSD_DISCOVER_REMOVE;

public class NsdClientActivity extends FragmentActivity {
    private static final String TAG = "NsdClientActivity";

    private ListView lvResults;
    private ArrayAdapter<String> adapter;

    private NsdClientManager nsdClientManager;
    private ArrayList<String> availableDevices =new ArrayList<>();
//    private String nsd_server_name = "WhySystem";
    private String nsd_server_name = "办公室";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsd_client);
        initView();

        nsdClientManager = NsdClientManager.getInstance(this, handler);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG,"msg = " + msg.what);
            super.handleMessage(msg);
            String serviceName = (String) msg.obj;
            Log.e(TAG,"obj = " + serviceName);
            if (msg.what == NSD_DEVICE_ADD) {
                if (!availableDevices.contains(serviceName)) {
                    availableDevices.add(serviceName);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } else if (msg.what == NSD_DISCOVER_REMOVE){
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
        nsdClientManager.stopNsdDiscovery();
    }

    private void initView() {
        Button btnStartScan = findViewById(R.id.btn_act_nsd_client_start_scan);
        btnStartScan.setOnClickListener(v -> {
            nsdClientManager.searchNsdServer(nsd_server_name);
        });

        lvResults = findViewById(R.id.lv_act_nsd_client_results);
        adapter = new ArrayAdapter<String>(this, R.layout.one_item_left, R.id.tv_one_item_left_name, availableDevices);
        lvResults.setAdapter(adapter);
    }
}
