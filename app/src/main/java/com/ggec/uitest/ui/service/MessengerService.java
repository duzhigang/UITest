package com.ggec.uitest.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

/**
 * 如果想让接口跨不同进程工作，可以使用Messenger为服务创建接口;服务可以这种方式定义对应于不同类型Message对象的handler.
 * */
public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    // Command to the service to display a message
    static final int MSG_SAY_HELLO = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG,"onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.v(TAG,"onStart");
    }

    // 每次调用startService都会自动分配一个startId，startId可以用来区分不同的startService的调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    // 服务销毁时调用
    @Override
    public void onDestroy() {
        Log.v(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(TAG,"onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.v(TAG,"onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG,"onBind");
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }
}
