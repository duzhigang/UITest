package com.ggec.uitest.ui.nsd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ggec.uitest.R;

import java.io.IOException;
import java.net.ServerSocket;

public class NsdServerActivity extends FragmentActivity {
    private static final String TAG = "NsdServerActivity";

    private EditText etNsdServerName;
    private TextView tvNsdServerName;
    private TextView tvNsdServerRecMsg;
    private NsdServerManager nsdServerManager;

    // 注册 NSD 服务的名称 和 端口 这个可以设置默认固定址，用于客户端通过 NSD_SERVER_NAME 筛选得到服务端地址和端口
//    private String nsd_server_name = "NSD_SERVICE_NAME_TEST";
    private String nsd_server_name = "SONY_AUDIO";
    private int nsd_server_port = 8088;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsd_server);
        initView();

        try {
            // Initialize a server socket on the next available port.
            ServerSocket serverSocket = new ServerSocket(0);
            // Store the chosen port.
            nsd_server_port = serverSocket.getLocalPort();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG,"初始化的serverName：" + nsd_server_name + ",port：" + nsd_server_port);
        nsdServerManager = NsdServerManager.getInstance(this);
        nsdServerManager.registerNsdServer(nsd_server_name, nsd_server_port);
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
        nsdServerManager.unRegisterNsdServer();
    }

    private void initView() {
        etNsdServerName = findViewById(R.id.et_act_nsd_server_name);
        tvNsdServerName = findViewById(R.id.tv_act_nsd_server_name);
        tvNsdServerRecMsg = findViewById(R.id.tv_act_nsd_server_rec_msg);
        Button btnNsdServerReset = findViewById(R.id.btn_act_nsd_server_reset);
        btnNsdServerReset.setOnClickListener(v -> {
            resetServerName();
        });
    }

    /**
     * 重置NSD服务器名称
     */
    public void resetServerName() {
        nsd_server_name = etNsdServerName.getText().toString();
        tvNsdServerName.setText("Nsd 服务端----" + nsd_server_name);
        nsdServerManager.registerNsdServer(nsd_server_name, nsd_server_port);
    }
}
