package com.ggec.uitest.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

/**
 * Service中的各个回调方法是运行在主线程中的
 * 1.bind方式启动服务
 *   bindService——>onCreate——>onBind——>onServiceConnected-->unbindService——>onUnbind——>onDestroy
 *   注意：
 *   (1).App A绑定App B的service，App A多次调用bindService()，而不调用unbindService()，此时App B的service的onBind()只执行一次
 *   (2).App A，App C绑定App B的service，App A和App C各调用一次或多次bindService()，而不调用unbindService()，此时App B的service的onBind()只执行一次
 * 2.start方式启动服务
 *   startService——>onCreate——>onStartCommand——>onStart——>onServiceConnected——>onUnbind——>onDestroy
 *   注意：如果多次执行了Context的startService方法启动Service，Service方法的onCreate方法只会在第一次创建
 *   Service的时候调用一次，以后均不会再次调用，我们可以在onCreate方法中完成一些Service初始化相关的操作；
 *   如果多次执行了，则onStartCommand方法也会相应的多次调用，onStartCommand方法很重要，我们在该方法中根据
 *   传入的Intent参数进行实际的操作，比如会在此处创建一个线程用于下载数据或播放音乐等。
 * */
public class LocalService extends Service {
    private static final String TAG = "LocalService";
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

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
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    // 在通过startService使用Service时，我们在重写onBind方法时，只需要将其返回null即可；
    // onBind方法主要是用于给bindService方法调用Service时才会使用到。
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG,"onBind");
        return mBinder;
    }

    // method for clients
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
}
