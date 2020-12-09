package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DiscoveryListener()和ResolveListener()的接口回调都处于同一个NsdManager线程
 * */
public class NsdClient {
    private static final String TAG = "NsdClient";

    // 服务类型的格式：_<应用层协议>._<传输层协议>.
//    private final String SERVER_TYPE = "_http._tcp.";
//    private final String SERVER_TYPE = "_airplay._tcp.";
//    private final String SERVER_TYPE = "_eva-mesh._tcp.";
//    private final String SERVER_TYPE = "_spotify-connect._tcp.";
    private final String SERVER_TYPE = "_ggec-iar._tcp.";
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;
    private NsdManager mNsdManager;
    private Context mContext;
    private String mServiceName;
    private ConcurrentLinkedQueue<NsdServiceInfo> pendingNsdServices = new ConcurrentLinkedQueue<>();
    private ArrayList<String> resolvedNsdServices = new ArrayList<>();
    private AtomicBoolean resolveListenerBusy = new AtomicBoolean(false);
    private NSDServiceInterface nsdServiceInterface;

    /**
     * @param context:上下文对象
     * @param serviceName  客户端扫描 指定的地址
     */
    public NsdClient(Context context, String serviceName) {
        mContext = context;
        mServiceName = serviceName;
        mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
    }

    public NsdClient(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
        initializeResolveListener();
    }

    public void setNsdServiceInterface(NSDServiceInterface nsdServiceInterface) {
        this.nsdServiceInterface = nsdServiceInterface;
    }

    /**
     * 扫描解析前的 NsdServiceInfo
     * 回调是在NsdManager自己开启的线程里面运行，和NsdManager.ResolveListener在同一个线程;
     * DiscoveryListener这个监听中的NsdServiceInfo只能获取到名字,ip和端口都不能获取到,要想获取到需要
     * 调用NsdManager.resolveService方法。
     */
    private void initializeDiscoveryListener() {
        // Instantiate a new DiscoveryListener
        // 请注意，此代码段会在发现服务时进行多次检查
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                Log.w(TAG, "onStartDiscoveryFailed(),errorCode = " + errorCode + ",thread id = " + Thread.currentThread().getId());
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                Log.w(TAG, "onStopDiscoveryFailed(),errorCode = " + errorCode + ",thread id = " + Thread.currentThread().getId());
            }

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.v(TAG, "onDiscoveryStarted(),serviceType = " + serviceType + ",thread id = " + Thread.currentThread().getId());
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.v(TAG, "onDiscoveryStopped(),serviceType = " + serviceType + ",thread id = " + Thread.currentThread().getId());
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                // A service was found! Do something with it.
                Log.i(TAG, "onServiceFound(), serviceInfo = " + serviceInfo + ",thread id = " + Thread.currentThread().getId());
                if (!serviceInfo.getServiceType().equals(SERVER_TYPE)) {
                    // Service type is the string containing the protocol and transport layer for this service.
                    Log.d(TAG, "Unknown Service Type: " + serviceInfo.getServiceType());
                    return;
                }
                // Both service type and service name are the ones we want
                // If the resolver is free, resolve the service to get all the details
                if (resolveListenerBusy.compareAndSet(false, true)) {
                    Log.e(TAG,"Start resolve");
                    mNsdManager.resolveService(serviceInfo, mResolveListener);
                }
                else {
                    // Resolver was busy. Add the service to the list of pending services
                    Log.e(TAG,"Add to pendingNsdServices");
                    pendingNsdServices.add(serviceInfo);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                // When the network service is no longer available.Internal bookkeeping code goes here.
                Log.i(TAG, "onServiceLost(), serviceName=" + serviceInfo.getServiceName() + ",thread id = " + Thread.currentThread().getId());
                // If the lost service was in the queue of pending services, remove it
                Iterator<NsdServiceInfo> iterator = pendingNsdServices.iterator();
                while (iterator.hasNext()) {
                    if (serviceInfo.getServiceName().equals(iterator.next().getServiceName())) {
                        Log.e(TAG,"Remove pendingNsdServices");
                        iterator.remove();
                    }
                }
                nsdServiceInterface.onNsdServiceLost(serviceInfo.getServiceName());
            }
        };
    }

    /**
     * 解析发现的NsdServiceInfo
     */
    private void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.w(TAG, "onResolveFailed(),errorCode:" + errorCode + ",thread id = " + Thread.currentThread().getId());
                // Process the next service waiting to be resolved
                resolveNextInQueue();
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                int port = serviceInfo.getPort();
                String serviceName = serviceInfo.getServiceName();
                String hostAddress = serviceInfo.getHost().getHostAddress();
                Map<String, byte[]> attributes = serviceInfo.getAttributes();
                Log.i(TAG,"onServiceResolved(), serviceInfo" + serviceInfo + ",thread id = " + Thread.currentThread().getId());
                // Register the newly resolved service into our list of resolved services
//                resolvedNsdServices.add(serviceName);
                nsdServiceInterface.onNsdServiceResolved(serviceName);
                // Process the next service waiting to be resolved
                resolveNextInQueue();
            }
        };
    }

    // 解析PendingNsdServices中的下一个NSD Service
    private void resolveNextInQueue() {
        // Get the next NSD service waiting to be resolved from the queue
        NsdServiceInfo nextNsdService = pendingNsdServices.poll();
        if (nextNsdService != null) {
            // There was one. Send to be resolved.
            mNsdManager.resolveService(nextNsdService, mResolveListener);
        }
        else {
            // There was no pending service. Release the flag
            resolveListenerBusy.set(false);
        }
    }

    // 开始扫描
    void startServiceDiscover() {
        // Cancel any existing discovery request
        stopServiceDiscover();
        initializeDiscoveryListener(); //初始化监听器
        // 开启扫描
        mNsdManager.discoverServices(SERVER_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

    }

    // 停止扫描
    void stopServiceDiscover() {
        if (mDiscoveryListener != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            mDiscoveryListener = null;
        }
    }

    interface NSDServiceInterface {
        void onNsdServiceResolved(String serviceName);
        void onNsdServiceLost(String serviceName);
    }
}
