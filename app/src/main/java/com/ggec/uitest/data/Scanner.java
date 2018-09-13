package com.ggec.uitest.data;

import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by ggec on 2018/8/17.
 * 测试NIO udp广播的发送与接收
 */

public class Scanner {
    private String TAG = "Scanner";

    private String broadcastIp = "";  // 广播地址
    private int broadcastSendPort = 6000; // 广播的目的端口
    private int broadcastRecPort = 6001;
    private DatagramChannel datagramChannel;

    public Scanner(String broadcastIp)    {
        this.broadcastIp = broadcastIp;
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.socket().bind(new InetSocketAddress(broadcastRecPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    datagramChannel.socket().setBroadcast(true);
                    // 针对的是API24及其以上版本才有
//                    channel.setOption(StandardSocketOptions.SO_BROADCAST,true);
                    for (int i = 0; i < 10; i++) {
                        ByteBuffer buffer = ByteBuffer.wrap("Test UDP Broadcast".getBytes("utf-8"));
                        InetAddress inetAddress = InetAddress.getByName(broadcastIp);
                        datagramChannel.send(buffer, new InetSocketAddress(inetAddress, broadcastSendPort));
//                        Thread.sleep(1000);
                        Log.e(TAG,"第" + i + "次发送广播");
                    }
                    Log.e(TAG,"发送udp广播完成");
                } catch (IOException e) {
                    e.printStackTrace();
                }/* catch (InterruptedException e2) {
                    e2.printStackTrace();
                }*/
            }
        }).start();
    }

    public void receive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                byte b[];
                while(true) {
                    buffer.clear();
                    SocketAddress socketAddress = null;
                    try {
                        socketAddress = datagramChannel.receive(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (socketAddress != null) {
                        int position = buffer.position();
                        b = new byte[position];
                        buffer.flip();
                        for(int i=0; i<position; ++i) {
                            b[i] = buffer.get();
                        }
                        try {
                            Log.e(TAG,"receive remote " +  socketAddress.toString() + ":"  + new String(b, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
