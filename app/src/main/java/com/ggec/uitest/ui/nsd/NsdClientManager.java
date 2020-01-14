package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.util.Log;

public class NsdClientManager {
    private static final String TAG = "NsdClientManager";

    private NsdClient nsdClient;
    private Context context;
    private Handler mHandler;
    private volatile static NsdClientManager mNsdClientManager=null;

    private NsdClientManager(Context context,Handler handler){
        this.context = context;
        this.mHandler = handler;
    }

    /**
     * DCL Single Instance
     * @param context
     * @param handler
     * @return NsdClientManager
     */
    public static NsdClientManager getInstance(Context context,Handler handler){
        if(mNsdClientManager == null){
            synchronized (NsdClientManager.class){
                if(mNsdClientManager == null){
                    mNsdClientManager = new NsdClientManager(context,handler);
                }
            }
        }
        return mNsdClientManager;
    }

    /**
     * 通过Nsd 搜索注册过的服务器相关参数进行Socket连接（IP和Port）
     */
    public void searchNsdServer(final String nsdServerName) {
        if (nsdClient == null) {
            nsdClient = new NsdClient(context, nsdServerName);
        }
        nsdClient.startNSDClient(mHandler);
    }

    void stopNsdDiscovery() {
        nsdClient.stopNSDClient();
    }
}
