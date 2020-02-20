package com.ggec.uitest.ui.netty.multiclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ggec.uitest.R;

import java.util.Map;

public class MultiClientActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "TwoActivity";
    private MultiNettyClient multiNettyClient;
    private final static int PORT = 60000;
    private final static String HOST_ONE = "192.168.33.15";
    private final static String HOST_TWO = "192.168.33.12";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_client);
        Button btnConnectOne = findViewById(R.id.btn_act_multi_client_connect_one);
        btnConnectOne.setOnClickListener(this);
        Button btnSendOne = findViewById(R.id.btn_act_multi_client_send_one);
        btnSendOne.setOnClickListener(this);
        Button btnDisconnectOne = findViewById(R.id.btn_act_multi_client_disconnect_one);
        btnDisconnectOne.setOnClickListener(this);
        Button btnConnectTwo = findViewById(R.id.btn_act_multi_client_connect_two);
        btnConnectTwo.setOnClickListener(this);
        Button btnSendTwo = findViewById(R.id.btn_act_multi_client_send_two);
        btnSendTwo.setOnClickListener(this);
        Button btnDisconnectTwo = findViewById(R.id.btn_act_multi_client_disconnect_two);
        btnDisconnectTwo.setOnClickListener(this);
        Button btnConnectDouble = findViewById(R.id.btn_act_multi_client_connect_double);
        btnConnectDouble.setOnClickListener(this);
        Button btnSendDouble = findViewById(R.id.btn_act_multi_client_send_double);
        btnSendDouble.setOnClickListener(this);
        Button btnDisconnectDouble = findViewById(R.id.btn_act_multi_client_disconnect_double);
        btnDisconnectDouble.setOnClickListener(this);

        multiNettyClient = new MultiNettyClient();
        multiNettyClient.init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_act_multi_client_connect_one:
                multiNettyClient.connectServer(HOST_ONE, PORT);
                break;
            case R.id.btn_act_multi_client_send_one:
                multiNettyClient.sendMsg(HOST_ONE, "client send to server one");
                break;
            case R.id.btn_act_multi_client_disconnect_one:
                multiNettyClient.disconnectServer(HOST_ONE);
                break;
            case R.id.btn_act_multi_client_connect_two:
                multiNettyClient.connectServer(HOST_TWO, PORT);
                break;
            case R.id.btn_act_multi_client_send_two:
                multiNettyClient.sendMsg(HOST_TWO, "client send to server two");
                break;
            case R.id.btn_act_multi_client_disconnect_two:
                multiNettyClient.disconnectServer(HOST_TWO);
                break;
            case R.id.btn_act_multi_client_connect_double:
                multiNettyClient.connectServer(HOST_ONE, PORT);
                multiNettyClient.connectServer(HOST_TWO, PORT);
                break;
            case R.id.btn_act_multi_client_send_double:
                multiNettyClient.sendMsg(HOST_ONE, "client send to double server");
                multiNettyClient.sendMsg(HOST_TWO, "client send to double server");
                break;
            case R.id.btn_act_multi_client_disconnect_double:
                multiNettyClient.disconnectServer(HOST_ONE);
                multiNettyClient.disconnectServer(HOST_TWO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        multiNettyClient.exit();
    }

    // 打印当前进程的所有线程信息
    private void printThread() {
        Map<Thread, StackTraceElement[]> stacks = Thread.getAllStackTraces();
        for (Thread key : stacks.keySet()) {
            Log.d(TAG, "print thread: " + key.getName());
        }
    }
}
