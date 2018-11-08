package com.ggec.uitest.ui.socket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/8/31.
 * 测试Socket通信相关内容
 */

public class SocketActivity extends FragmentActivity {
    private static final String TAG = "SocketActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UdpBroadcastFragment fragment = new UdpBroadcastFragment();
        ft.replace(R.id.socket_frame, fragment).commit();
    }
}
