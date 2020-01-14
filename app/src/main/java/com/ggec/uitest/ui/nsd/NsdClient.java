package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

/**
 * DiscoveryListener()和ResolveListener()的接口回调都处于同一个NsdManager线程
 * */
public class NsdClient {
    private static final String TAG = "NsdClient";
    public static final int NSD_DEVICE_ADD = 0x01;
    public static final int NSD_DISCOVER_REMOVE = 0x02;

    // 服务类型的格式：_<应用层协议>._<传输层协议>.
//    private final String SERVER_TYPE = "_http._tcp.";
//    private final String SERVER_TYPE = "_airplay._tcp.";
//    private final String SERVER_TYPE = "_eva-mesh._tcp.";
    private final String SERVER_TYPE = "_spotify-connect._tcp.";
    private NsdManager.DiscoveryListener mDiscoveryListener;
//    private NsdManager.ResolveListener mResolverListener;
    private NsdManager mNsdManager;
    private Context mContext;
    private String mServiceName;
    private Handler mHandler;
    private ArrayList<String> discoveryList = new ArrayList<>();
    private ArrayList<String> resolveList = new ArrayList<>();

    /**
     * @param context:上下文对象
     * @param serviceName  客户端扫描 指定的地址
     */
    public NsdClient(Context context, String serviceName) {
        mContext = context;
        mServiceName = serviceName;
    }

    void startNSDClient(final Handler handler) {
        new Thread(){
            @Override
            public void run() {
                mHandler = handler;
                mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
                initializeDiscoveryListener();//初始化监听器
                getResolveListener();//初始化解析器，这行代码可能不需要
                mNsdManager.discoverServices(SERVER_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);//开启扫描
            }
        }.start();
    }

    /**
     * 扫描解析前的 NsdServiceInfo
     */
    private void initializeDiscoveryListener() {
        // Instantiate a new DiscoveryListener
        // 请注意，此代码段会在发现服务时进行多次检查
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                Log.e(TAG, "onStartDiscoveryFailed(),Error code:" + errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                Log.e(TAG, "onStopDiscoveryFailed(),Error code:" + errorCode);
            }

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(TAG, "onDiscoveryStarted()");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.e(TAG, "onDiscoveryStopped(),serviceType = " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.e(TAG,"onServiceFound thread id = " + Thread.currentThread().getId());
                // A service was found! Do something with it.
                Log.d(TAG, "onServiceFound: " + serviceInfo );
                if (!serviceInfo.getServiceType().equals(SERVER_TYPE)) {
                    // Service type is the string containing the protocol and transport layer for this service.
                    Log.d(TAG, "Unknown Service Type: " + serviceInfo.getServiceType());
                    return;
                } else if (serviceInfo.getServiceName().equals(mServiceName)) {
                    // The name of the service tells the user what they'd be connecting to. It could be "Bob's Chat App".
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else if (serviceInfo.getServiceName().contains("NsdChat")){
                    Log.d(TAG, "NsdChat machine: " + mServiceName);
                }
                discoveryList.add(serviceInfo.toString());
                mNsdManager.resolveService(serviceInfo, getResolveListener());

                mHandler.obtainMessage(NSD_DEVICE_ADD, serviceInfo.getServiceName()).sendToTarget();
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                // When the network service is no longer available.Internal bookkeeping code goes here.
                Log.e(TAG, "onServiceLost(): serviceInfo=" + serviceInfo);
                discoveryList.remove(serviceInfo.toString());

                mHandler.obtainMessage(NSD_DISCOVER_REMOVE, serviceInfo.getServiceName()).sendToTarget();
            }
        };
    }

    /**
     * 解析发现的NsdServiceInfo
     */
    private NsdManager.ResolveListener getResolveListener() {
        NsdManager.ResolveListener resolverListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.e(TAG, "onResolveFailed()");
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG,"onServiceResolved thread id = " + Thread.currentThread().getId());
                int port = serviceInfo.getPort();
                String serviceName = serviceInfo.getServiceName();
                String hostAddress = serviceInfo.getHost().getHostAddress();
                Log.e(TAG, "onServiceResolved 已解析:" + " host:" + hostAddress + ":" + port + " ----- serviceName: " + serviceName);
                resolveList.add(" host:" + hostAddress + ":" + port );
                //TODO 建立网络连接
            }
        };
        return resolverListener;
    }

    void stopNSDClient() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }
}
