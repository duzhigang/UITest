package com.ggec.uitest.ui.netty.server;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ggec.uitest.R;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * 一个Server可以被多个Client连接，但是这个例子点击Send按钮时只能发送给最后一个Active的Channel,点击Send按钮时会主动断开所有的client连接
 * */
public class NettyServerActivity extends FragmentActivity implements View.OnClickListener, NettyServerListener {
    private static final String TAG = "ClientActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netty_server);
        findViewById(R.id.btn_act_netty_server_start).setOnClickListener(this);
        findViewById(R.id.btn_act_netty_server_send).setOnClickListener(this);
        findViewById(R.id.btn_act_netty_server_disconnect).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_act_netty_server_start:
                startServer();
                break;
            case R.id.btn_act_netty_server_send:
                if (! NettyServer.getInstance().getConnectStatus()) {
                    Toast.makeText(NettyServerActivity.this, "未连接,请先连接", LENGTH_SHORT).show();
                } else {
                    String msg = "server send to client data";
                    NettyServer.getInstance().sendMsgToServer(msg, new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                Log.d(TAG, "Server Write data successful");
                            } else {
                                Log.d(TAG, "Server Write data error");
                            }
                        }
                    });
                }
                break;
            case R.id.btn_act_netty_server_disconnect:
                NettyServer.getInstance().disconnect();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMessageResponse(Object msg) {
        Log.d(TAG,"onMessageResponse,Server received: " + msg);
    }

    @Override
    public void onChannel(Channel channel) {
        Log.d(TAG,"onChannel,channel: " + channel.remoteAddress().toString());
        NettyServer.getInstance().setChannel(channel);
    }

    @Override
    public void onStartServer() {
        Log.d(TAG,"onStartServer,server启动成功");
    }

    @Override
    public void onStopServer() {
        Log.d(TAG,"onStopServer,server关闭");
    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {
        Log.d(TAG,"Server连接的Client状态发生改变，statusCode: " + statusCode);
    }

    private void startServer() {
        if(! NettyServer.getInstance().isServerStart()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NettyServer.getInstance().setListener(NettyServerActivity.this);
                    NettyServer.getInstance().start();
                }
            }).start();

        }else{
            NettyServer.getInstance().disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NettyServer.getInstance().disconnect();
    }
}
