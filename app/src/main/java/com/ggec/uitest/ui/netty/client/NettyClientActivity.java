package com.ggec.uitest.ui.netty.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ggec.uitest.R;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import static android.widget.Toast.LENGTH_SHORT;

public class NettyClientActivity extends FragmentActivity implements View.OnClickListener, NettyClientListener {
    private static final String TAG = "ClientActivity";
    private final static int PORT = 60000;
    private final static String HOST_ONE = "192.168.33.12";

    private NettyClient nettyClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netty_client);
        findViewById(R.id.btn_act_netty_client_connect).setOnClickListener(this);
        findViewById(R.id.btn_act_netty_client_send).setOnClickListener(this);
        findViewById(R.id.btn_act_netty_client_disconnect).setOnClickListener(this);

        nettyClient = new NettyClient(HOST_ONE, PORT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_act_netty_client_connect:
                connect();
                break;
            case R.id.btn_act_netty_client_send:
                if (!nettyClient.getConnectStatus()) {
                    Toast.makeText(getApplicationContext(), "未连接,请先连接", LENGTH_SHORT).show();
                } else {
                    String msg = "client send to server data";
                    nettyClient.sendMsgToServer(msg, new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {                //4
                                Log.d(TAG, "Write auth successful");
                            } else {
                                Log.d(TAG, "Write auth error");
                            }
                        }
                    });
                }
                break;
            case R.id.btn_act_netty_client_disconnect:
                nettyClient.disconnect();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMessageResponse(Object msg) {
        Log.d(TAG, "onMessageResponse:" + msg);
    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {
        Log.d(TAG,"onServiceStatusConnectChanged：" + statusCode);
    }

    private void connect() {
        Log.d(TAG, "connect");
        if (!nettyClient.getConnectStatus()) {
            nettyClient.setNettyClientListener(NettyClientActivity.this);
            nettyClient.connect();//连接服务器
        } else {
            nettyClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nettyClient.disconnect();
    }
}
