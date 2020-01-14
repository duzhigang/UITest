package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.util.Log;

public class NsdServerManager {
    private static final String TAG = "NsdServerManager";

    private volatile  static NsdServerManager nsdServerManager;
    private static NSDServer nsdServer;
    private Context context;

    public NsdServerManager(Context context){
        this.context=context;
    }

    public static NsdServerManager getInstance(Context context){
        if(nsdServerManager==null){
            synchronized (NsdServerManager.class){
                if (nsdServerManager==null) {
                    nsdServer=new NSDServer();
                    nsdServerManager = new NsdServerManager(context);
                }
            }
        }
        return nsdServerManager;
    }

    /**
     * 注册Nsd服务
     */
    void registerNsdServer(String nsdServerName, int nsdServerPort) {
        new Thread(new NsdServerRunnable(nsdServerName, nsdServerPort,nsdServer,context)).start();
    }

    /**
     * 取消注册NsdServer
     */
    void unRegisterNsdServer(){
        Log.e(TAG, "unRegisterNsdServer: " );
        nsdServer.stopNSDServer();
    }
}
