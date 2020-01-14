package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class NsdServerRunnable implements Runnable {
    private static final String TAG = "NsdServerRunnable";
    /**
     * 注册 NSD 服务的名称 和 端口 这个可以设置默认固定址，用于客户端通过 NSD_SERVER_NAME 筛选得到服务端地址和端口
     */
    private String nsdServerName ;
    private int nsdServerPort;
    private NSDServer nsdServer;
    private Context context;

    public NsdServerRunnable(String nsdServerName, int nsdServerPort, NSDServer nsdServer, Context context){
        this.nsdServerName=nsdServerName;
        this.nsdServerPort = nsdServerPort;
        this.nsdServer=nsdServer;
        this.context=context;
    }

    @Override
    public void run() {
        nsdServer.startNSDServer(context, nsdServerName, nsdServerPort);
        nsdServer.setRegisterState(new NSDServer.IRegisterState() {
            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "onServiceRegistered: " + serviceInfo.toString());
            }
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "onRegistrationFailed: " + serviceInfo.toString() + ",errorCode = " + errorCode);
            }
            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "onServiceUnregistered: " + serviceInfo.toString());
            }
            @Override
            public void onUnRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "onUnRegistrationFailed: " + serviceInfo.toString() + ",errorCode = " + errorCode);
            }
        });
    }
}
