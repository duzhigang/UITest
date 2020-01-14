package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class NSDServer {
    public static final String TAG = "NSDServer";

    private NsdManager mNsdManager;
    private NsdManager.RegistrationListener mRegistrationListener;
    private IRegisterState registerState; //NSD服务接口对象
    private String mServerName;
//    private static final String SERVER_TYPE = "_http._tcp.";  // 服务器type，要客户端扫描服务器的一致
    private static final String SERVER_TYPE = "_spotify-connect._tcp.";
//    private static final String SERVER_TYPE = "_airplay._tcp.";

    public NSDServer() {
    }

    public void startNSDServer(Context context, String serviceName, int port) {
        initializeRegistrationListener();
        registerService(context, serviceName, port);
    }

    //初始化化注册监听器
    private void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed!  Put debugging code here to determine why.
                Log.e(TAG, "onRegistrationFailed" + serviceInfo + " ,errorCode:" + errorCode);
                if (registerState != null) {
                    registerState.onRegistrationFailed(serviceInfo, errorCode);
                }
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed.  Put debugging code here to determine why.
                Log.i(TAG, "onUnregistrationFailed serviceInfo: " + serviceInfo + " ,errorCode:" + errorCode);
                if (registerState != null) {
                    registerState.onUnRegistrationFailed(serviceInfo, errorCode);
                }
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                // Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                mServerName = serviceInfo.getServiceName();
                Log.d(TAG,"Server注册成功，其serverName：" + mServerName + ",host：" + serviceInfo.getHost() + ",port：" + serviceInfo.getPort());
                Log.i(TAG, "onServiceRegistered: " + serviceInfo.toString());
                if (registerState != null) {
                    registerState.onServiceRegistered(serviceInfo);
                }
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
                Log.i(TAG, "onServiceUnregistered serviceInfo: " + serviceInfo);
                if (registerState != null) {
                    registerState.onServiceUnregistered(serviceInfo);
                }
            }
        };
    }

    private void registerService(Context context, String serviceName, int port) {
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setPort(port);
        serviceInfo.setServiceType(SERVER_TYPE);
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    public void stopNSDServer() {
        // 让别人扫描不到你的NSD服务器
        mNsdManager.unregisterService(mRegistrationListener);
    }

    //设置NSD服务接口对象
    public void setRegisterState(IRegisterState registerState) {
        this.registerState = registerState;
    }

    //NSD服务注册监听接口
    public interface IRegisterState {
        void onServiceRegistered(NsdServiceInfo serviceInfo);     //注册NSD成功

        void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode);   //注册NSD失败

        void onServiceUnregistered(NsdServiceInfo serviceInfo);  //取消NSD注册成功

        void onUnRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode); //取消NSD注册失败

    }
}
