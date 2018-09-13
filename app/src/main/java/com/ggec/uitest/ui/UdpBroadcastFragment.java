package com.ggec.uitest.ui;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ggec.uitest.R;
import com.ggec.uitest.data.Scanner;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by ggec on 2018/8/10.
 * 测试UDP广播
 */

public class UdpBroadcastFragment extends Fragment {
    private static final String TAG = "UdpBroadcastFragment";

    private Scanner scanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_udp_broadcast, container, false);
        String broadcastIp = getBroadcastIp();
        scanner = new Scanner(broadcastIp);
        Button btnSendStart = (Button) view.findViewById(R.id.btn_udp_broadcast_send_start);
        btnSendStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"开始发送广播");
                scanner.send();
            }
        });
        Button btnSendStop = (Button) view.findViewById(R.id.btn_udp_broadcast_send_close);
        btnSendStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "暂未实现", Toast.LENGTH_SHORT).show();
            }
        });
        Button btnRecStart = (Button) view.findViewById(R.id.btn_udp_broadcast_rec_start);
        btnRecStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"接收广播");
                scanner.receive();
            }
        });
        Button btnRecStop = (Button) view.findViewById(R.id.btn_udp_broadcast_rec_close);
        btnRecStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "暂未实现", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public String getBroadcastIp() {
        WifiManager mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        android.net.wifi.WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        int ip = mWifiInfo.getIpAddress();
        String ipStr = (ip & 0xFF)+ "." + ((ip >> 8 ) & 0xFF) + "." + ((ip >> 16 ) & 0xFF) +"."+((ip >> 24 ) & 0xFF );
        String broadcastIp = ipStr.substring(0, ipStr.lastIndexOf(".")).concat(".255");
        Log.v(TAG,"获取到当前网络的广播地址 = " + broadcastIp);
        return broadcastIp;
    }
}
